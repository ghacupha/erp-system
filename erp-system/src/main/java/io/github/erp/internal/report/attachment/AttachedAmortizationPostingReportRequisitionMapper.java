package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedAmortizationPostingReportRequisitionMapper implements Mapping<AmortizationPostingReportRequisitionDTO, AttachedAmortizationPostingReportRequisition> {


    @Override
    public AmortizationPostingReportRequisitionDTO toValue1(AttachedAmortizationPostingReportRequisition vs) {

        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisition = new AmortizationPostingReportRequisitionDTO();

        amortizationPostingReportRequisition.setId(vs.getId());
        amortizationPostingReportRequisition.setRequestId(vs.getRequestId());
        amortizationPostingReportRequisition.setTimeOfRequisition(vs.getTimeOfRequisition());
        amortizationPostingReportRequisition.setFileChecksum(vs.getFileChecksum());
        amortizationPostingReportRequisition.setTampered(vs.getTampered());
        amortizationPostingReportRequisition.setFilename(vs.getFilename());
        amortizationPostingReportRequisition.setReportParameters(vs.getReportParameters());
        amortizationPostingReportRequisition.setReportFile(vs.getReportFile());
        amortizationPostingReportRequisition.setReportFileContentType(vs.getReportFileContentType());
        amortizationPostingReportRequisition.setAmortizationPeriod(vs.getAmortizationPeriod());
        amortizationPostingReportRequisition.setRequestedBy(vs.getRequestedBy());
        amortizationPostingReportRequisition.setLastAccessedBy(vs.getLastAccessedBy());

        return amortizationPostingReportRequisition;
    }

    @Override
    public AttachedAmortizationPostingReportRequisition toValue2(AmortizationPostingReportRequisitionDTO vs) {
        return AttachedAmortizationPostingReportRequisition.builder()
            .id(vs.getId())
            .requestId(vs.getRequestId())
            .timeOfRequisition(vs.getTimeOfRequisition())
            .fileChecksum(vs.getFileChecksum())
            .tampered(vs.getTampered())
            .filename(vs.getFilename())
            .reportParameters(vs.getReportParameters())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .amortizationPeriod(vs.getAmortizationPeriod())
            .requestedBy(vs.getRequestedBy())
            .lastAccessedBy(vs.getLastAccessedBy())
            .build();
    }
}
