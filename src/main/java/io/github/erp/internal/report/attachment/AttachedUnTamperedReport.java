package io.github.erp.internal.report.attachment;

import io.github.erp.internal.model.HasChecksum;

import java.util.UUID;

public interface AttachedUnTamperedReport<DTO> extends HasChecksum {


    AttachedUnTamperedReport<DTO> setReportAttachment(byte[] reportResource);

    /**
     *
     * @return Report name as it is saved in the DB
     */
    String getReportName();

    /**
     *
     * @return Filename as UUID
     */
    UUID getReportId();

    void setReportTampered(boolean reportIsTampered);

    boolean getReportTampered();
}
