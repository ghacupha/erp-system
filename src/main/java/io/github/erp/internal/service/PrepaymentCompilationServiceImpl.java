package io.github.erp.internal.service;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.PrepaymentMarshalling;
import io.github.erp.erp.depreciation.FiscalMonthNotConfiguredException;
import io.github.erp.internal.repository.InternalFiscalMonthRepository;
import io.github.erp.repository.PrepaymentAccountRepository;
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.service.PrepaymentAmortizationService;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.dto.PrepaymentAccountDTO;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.service.mapper.FiscalMonthMapper;
import io.github.erp.service.mapper.PrepaymentAccountMapper;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * asynchronous processing of compilation request with callback
 */
@Service
@Transactional
public class PrepaymentCompilationServiceImpl implements PrepaymentCompilationService {

    private final static Logger log = LoggerFactory.getLogger(PrepaymentCompilationServiceImpl.class);

    private final PrepaymentMarshallingRepository prepaymentMarshallingRepository;
    private final PrepaymentAccountRepository prepaymentAccountRepository;
    private final PrepaymentAmortizationService prepaymentAmortizationService;
    private final PrepaymentAccountMapper prepaymentAccountMapper;
    private final InternalFiscalMonthRepository fiscalMonthRepository;
    private final FiscalMonthMapper fiscalMonthMapper;
    private final PrepaymentCompilationCompleteSequence prepaymentCompilationCompleteSequence;

    public PrepaymentCompilationServiceImpl(PrepaymentMarshallingRepository prepaymentMarshallingRepository, PrepaymentAccountRepository prepaymentAccountRepository, PrepaymentAmortizationService prepaymentAmortizationService, PrepaymentAccountMapper prepaymentAccountMapper, InternalFiscalMonthRepository fiscalMonthRepository, FiscalMonthMapper fiscalMonthMapper, PrepaymentCompilationCompleteSequence prepaymentCompilationCompleteSequence) {
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentAccountRepository = prepaymentAccountRepository;
        this.prepaymentAmortizationService = prepaymentAmortizationService;
        this.prepaymentAccountMapper = prepaymentAccountMapper;
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.fiscalMonthMapper = fiscalMonthMapper;
        this.prepaymentCompilationCompleteSequence = prepaymentCompilationCompleteSequence;
    }

    @Override
    public void compile(PrepaymentCompilationRequestDTO compilationRequest) {

        long numberOfProcessedItems = prepaymentMarshallingRepository.findAllWithEagerRelationships().stream()
            .filter(marshal -> !marshal.getInactive())
            .filter(marshal -> !marshal.getProcessed())
            .peek(prepaymentMarshalling -> prepaymentMarshalling.setProcessed(true))
            .peek(prepaymentMarshalling -> prepaymentMarshalling.setCompilationToken(compilationRequest.getCompilationToken()))
            .flatMap(marshal -> mapIntermediateMarshallingItem(marshal, compilationRequest))
            .map(prepaymentAmortizationService::save)
            .count();

        compilationRequest.setItemsProcessed(Math.toIntExact(numberOfProcessedItems));


        prepaymentCompilationCompleteSequence.compilationComplete(compilationRequest);
    }

    private Stream<PrepaymentAmortizationDTO> mapIntermediateMarshallingItem(PrepaymentMarshalling marshalItem, PrepaymentCompilationRequestDTO prepaymentCompilationRequest) {

        var prepaymentAnon = new Object() {
            PrepaymentAccountDTO account = null;
        };

        prepaymentAccountRepository.findOneWithEagerRelationships(marshalItem.getPrepaymentAccount().getId()).ifPresent(prepayment -> {
            prepaymentAnon.account = prepaymentAccountMapper.toDto(prepayment);
        });

        List<PrepaymentAmortizationDTO> amortizationDTOList = new ArrayList<>();


        for (int period = 0; period <= marshalItem.getAmortizationPeriods() - 1; period++) {

            PrepaymentAmortizationDTO dto = new PrepaymentAmortizationDTO();
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
    private FiscalMonthDTO incrementFiscalMonth(PrepaymentMarshalling marshalling, int period) {

        LocalDate startDate = marshalling.getFirstFiscalMonth().getStartDate().plusMonths(period).with(TemporalAdjusters.firstDayOfMonth());

        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        final FiscalMonthDTO[] fiscalMonthDTO = {null};

        fiscalMonthRepository.findFiscalMonthByStartDateAndEndDate(startDate, endDate).ifPresentOrElse(month -> {

            log.trace("Fiscal month extracted for year: {}, in quarter: {}", month.getFiscalYear().getFiscalYearCode(), month.getFiscalQuarter().getFiscalQuarterCode());

            fiscalMonthDTO[0] = fiscalMonthMapper.toDto(month);
        },
            () -> { throw new FiscalMonthNotConfiguredException(marshalling, startDate, endDate); });

        return fiscalMonthDTO[0];
    }
}
