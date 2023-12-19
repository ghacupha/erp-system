package io.github.erp.internal.service.autonomousReport;

import io.github.erp.domain.PrepaymentAccountReportTuple;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("prepaymentAccountReportTupleMapper")
public class PrepaymentAccountReportTupleMapper implements Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> {

    @Override
    public PrepaymentAccountReportTuple toValue1(PrepaymentAccountReportDTO vs) {
        PrepaymentAccountReportTuple tuple = new PrepaymentAccountReportTuple() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getPrepaymentAccount() {
                return vs.getPrepaymentAccount();
            }

            @Override
            public BigDecimal getPrepaymentAmount() {
                return vs.getPrepaymentAmount();
            }

            @Override
            public BigDecimal getAmortisedAmount() {
                return vs.getAmortisedAmount();
            }

            @Override
            public BigDecimal getOutstandingAmount() {
                return vs.getOutstandingAmount();
            }

            @Override
            public Integer getNumberOfPrepaymentAccounts() {
                return vs.getNumberOfPrepaymentAccounts();
            }

            @Override
            public Integer getNumberOfAmortisedItems() {
                return vs.getNumberOfAmortisedItems();
            }
        };

        return tuple;
    }

    @Override
    public PrepaymentAccountReportDTO toValue2(PrepaymentAccountReportTuple vs) {
        PrepaymentAccountReportDTO report = new PrepaymentAccountReportDTO();
        report.setId(vs.getId());
        report.setPrepaymentAccount(vs.getPrepaymentAccount());
        report.setNumberOfPrepaymentAccounts(vs.getNumberOfPrepaymentAccounts());
        report.setNumberOfAmortisedItems(vs.getNumberOfAmortisedItems());
        report.setPrepaymentAmount(vs.getPrepaymentAmount());
        report.setAmortisedAmount(vs.getAmortisedAmount());
        report.setOutstandingAmount(vs.getOutstandingAmount());

        return report;
    }
}
