package io.github.erp.internal.service.autonomousReport;

import io.github.erp.domain.PrepaymentReportTuple;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.PrepaymentReportDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PrepaymentReportTupleMapper implements Mapping<PrepaymentReportTuple, PrepaymentReportDTO> {

    @Override
    public PrepaymentReportTuple toValue1(PrepaymentReportDTO vs) {

        PrepaymentReportTuple report = new PrepaymentReportTuple() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getCatalogueNumber() {
                return vs.getCatalogueNumber();
            }

            @Override
            public String getParticulars() {
                return vs.getParticulars();
            }

            @Override
            public String getDealerName() {
                return vs.getDealerName();
            }

            @Override
            public String getPaymentNumber() {
                return vs.getPaymentNumber();
            }

            @Override
            public LocalDate getPaymentDate() {
                return vs.getPaymentDate();
            }

            @Override
            public String getCurrencyCode() {
                return vs.getCurrencyCode();
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
        };

        return report;
    }

    @Override
    public PrepaymentReportDTO toValue2(PrepaymentReportTuple vs) {

        PrepaymentReportDTO report = new PrepaymentReportDTO();
        report.setId(vs.getId());
        report.setCatalogueNumber(vs.getCatalogueNumber());
        report.setParticulars(vs.getParticulars());
        report.setDealerName(vs.getDealerName());
        report.setPaymentNumber(vs.getPaymentNumber());
        report.setPaymentDate(vs.getPaymentDate());
        report.setCurrencyCode(vs.getCurrencyCode());
        report.setPrepaymentAmount(vs.getPrepaymentAmount());
        report.setAmortisedAmount(vs.getAmortisedAmount());
        report.setOutstandingAmount(vs.getOutstandingAmount());

        return report;
    }
}
