package io.github.erp.internal.report.attachment;

import io.github.erp.internal.model.HasChecksum;
import io.github.erp.service.dto.*;
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
public class AttachedDepreciationReportDTO implements  AttachedUnTamperedReport<DepreciationReportDTO>, HasChecksum {

    private Long id;

    @NotNull
    private String reportName;

    @NotNull
    private ZonedDateTime timeOfReportRequest;

    private String fileChecksum;

    private Boolean tampered;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    private ApplicationUserDTO requestedBy;

    private DepreciationPeriodDTO depreciationPeriod;

    private ServiceOutletDTO serviceOutlet;

    private AssetCategoryDTO assetCategory;

    @Override
    public void setChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    @Override
    public AttachedUnTamperedReport<DepreciationReportDTO> setReportAttachment(byte[] reportResource) {

        this.reportFile = reportResource;

        return this;
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
        return this.tampered;
    }
}
