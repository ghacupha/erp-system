package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.TaxRuleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxRule} and its DTO {@link TaxRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaxRuleMapper extends EntityMapper<TaxRuleDTO, TaxRule> {


    @Mapping(target = "payment", ignore = true)
    TaxRule toEntity(TaxRuleDTO taxRuleDTO);

    default TaxRule fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaxRule taxRule = new TaxRule();
        taxRule.setId(id);
        return taxRule;
    }
}
