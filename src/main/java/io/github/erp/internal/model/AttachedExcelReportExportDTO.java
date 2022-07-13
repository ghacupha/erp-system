package io.github.erp.internal.model;

import io.github.erp.internal.report.attachment.AttachedReport;
import io.github.erp.service.dto.AlgorithmDTO;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.dto.ReportDesignDTO;
import io.github.erp.service.dto.ReportStatusDTO;
import io.github.erp.service.dto.SecurityClearanceDTO;
import io.github.erp.service.dto.SystemModuleDTO;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
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
public class AttachedExcelReportExportDTO implements AttachedReport<ExcelReportExportDTO>, HasChecksum{

    @Override
    public void setChecksum(String fileChecksum) {
        this.fileCheckSum = fileChecksum;
    }

    @Override
    public String getFileChecksum() {
        return this.fileCheckSum;
    }

    @Override
    public AttachedReport<ExcelReportExportDTO> setReportAttachment(byte[] reportResource) {
        this.reportFile = reportResource;
        return this;
    }

    @Override
    public String getReportName() {
        return this.reportName;
    }

    @Override
    public UUID getReportId() {
        return this.reportId;
    }


    private Long id;

    @NotNull
    private String reportName;

    @NotNull
    private String reportPassword;

    @Lob
    private byte[] reportNotes;

    private String reportNotesContentType;

    @Lob
    private String fileCheckSum;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    @NotNull
    private ZonedDateTime reportTimeStamp;

    @NotNull
    private UUID reportId;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> parameters = new HashSet<>();

    private ReportStatusDTO reportStatus;

    private SecurityClearanceDTO securityClearance;

    private ApplicationUserDTO reportCreator;

    private DealerDTO organization;

    private DealerDTO department;

    private SystemModuleDTO systemModule;

    private ReportDesignDTO reportDesign;

    private AlgorithmDTO fileCheckSumAlgorithm;
}
