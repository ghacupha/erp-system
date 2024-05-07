package io.github.erp.internal.report.attachment.models;

import io.github.erp.internal.model.HasChecksum;
import io.github.erp.internal.report.attachment.AttachedUnTamperedReport;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Model for report-attachment protocol on the prepayment-by-account-report-requisition api
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachedPrepaymentByAccountReportRequisitionDTO  implements AttachedUnTamperedReport<PrepaymentByAccountReportRequisitionDTO>, HasChecksum {

    private Long id;

    private UUID requestId;

    @NotNull
    private ZonedDateTime timeOfRequisition;

    private String fileChecksum;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    @NotNull
    private LocalDate reportDate;

    private Boolean tampered;

    private ApplicationUserDTO requestedBy;

    private ApplicationUserDTO lastAccessedBy;

    @Override
    public void setChecksum(String fileChecksum) {

        this.fileChecksum = fileChecksum;
    }

    @Override
    public AttachedUnTamperedReport<PrepaymentByAccountReportRequisitionDTO> setReportAttachment(byte[] reportResource) {

        this.reportFile = reportResource;

        return this;
    }

    /**
     * @return Report name as it is saved in the DB
     */
    @Override
    public String getReportName() {
        return requestId.toString();
    }

    /**
     * @return Filename as UUID
     */
    @Override
    public UUID getReportId() {
        return filename;
    }

    @Override
    public void setReportTampered(boolean reportIsTampered) {

        this.tampered = reportIsTampered;
    }

    @Override
    public boolean getReportTampered() {
        return tampered;
    }
}
