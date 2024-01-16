package io.github.erp.internal.report.dynamic;

import net.sf.dynamicreports.report.exception.DRException;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ReportGenerator {

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

