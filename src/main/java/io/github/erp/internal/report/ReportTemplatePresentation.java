package io.github.erp.internal.report;

public interface ReportTemplatePresentation<T> {

    String presentTemplate(T dto);
}
