package io.github.erp.internal.report;

import io.github.erp.internal.model.AttachedXlsxReportRequisitionDTO;

public interface AttachedReport<DTO> {

    AttachedReport<DTO> setReportAttachment(byte[] reportResource);

    String getReportName();

    String getReportId();
}
