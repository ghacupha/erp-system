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
