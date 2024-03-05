package io.github.erp.internal.model.mapping;

import io.github.erp.domain.MonthlyPrepaymentOutstandingReportItemInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.MonthlyPrepaymentOutstandingReportItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class MonthlyPrepaymentOutstandingReportInternalMapper
    implements Mapping<MonthlyPrepaymentOutstandingReportItemInternal,
    MonthlyPrepaymentOutstandingReportItemDTO> {


    @Override
    public MonthlyPrepaymentOutstandingReportItemInternal toValue1(MonthlyPrepaymentOutstandingReportItemDTO vs) {
        return new MonthlyPrepaymentOutstandingReportItemInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public LocalDate getFiscalMonthEndDate() {

                return vs.getFiscalMonthEndDate();
            }

            @Override
            public BigDecimal getTotalPrepaymentAmount() {

                return vs.getTotalPrepaymentAmount();
            }

            @Override
            public BigDecimal getTotalAmortisedAmount() {

                return vs.getTotalAmortisedAmount();
            }

            @Override
            public BigDecimal getTotalOutstandingAmount() {

                return vs.getTotalOutstandingAmount();
            }

            @Override
            public Integer getNumberOfPrepaymentAccounts() {

                return vs.getNumberOfPrepaymentAccounts();
            }
        };
    }

    @Override
    public MonthlyPrepaymentOutstandingReportItemDTO toValue2(MonthlyPrepaymentOutstandingReportItemInternal vs) {

        MonthlyPrepaymentOutstandingReportItemDTO dto = new MonthlyPrepaymentOutstandingReportItemDTO();

        dto.setId(vs.getId());
        dto.setFiscalMonthEndDate(vs.getFiscalMonthEndDate());
        dto.setTotalPrepaymentAmount(vs.getTotalPrepaymentAmount());
        dto.setTotalAmortisedAmount(vs.getTotalAmortisedAmount());
        dto.setTotalOutstandingAmount(vs.getTotalOutstandingAmount());
        dto.setNumberOfPrepaymentAccounts(vs.getNumberOfPrepaymentAccounts());
        return dto;
    }
}
