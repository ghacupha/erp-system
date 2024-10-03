package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.attachment.models.AttachedPrepaymentByAccountReportRequisitionDTO;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Attachment service for the prepayment-by-account report
 */
@Transactional(readOnly = true)
@Service
public class PrepaymentByAccountReportRequisitionAttachmentService
    extends AbstractUnTamperedReportAttachmentService<PrepaymentByAccountReportRequisitionDTO>
    implements ReportAttachmentService<PrepaymentByAccountReportRequisitionDTO> {


    private final Mapping<PrepaymentByAccountReportRequisitionDTO, AttachedPrepaymentByAccountReportRequisitionDTO> mapping;

    public PrepaymentByAccountReportRequisitionAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<PrepaymentByAccountReportRequisitionDTO, AttachedPrepaymentByAccountReportRequisitionDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public PrepaymentByAccountReportRequisitionDTO attachReport(PrepaymentByAccountReportRequisitionDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedPrepaymentByAccountReportRequisitionDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
