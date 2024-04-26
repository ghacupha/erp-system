package io.github.erp.internal.report.attachment;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
