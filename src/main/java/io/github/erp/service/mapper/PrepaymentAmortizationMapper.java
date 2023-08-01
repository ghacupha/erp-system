package io.github.erp.service.mapper;

import io.github.erp.domain.PrepaymentAmortization;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentAmortization} and its DTO {@link PrepaymentAmortizationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PrepaymentAccountMapper.class, SettlementCurrencyMapper.class, TransactionAccountMapper.class, PlaceholderMapper.class }
)
public interface PrepaymentAmortizationMapper extends EntityMapper<PrepaymentAmortizationDTO, PrepaymentAmortization> {
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "debitAccount", source = "debitAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "creditAccount", source = "creditAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PrepaymentAmortizationDTO toDto(PrepaymentAmortization s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentAmortization toEntity(PrepaymentAmortizationDTO prepaymentAmortizationDTO);
}
