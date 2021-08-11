package io.github.erp.internal.framework.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * This service the uploaded file in whatever way that is deemed necessary.
 * This service returns an instance of itself inorder to run withing a chain of file-handlers
 */
public interface HandlingService<H> {

    /**
     * Returns an instance of this after handling the payload issued
     *
     * @param payload The item being handled
     */
    void handle(H payload);
}