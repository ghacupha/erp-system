package io.github.erp.internal.service;

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
