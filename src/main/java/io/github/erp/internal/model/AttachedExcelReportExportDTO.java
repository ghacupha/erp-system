package io.github.erp.internal.model;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
