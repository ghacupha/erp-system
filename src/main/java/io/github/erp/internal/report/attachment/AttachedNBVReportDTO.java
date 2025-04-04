package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.internal.model.HasChecksum;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.NbvReportDTO;
import io.github.erp.service.dto.ServiceOutletDTO;
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
public class AttachedNBVReportDTO implements  AttachedUnTamperedReport<NbvReportDTO>, HasChecksum {

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
    public AttachedUnTamperedReport<NbvReportDTO> setReportAttachment(byte[] reportResource) {
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
