package io.github.erp.internal.report.attachment;

import io.github.erp.internal.model.HasChecksum;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachedAssetAdditionsReportDTO implements AttachedUnTamperedReport<AssetAdditionsReportDTO>, HasChecksum {

    private Long id;

    private LocalDate timeOfRequest;

    private LocalDate reportStartDate;

    private LocalDate reportEndDate;

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
    public void setChecksum(String fileChecksum) {

        this.fileChecksum = fileChecksum;
    }

    @Override
    public AttachedUnTamperedReport<AssetAdditionsReportDTO> setReportAttachment(byte[] reportResource) {

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
