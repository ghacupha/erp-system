package io.github.erp.internal.service.autonomousReport._maps;

import io.github.erp.domain.WorkInProgressReportREPO;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WorkInProgressReportREPOMapper implements Mapping<WorkInProgressReportREPO, WIPByDealerProjectDTO> {

    @Override
    public WorkInProgressReportREPO toValue1(WIPByDealerProjectDTO vs) {
        return new WorkInProgressReportREPO() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getProjectTitle() {
                return vs.getProjectTitle();
            }

            @Override
            public String getDealerName() {
                return vs.getDealerName();
            }

            @Override
            public Long getNumberOfItems() {
                return vs.getNumberOfItems();
            }

            @Override
            public BigDecimal getInstalmentAmount() {
                return vs.getInstalmentAmount();
            }

            @Override
            public BigDecimal getTransferAmount() {
                return vs.getTransferAmount();
            }

            @Override
            public BigDecimal getOutstandingAmount() {
                return vs.getOutstandingAmount();
            }
        };
    }

    @Override
    public WIPByDealerProjectDTO toValue2(WorkInProgressReportREPO vs) {
        return WIPByDealerProjectDTO.builder()
            .id(vs.getId())
            .projectTitle(vs.getProjectTitle())
            .dealerName(vs.getDealerName())
            .numberOfItems(vs.getNumberOfItems())
            .instalmentAmount(vs.getInstalmentAmount())
            .transferAmount(vs.getTransferAmount())
            .outstandingAmount(vs.getOutstandingAmount())
            .build();
    }
}
