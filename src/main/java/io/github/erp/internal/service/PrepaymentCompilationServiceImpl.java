package io.github.erp.internal.service;

import io.github.erp.domain.PrepaymentAccount;
import io.github.erp.repository.PrepaymentAccountRepository;
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.service.PrepaymentAmortizationService;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * asynchronous processing of compilation request with callback
 */
@Service
public class PrepaymentCompilationServiceImpl implements PrepaymentCompilationService {

    private final PrepaymentMarshallingRepository prepaymentMarshallingRepository;
    private final PrepaymentAccountRepository prepaymentAccountRepository;
    private final PrepaymentAmortizationService prepaymentAmortizationService;

    public PrepaymentCompilationServiceImpl(PrepaymentMarshallingRepository prepaymentMarshallingRepository, PrepaymentAccountRepository prepaymentAccountRepository, PrepaymentAmortizationService prepaymentAmortizationService) {
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentAccountRepository = prepaymentAccountRepository;
        this.prepaymentAmortizationService = prepaymentAmortizationService;
    }

    @Override
    @Async
    public void compile(PrepaymentCompilationRequestDTO compilationRequest, PrepaymentCompilationCompleteSequence completeSequence) {

        long numberOfProcessedItems = prepaymentMarshallingRepository.findAll().stream()
            .filter(marshal -> !marshal.getInactive())
            .filter(marshal -> !marshal.getProcessed())
            // TODO map to amortization
            .map(prepaymentMarshalling -> prepaymentAccountRepository.getById(prepaymentMarshalling.getPrepaymentAccount().getId()))
            .map(this::mapAmortization)
            .map(prepaymentAmortizationService::save)
            .count();

        compilationRequest.setItemsProcessed(Math.toIntExact(numberOfProcessedItems));


        completeSequence.compilationComplete(compilationRequest);
    }

    private PrepaymentAmortizationDTO mapAmortization(PrepaymentAccount prepaymentAccount) {
        // TODO Create amortization from prepaymentAccount
        return null;
    }
}
