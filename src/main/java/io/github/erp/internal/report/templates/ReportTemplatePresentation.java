package io.github.erp.internal.report.templates;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * This interface is a general interface for clients that read report requests and fetch the
 * appropriate report template and dump it on the report directory path. The goal is to have
 * the right template when one is needed, so that the next step in report generation can commence
 *
 * @param <T>
 */
public interface ReportTemplatePresentation<T> {

    String presentTemplate(T dto);
}
