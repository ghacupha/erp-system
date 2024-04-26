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
