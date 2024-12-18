package io.github.erp.internal.service.prepayments;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.*;
import io.github.erp.erp.assets.depreciation.exceptions.FiscalMonthNotConfiguredException;
import io.github.erp.internal.repository.InternalFiscalMonthRepository;
import io.github.erp.repository.PrepaymentAccountRepository;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.repository.search.PrepaymentAmortizationSearchRepository;
import io.github.erp.service.dto.AmortizationPeriodDTO;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.service.mapper.AmortizationPeriodMapper;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final InternalAmortizationPeriodService internalAmortizationPeriodService;
    private final AmortizationPeriodMapper amortizationPeriodMapper;

    public PrepaymentCompilationServiceImpl(
        PrepaymentMarshallingRepository prepaymentMarshallingRepository,
        PrepaymentAccountRepository prepaymentAccountRepository,
        InternalFiscalMonthRepository fiscalMonthRepository,
        PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper,
        PrepaymentCompilationCompleteSequence prepaymentCompilationCompleteSequence,
        PrepaymentAmortizationRepository prepaymentAmortizationRepository,
        PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository,
        InternalAmortizationPeriodService internalAmortizationPeriodService,
        AmortizationPeriodMapper amortizationPeriodMapper) {
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentAccountRepository = prepaymentAccountRepository;
        this.prepaymentCompilationRequestMapper = prepaymentCompilationRequestMapper;
        this.prepaymentAmortizationRepository = prepaymentAmortizationRepository;
        this.prepaymentAmortizationSearchRepository = prepaymentAmortizationSearchRepository;
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.prepaymentCompilationCompleteSequence = prepaymentCompilationCompleteSequence;
        this.internalAmortizationPeriodService = internalAmortizationPeriodService;
        this.amortizationPeriodMapper = amortizationPeriodMapper;
    }

    @Override
    public void compile(PrepaymentCompilationRequestDTO compilationRequest) {

        // TODO Implement this is batches

        long numberOfProcessedItems = prepaymentMarshallingRepository.findAllWithEagerRelationships().stream()
            .filter(marshal -> !marshal.getInactive())
            .filter(marshal -> !marshal.getProcessed())
            .count();

        List<PrepaymentAmortization> savedAmortizationList = prepaymentMarshallingRepository.findAllWithEagerRelationships().stream()
            .filter(marshal -> !marshal.getInactive())
            .filter(marshal -> !marshal.getProcessed())
            .peek(prepaymentMarshalling -> prepaymentMarshalling.setProcessed(true))
            .peek(prepaymentMarshalling -> prepaymentMarshalling.setCompilationToken(compilationRequest.getCompilationToken()))
            .flatMap(marshal -> mapIntermediateMarshallingItem(marshal, prepaymentCompilationRequestMapper.toEntity(compilationRequest)))
            .map(prepaymentAmortizationRepository::save)
            .toList();

        compilationRequest.setItemsProcessed(Math.toIntExact(numberOfProcessedItems));
        prepaymentCompilationCompleteSequence.compilationComplete(compilationRequest);

        // Optional indexing
        // savedAmortizationList.forEach(prepaymentAmortizationSearchRepository::save);
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

            FiscalMonth fiscalMonth = incrementFiscalMonth(marshalItem, period);

            PrepaymentAmortization prepaymentAmortization = new PrepaymentAmortization();
            prepaymentAmortization.setPrepaymentAmount(prepaymentAnon.account.getPrepaymentAmount().divide(BigDecimal.valueOf(marshalItem.getAmortizationPeriods()), RoundingMode.HALF_EVEN));
            prepaymentAmortization.setFiscalMonth(fiscalMonth);
            prepaymentAmortization.setAmortizationPeriod(incrementAmortizationPeriod(marshalItem, period));
            prepaymentAmortization.setPrepaymentCompilationRequest(prepaymentCompilationRequest);
            prepaymentAmortization.setPrepaymentAccount(prepaymentAnon.account);
            prepaymentAmortization.setDebitAccount(prepaymentAnon.account.getTransferAccount());
            prepaymentAmortization.setCreditAccount(prepaymentAnon.account.getDebitAccount());
            prepaymentAmortization.setDescription(prepaymentAnon.account.getParticulars());
            prepaymentAmortization.setSettlementCurrency(prepaymentAnon.account.getSettlementCurrency());
            prepaymentAmortization.amortizationIdentifier(UUID.randomUUID());
            prepaymentAmortization.setInactive(false);

            amortizationDTOList.add(prepaymentAmortization);
        }

        return amortizationDTOList.stream();
    }

    private AmortizationPeriod incrementAmortizationPeriod(PrepaymentMarshalling marshalling, int period) {

        long firstAmortizationPeriodId = marshalling.getFirstAmortizationPeriod().getId();

        Optional<AmortizationPeriodDTO> nextPeriod =
            internalAmortizationPeriodService.getNextAmortizationPeriod(firstAmortizationPeriodId, period);

        if (nextPeriod.isPresent()) {
            return amortizationPeriodMapper.toEntity(nextPeriod.get());
        } else {
            throw new AmortizationPeriodNotFoundException("No amortization-period found in the nth sequence after instance id : " + firstAmortizationPeriodId);
        }
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
