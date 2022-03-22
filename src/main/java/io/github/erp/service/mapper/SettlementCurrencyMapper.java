package io.github.erp.service.mapper;

import io.github.erp.domain.SettlementCurrency;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SettlementCurrency} and its DTO {@link SettlementCurrencyDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface SettlementCurrencyMapper extends EntityMapper<SettlementCurrencyDTO, SettlementCurrency> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    SettlementCurrencyDTO toDto(SettlementCurrency s);

    @Mapping(target = "removePlaceholder", ignore = true)
    SettlementCurrency toEntity(SettlementCurrencyDTO settlementCurrencyDTO);

    @Named("iso4217CurrencyCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "iso4217CurrencyCode", source = "iso4217CurrencyCode")
    SettlementCurrencyDTO toDtoIso4217CurrencyCode(SettlementCurrency settlementCurrency);
}