package io.github.erp.internal.report.attachment;

import io.github.erp.internal.model.HasChecksum;
import io.github.erp.service.dto.AmortizationPeriodDTO;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import io.github.erp.service.dto.ApplicationUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachedAmortizationPostingReportRequisition  implements AttachedUnTamperedReport<AmortizationPostingReportRequisitionDTO>, HasChecksum {

    private Long id;

    @NotNull
    private UUID requestId;

    @NotNull
    private ZonedDateTime timeOfRequisition;

    private String fileChecksum;

    private Boolean tampered;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;
    private AmortizationPeriodDTO amortizationPeriod;

    private ApplicationUserDTO requestedBy;

    private ApplicationUserDTO lastAccessedBy;

    @Override
    public void setChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    @Override
    public AttachedUnTamperedReport<AmortizationPostingReportRequisitionDTO> setReportAttachment(byte[] reportResource) {
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
