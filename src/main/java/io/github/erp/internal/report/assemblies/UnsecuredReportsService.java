package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
