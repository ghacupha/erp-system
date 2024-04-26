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
