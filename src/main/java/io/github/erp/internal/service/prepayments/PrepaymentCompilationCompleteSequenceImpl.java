package io.github.erp.internal.service.prepayments;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.enumeration.CompilationStatusTypes;
import io.github.erp.service.PrepaymentCompilationRequestService;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrepaymentCompilationCompleteSequenceImpl implements PrepaymentCompilationCompleteSequence {

    private final PrepaymentCompilationRequestService prepaymentCompilationRequestService;

    public PrepaymentCompilationCompleteSequenceImpl(PrepaymentCompilationRequestService prepaymentCompilationRequestService) {
        this.prepaymentCompilationRequestService = prepaymentCompilationRequestService;
    }

    @Override
    public void compilationComplete(PrepaymentCompilationRequestDTO compilationRequest) {


        compilationRequest.setCompilationStatus(CompilationStatusTypes.COMPLETE);

        prepaymentCompilationRequestService.save(compilationRequest);
    }
}
