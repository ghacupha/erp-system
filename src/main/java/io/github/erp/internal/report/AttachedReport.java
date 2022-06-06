package io.github.erp.internal.report;

import io.github.erp.internal.model.AttachedXlsxReportRequisitionDTO;

import java.util.UUID;

public interface AttachedReport<DTO> {

    AttachedReport<DTO> setReportAttachment(byte[] reportResource);

    String getReportName();

    UUID getReportId();
}
