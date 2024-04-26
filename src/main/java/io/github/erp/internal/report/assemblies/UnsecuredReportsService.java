package io.github.erp.internal.report.assemblies;

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
import java.util.Map;

/**
 * This service is similar to the pdf-reports-service however the report formats
 * namely csv, excel and html do not have direct implementation of password encryption
 * so you will not have those in the description
 */
public interface UnsecuredReportsService {

    /**
     * Creates the PDF report based on the parameters given the most important being the
     * report-template which is also the first argument. The result is a string with the
     * file location
     *
     * @param reportFileLocation Location of the report file i.e. the JRXML or Jasper file
     * @param reportName This is the name of the resulting report after jasper library has generated a PDF report
     * @param parameters Additional parameters for inclusion in the report
     * @return This is the full report path of the generated report
     */
    String generateReport(String reportFileLocation, String reportName, Map<String, Object> parameters);
}
