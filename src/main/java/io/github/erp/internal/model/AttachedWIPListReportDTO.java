package io.github.erp.internal.model;

import io.github.erp.internal.report.attachment.AttachedReport;
import io.github.erp.internal.report.attachment.AttachedUnTamperedReport;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.WIPListReportDTO;
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
public class AttachedWIPListReportDTO implements AttachedUnTamperedReport<WIPListReportDTO>, HasChecksum {

    private Long id;

    @NotNull
    private ZonedDateTime timeOfRequest;

    @NotNull
    private UUID requestId;

    private String fileChecksum;

    private Boolean tampered;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    private ApplicationUserDTO requestedBy;

    @Override
    public AttachedUnTamperedReport<WIPListReportDTO> setReportAttachment(byte[] reportResource) {
        this.reportFile = reportResource;

        return this;
    }

    @Override
    public String getReportName() {
        return filename.toString();
    }

    @Override
    public UUID getReportId() {
        return this.filename;
    }

    @Override
    public void setChecksum(String fileChecksum) {

        this.fileChecksum = fileChecksum;
    }

    @Override
    public String getFileChecksum() {
        return this.fileChecksum;
    }

    @Override
    public void setReportTampered(boolean reportIsTampered) {
        this.tampered = reportIsTampered;
    }

    @Override
    public boolean getReportTampered() {
        return this.tampered;
    }
}
