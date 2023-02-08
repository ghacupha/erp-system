package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.internal.model.AttachedExcelReportExportDTO;
import io.github.erp.internal.model.mapping.AttachedExcelReportExportDTOMapping;
import io.github.erp.service.dto.ExcelReportExportDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ReportAttachmentService for the excelReportExport entity
 */
@Service
public class ExcelReportExportAttachmentService
    extends AbstractReportAttachmentService<ExcelReportExportDTO>
    implements ReportAttachmentService<ExcelReportExportDTO> {

    private final Mapping<ExcelReportExportDTO, AttachedExcelReportExportDTO> mapping;

    public ExcelReportExportAttachmentService(@Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
                                              Mapping<ExcelReportExportDTO, AttachedExcelReportExportDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public ExcelReportExportDTO attachReport(ExcelReportExportDTO one) {
        one.setReportFileContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return mapping.toValue1((AttachedExcelReportExportDTO) super.attachReport(mapping.toValue2(one), ".xlsx"));
    }
}
