package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.TaxRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxRule} and its DTO {@link TaxRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaxRuleMapper extends EntityMapper<TaxRuleDTO, TaxRule> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaxRuleDTO toDtoId(TaxRule taxRule);
}
