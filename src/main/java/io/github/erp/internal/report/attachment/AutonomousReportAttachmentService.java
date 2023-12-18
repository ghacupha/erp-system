package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AutonomousReportDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AutonomousReportAttachmentService
    extends AbstractUnTamperedReportAttachmentService<AutonomousReportDTO>
    implements ReportAttachmentService<AutonomousReportDTO> {

    private final Mapping<AutonomousReportDTO, AttachedAutonomousReportDTO> mapping;

    public AutonomousReportAttachmentService(
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
                                              Mapping<AutonomousReportDTO,
                                                  AttachedAutonomousReportDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public AutonomousReportDTO attachReport(AutonomousReportDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedAutonomousReportDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
