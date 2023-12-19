package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
public class AttachedAutonomousReportDTO implements AttachedUnTamperedReport<AutonomousReportDTO>, HasChecksum {

    @Override
    public void setChecksum(String fileChecksum) {
        this.fileCheckSum = fileChecksum;
    }

    @Override
    public String getFileChecksum() {
        return this.fileCheckSum;
    }

    @Override
    public AttachedUnTamperedReport<AutonomousReportDTO> setReportAttachment(byte[] reportResource) {
        this.reportFile = reportResource;
        return this;
    }

    @Override
    public void setReportTampered(boolean reportIsTampered) {

        this.reportTampered = reportIsTampered;

    }

    @Override
    public boolean getReportTampered() {
        return reportTampered;
    }

    /**
     *
     * @return Report name in the DB
     */
    @Override
    public String getReportName() {
        return this.reportName;
    }

    /**
     *
     * @return Report file name as UUID
     */
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

    private boolean reportTampered;

    private String reportFileContentType;

    private Set<UniversallyUniqueMappingDTO> reportMappings;

    private Set<PlaceholderDTO> placeholders;

    private ApplicationUserDTO createdBy;

    @Lob
    private String fileCheckSum;
}
