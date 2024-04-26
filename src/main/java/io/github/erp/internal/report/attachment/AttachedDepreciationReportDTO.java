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
