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
import lombok.Data;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Data
@Service
public class SimpleJasperReportExporter {
    private JasperPrint jasperPrint;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    @SneakyThrows
    public void exportToPdf(String fileName, String author, String ownerPassword, String userPassword) {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));

        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor(author);
        exportConfig.setEncrypted(true);
        exportConfig.setOwnerPassword(ownerPassword);
        exportConfig.setUserPassword(userPassword);
        exportConfig.set128BitKey(true);
        exportConfig.setCompressed(true);
        // TODO exportConfig.setPdfaConformance(PdfaConformanceEnum.PDFA_1A);
        // exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
    }

    @SneakyThrows
    public void exportToPdf(String fileName, String author) {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));

        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor(author);
        exportConfig.setEncrypted(false);
        exportConfig.setCompressed(true);

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
    }

    @SneakyThrows
    public void exportToXlsx(String fileName, String sheetName) {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));

        SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
        reportConfig.setSheetNames(new String[]{sheetName});

        SimpleXlsxExporterConfiguration exportConfig = new SimpleXlsxExporterConfiguration();
        exportConfig.setMetadataAuthor(applicationName);

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
    }

    @SneakyThrows
    public void exportToCsv(String fileName) {
        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(fileName));

        exporter.exportReport();
    }

    @SneakyThrows
    public void exportToCsv(String fileName, String fieldDelimiter) {
        JRCsvExporter exporter = new JRCsvExporter();

        SimpleCsvExporterConfiguration exportConfig = new SimpleCsvExporterConfiguration();
        exportConfig.setFieldDelimiter(fieldDelimiter);

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(fileName));

        exporter.exportReport();
    }

    @SneakyThrows
    public void exportToHtml(String fileName) {
        HtmlExporter exporter = new HtmlExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(fileName));

        exporter.exportReport();
    }
}
