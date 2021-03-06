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
package org.apache.camel;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Tagging interface to indicate that a type is capable of caching the underlying data stream.
 * <p/>
 * This is a useful feature for avoiding message re-readability issues.
 * This interface is mainly used by the {@link org.apache.camel.processor.interceptor.StreamCachingInterceptor}
 * for determining if/how to wrap a stream-based message.
 *
 * @version 
 */
public interface StreamCache {

    long DEFAULT_SPOOL_THRESHOLD = 128 * 1024;

    /**
     * Resets the StreamCache for a new stream consumption.
     */
    void reset();

    /**
     * Writes the stream to the given output
     *
     * @param os the destination to write to
     * @throws java.io.IOException is thrown if write fails
     */
    void writeTo(OutputStream os) throws IOException;

}
