package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouDepreciationEntryReportItemInternal;
import io.github.erp.service.dto.RouDepreciationEntryReportItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class RouDepreciationEntryReportItemInternalMapper implements Mapping<RouDepreciationEntryReportItemInternal, RouDepreciationEntryReportItemDTO> {

    @Override
    public RouDepreciationEntryReportItemInternal toValue1(RouDepreciationEntryReportItemDTO vs) {
        return new RouDepreciationEntryReportItemInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getLeaseContractNumber() {
                return vs.getLeaseContractNumber();
            }

            @Override
            public String getFiscalPeriodCode() {
                return vs.getFiscalPeriodCode();
            }

            @Override
            public LocalDate getFiscalPeriodEndDate() {
                return vs.getFiscalPeriodEndDate();
            }

            @Override
            public String getAssetCategoryName() {
                return vs.getAssetCategoryName();
            }

            @Override
            public String getDebitAccountNumber() {
                return vs.getDebitAccountNumber();
            }

            @Override
            public String getCreditAccountNumber() {
                return vs.getCreditAccountNumber();
            }

            @Override
            public String getDescription() {
                return vs.getDescription();
            }

            @Override
            public String getShortTitle() {
                return vs.getShortTitle();
            }

            @Override
            public String getRouAssetIdentifier() {
                return vs.getRouAssetIdentifier();
            }

            @Override
            public Integer getSequenceNumber() {
                return vs.getSequenceNumber();
            }

            @Override
            public BigDecimal getDepreciationAmount() {
                return vs.getDepreciationAmount();
            }

            @Override
            public BigDecimal getOutstandingAmount() {
                return vs.getOutstandingAmount();
            }
        };
    }

    @Override
    public RouDepreciationEntryReportItemDTO toValue2(RouDepreciationEntryReportItemInternal vs) {
        RouDepreciationEntryReportItemDTO dto = new RouDepreciationEntryReportItemDTO();
        dto.setId(vs.getId());
        dto.setLeaseContractNumber(vs.getLeaseContractNumber());
        dto.setFiscalPeriodCode(vs.getFiscalPeriodCode());
        dto.setFiscalPeriodEndDate(vs.getFiscalPeriodEndDate());
        dto.setAssetCategoryName(vs.getAssetCategoryName());
        dto.setDebitAccountNumber(vs.getDebitAccountNumber());
        dto.setCreditAccountNumber(vs.getCreditAccountNumber());
        dto.setDescription(vs.getDescription());
        dto.setShortTitle(vs.getShortTitle());
        dto.setRouAssetIdentifier(vs.getRouAssetIdentifier());
        dto.setSequenceNumber(vs.getSequenceNumber());
        dto.setDepreciationAmount(vs.getDepreciationAmount());
        dto.setOutstandingAmount(vs.getOutstandingAmount());

        return dto;
    }
}
