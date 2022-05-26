package io.github.erp.internal.report;

/**
 * This interface takes report-requisition data and start the process of creating a report
 * @param <T>
 */
public interface ReportRequisitionService<T> {

    void createReport(T dto);
}
