/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.spring.batch;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

/**
 * Spring Batch Producer triggering the execution of the jobs.
 */
public class SpringBatchProducer extends DefaultProducer {

    private final JobLauncher jobLauncher;

    private final Job job;

    public SpringBatchProducer(SpringBatchEndpoint endpoint, JobLauncher jobLauncher, Job job) {
        super(endpoint);
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        JobParameters jobParameters = prepareJobParameters(exchange.getIn().getHeaders());
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        exchange.getOut().setBody(jobExecution);
    }

    /**
     * Helper method converting the Camel message headers into the Spring Batch parameters map. Date, Long and Double
     * header values are converted to the appropriate types. All the other header values are converted to string
     * representation.
     *
     * @param headers Camel message header to be converted
     * @return Camel message headers converted into the Spring Batch parameters map
     */
    protected JobParameters prepareJobParameters(Map<String, Object> headers) {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        for (Map.Entry<String, Object> headerEntry : headers.entrySet()) {
            String headerKey = headerEntry.getKey();
            Object headerValue = headerEntry.getValue();
            if (headerValue instanceof Date) {
                parametersBuilder.addDate(headerKey, (Date) headerValue);
            } else if (headerValue instanceof Long) {
                parametersBuilder.addLong(headerKey, (Long) headerValue);
            } else if (headerValue instanceof Double) {
                parametersBuilder.addDouble(headerKey, (Double) headerValue);
            } else if (headerValue != null) {
                parametersBuilder.addString(headerKey, headerValue.toString());
            } else {
                // if the value is null we just put String with null value here to avoid the NPE
                parametersBuilder.addString(headerKey, null);
            }
        }
        JobParameters jobParameters = parametersBuilder.toJobParameters();
        log.debug("Prepared parameters for Spring Batch job: {}", jobParameters);
        return jobParameters;
    }

}
