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
import io.github.erp.domain.*;
import io.github.erp.erp.assets.depreciation.exceptions.FiscalMonthNotConfiguredException;
import io.github.erp.internal.repository.InternalFiscalMonthRepository;
import io.github.erp.repository.PrepaymentAccountRepository;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.repository.search.PrepaymentAmortizationSearchRepository;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.service.mapper.PrepaymentCompilationRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * asynchronous processing of compilation request with callback
 * Update 2023.12.28
 * We have changed the data access to use direct repositories instead of
 * services, so that we might have access to the transactional facility in the
 * event of failure
 */
@Service
@Transactional
public class PrepaymentCompilationServiceImpl implements PrepaymentCompilationService {

    private final static Logger log = LoggerFactory.getLogger(PrepaymentCompilationServiceImpl.class);

    private final PrepaymentMarshallingRepository prepaymentMarshallingRepository;
    private final PrepaymentAccountRepository prepaymentAccountRepository;
    private final InternalFiscalMonthRepository fiscalMonthRepository;
    private final PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper;
    private final PrepaymentCompilationCompleteSequence prepaymentCompilationCompleteSequence;
    private final PrepaymentAmortizationRepository prepaymentAmortizationRepository;
    private final PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository;

    public PrepaymentCompilationServiceImpl(
        PrepaymentMarshallingRepository prepaymentMarshallingRepository,
        PrepaymentAccountRepository prepaymentAccountRepository,
        InternalFiscalMonthRepository fiscalMonthRepository,
        PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper,
        PrepaymentCompilationCompleteSequence prepaymentCompilationCompleteSequence,
        PrepaymentAmortizationRepository prepaymentAmortizationRepository,
        PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository) {
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentAccountRepository = prepaymentAccountRepository;
        this.prepaymentCompilationRequestMapper = prepaymentCompilationRequestMapper;
        this.prepaymentAmortizationRepository = prepaymentAmortizationRepository;
        this.prepaymentAmortizationSearchRepository = prepaymentAmortizationSearchRepository;
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.prepaymentCompilationCompleteSequence = prepaymentCompilationCompleteSequence;
    }

    @Override
    public void compile(PrepaymentCompilationRequestDTO compilationRequest) {

        long numberOfProcessedItems = prepaymentMarshallingRepository.findAllWithEagerRelationships().stream()
            .filter(marshal -> !marshal.getInactive())
            .filter(marshal -> !marshal.getProcessed())
            .peek(prepaymentMarshalling -> prepaymentMarshalling.setProcessed(true))
            .peek(prepaymentMarshalling -> prepaymentMarshalling.setCompilationToken(compilationRequest.getCompilationToken()))
            .flatMap(marshal -> mapIntermediateMarshallingItem(marshal, prepaymentCompilationRequestMapper.toEntity(compilationRequest)))
            .map(prepaymentAmortizationRepository::save) // we also need to ensure the data is in the search index
            .map(prepaymentAmortizationSearchRepository::save)
            .count();

        compilationRequest.setItemsProcessed(Math.toIntExact(numberOfProcessedItems));


        prepaymentCompilationCompleteSequence.compilationComplete(compilationRequest);
    }

    private Stream<PrepaymentAmortization> mapIntermediateMarshallingItem(PrepaymentMarshalling marshalItem, PrepaymentCompilationRequest prepaymentCompilationRequest) {

        var prepaymentAnon = new Object() {
            PrepaymentAccount account = null;
        };

        prepaymentAccountRepository.findOneWithEagerRelationships(marshalItem.getPrepaymentAccount().getId()).ifPresent(prepayment -> {
            prepaymentAnon.account = prepayment;
        });

        List<PrepaymentAmortization> amortizationDTOList = new ArrayList<>();


        for (int period = 0; period <= marshalItem.getAmortizationPeriods() - 1; period++) {

            PrepaymentAmortization dto = new PrepaymentAmortization();
            dto.setPrepaymentAmount(prepaymentAnon.account.getPrepaymentAmount().divide(BigDecimal.valueOf(marshalItem.getAmortizationPeriods()), RoundingMode.HALF_EVEN));
            dto.setFiscalMonth(incrementFiscalMonth(marshalItem, period));
            dto.setPrepaymentCompilationRequest(prepaymentCompilationRequest);
            dto.setPrepaymentAccount(prepaymentAnon.account);
            dto.setDebitAccount(prepaymentAnon.account.getTransferAccount());
            dto.setCreditAccount(prepaymentAnon.account.getDebitAccount());
            dto.setDescription(prepaymentAnon.account.getParticulars());
            dto.setSettlementCurrency(prepaymentAnon.account.getSettlementCurrency());
            dto.setInactive(false);

            amortizationDTOList.add(dto);
        }

        return amortizationDTOList.stream();
    }

    /**
     * Gets fiscal-month and then increments the period to the next period
     */
    private FiscalMonth incrementFiscalMonth(PrepaymentMarshalling marshalling, int period) {

        LocalDate startDate = marshalling.getFirstFiscalMonth().getStartDate().plusMonths(period).with(TemporalAdjusters.firstDayOfMonth());

        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        final FiscalMonth[] fiscalMonth = {null};

        fiscalMonthRepository.findFiscalMonthByStartDateAndEndDate(startDate, endDate).ifPresentOrElse(month -> {

            log.trace("Fiscal month extracted for year: {}, in quarter: {}", month.getFiscalYear().getFiscalYearCode(), month.getFiscalQuarter().getFiscalQuarterCode());

            fiscalMonth[0] = month;
        },
            () -> { throw new FiscalMonthNotConfiguredException(marshalling, startDate, endDate); });

        return fiscalMonth[0];
    }
}
