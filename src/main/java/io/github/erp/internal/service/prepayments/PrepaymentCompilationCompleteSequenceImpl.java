package io.github.erp.internal.service.prepayments;

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
