package io.github.erp.internal.report;

public interface AttachedReport<DTO> {

    AttachedReport<DTO> setReportAttachment(byte[] reportResource);
}
