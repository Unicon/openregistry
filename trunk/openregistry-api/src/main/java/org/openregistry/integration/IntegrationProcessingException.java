/**
 * Copyright (C) 2009 Jasig, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openregistry.integration;

/**
 * A RuntimeException indicating an unrecoverable fault during an integration event processing e.g.
 * sending an message to a remote messaging broker destination. This exception wraps the original native
 * integration component exception which is then exposed to OR clients (if needed via Throwable#getCause)
 *
 * @author Dmitriy Kopylenko
 * @since 1.0
 */
public class IntegrationProcessingException extends RuntimeException {

    public IntegrationProcessingException(Throwable throwable) {
        super(throwable);
    }
}
