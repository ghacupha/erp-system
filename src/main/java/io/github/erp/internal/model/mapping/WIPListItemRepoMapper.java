package io.github.erp.internal.model.mapping;

import io.github.erp.domain.WIPListItemREPO;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.WIPListItemDTO;
import org.springframework.stereotype.Component;

@Component("wipListItemRepoMapper")
public class WIPListItemRepoMapper implements Mapping<WIPListItemREPO, WIPListItemDTO> {

    @Override
    public WIPListItemREPO toValue1(WIPListItemDTO vs) {

        return WIPListItemREPO.builder()
            .id(vs.getId())
            .sequenceNumber(vs.getSequenceNumber())
            .particulars(vs.getParticulars())
            .instalmentDate(vs.getInstalmentDate())
            .instalmentAmount(vs.getInstalmentAmount())
            .settlementCurrency(vs.getSettlementCurrency())
            .settlementTransaction(vs.getSettlementTransaction())
            .settlementTransactionDate(vs.getSettlementTransactionDate())
            .dealerName(vs.getDealerName())
            .workProject(vs.getWorkProject())
            .build();
    }

    @Override
    public WIPListItemDTO toValue2(WIPListItemREPO vs) {
        WIPListItemDTO dto = new WIPListItemDTO();
        dto.setId(vs.getId());
        dto.setSequenceNumber(vs.getSequenceNumber());
        dto.setParticulars(vs.getParticulars());
        dto.setInstalmentDate(vs.getInstalmentDate());
        dto.setInstalmentAmount(vs.getInstalmentAmount());
        dto.setSettlementCurrency(vs.getSettlementCurrency());
        dto.setSettlementTransaction(vs.getSettlementTransaction());
        dto.setSettlementTransactionDate(vs.getSettlementTransactionDate());
        dto.setDealerName(vs.getDealerName());
        dto.setWorkProject(vs.getWorkProject());

        return dto;
    }
}
