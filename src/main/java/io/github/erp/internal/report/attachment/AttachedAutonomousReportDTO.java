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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachedAutonomousReportDTO implements AttachedReport<AutonomousReportDTO>, HasChecksum {


    @Override
    public void setChecksum(String fileChecksum) {
        // TODO this.fileCheckSum = fileChecksum;
    }

    @Override
    public String getFileChecksum() {
        // todo return this.fileCheckSum;
        return "";
    }

    @Override
    public AttachedReport<AutonomousReportDTO> setReportAttachment(byte[] reportResource) {
        this.reportFile = reportResource;
        return this;
    }

    @Override
    public String getReportName() {
        return this.reportName;
    }

    @Override
    public UUID getReportId() {
        return this.reportFilename;
    }

    private Long id;

    @NotNull
    private String reportName;

    private String reportParameters;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private UUID reportFilename;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;
    private Set<UniversallyUniqueMappingDTO> reportMappings = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private ApplicationUserDTO createdBy;

    @Lob
    private String fileCheckSum;
}
