package io.github.erp.internal.report.dynamic;

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

