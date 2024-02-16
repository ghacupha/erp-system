package io.github.erp.internal.report.dynamic;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import net.sf.dynamicreports.report.exception.DRException;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ReportGenerator {

    public static <T> byte[] generateExcelReport(List<T> entityList, Class<T> entityType, String systemReportPassword) throws DRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        new GenericReportTemplate<>(entityType)
            .configureReport()
            .setDataSource(entityList)
            .toXlsx(outputStream, systemReportPassword);

        return outputStream.toByteArray();
    }

    public static <T> byte[] generateUnencryptedExcelReport(List<T> entityList, Class<T> entityType, String reportTitle) throws DRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        new GenericReportTemplate<>(entityType)
            .configureReport()
            .setDataSource(entityList)
            .toXlsx(outputStream, reportTitle);

        return outputStream.toByteArray();
    }

    public static <T> byte[] generatePdfReport(List<T> entityList, Class<T> entityType, String userPassword, String systemReportPassword) throws DRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        new GenericReportTemplate<>(entityType)
            .configureReport()
            .setDataSource(entityList)
            .toPdf(outputStream, userPassword, systemReportPassword);

        return outputStream.toByteArray();
    }

    public static <T> byte[] generatePdfReport(List<T> entityList, Class<T> entityType, String systemReportPassword) throws DRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        new GenericReportTemplate<>(entityType)
            .configureReport()
            .setDataSource(entityList)
            .toPdf(outputStream, systemReportPassword);

        return outputStream.toByteArray();
    }
}

