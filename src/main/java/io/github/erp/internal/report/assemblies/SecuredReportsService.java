package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * Creates reports in PDF format, authenticated by both user password and owner password
 */
public interface SecuredReportsService {

    /**
     * Creates the PDF report based on the parameters given the most important being the
     * report-template which is also the first argument. The result is a string with the
     * file location
     *
     * @param reportFileLocation Location of the report file i.e. the JRXML or Jasper file
     * @param reportName This is the name of the resulting report after jasper library has generated a PDF report
     * @param ownerPassword Owner password on the report
     * @param userPassword User password on the report
     * @param parameters Additional parameters for inclusion in the report
     * @return This is the full report path of the generated report
     */
    String generateReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters);
}
