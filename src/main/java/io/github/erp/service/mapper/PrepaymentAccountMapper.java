package io.github.erp.service.mapper;

import io.github.erp.domain.PrepaymentAccount;
import io.github.erp.service.dto.PrepaymentAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentAccount} and its DTO {@link PrepaymentAccountDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        SettlementCurrencyMapper.class,
        SettlementMapper.class,
        ServiceOutletMapper.class,
        DealerMapper.class,
        TransactionAccountMapper.class,
        PlaceholderMapper.class,
        UniversallyUniqueMappingMapper.class,
        PrepaymentMappingMapper.class,
    }
)
public interface PrepaymentAccountMapper extends EntityMapper<PrepaymentAccountDTO, PrepaymentAccount> {
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "prepaymentTransaction", source = "prepaymentTransaction", qualifiedByName = "paymentNumber")
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "debitAccount", source = "debitAccount", qualifiedByName = "accountName")
    @Mapping(target = "transferAccount", source = "transferAccount", qualifiedByName = "accountName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "generalParameters", source = "generalParameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "prepaymentParameters", source = "prepaymentParameters", qualifiedByName = "parameterSet")
    PrepaymentAccountDTO toDto(PrepaymentAccount s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeGeneralParameters", ignore = true)
    @Mapping(target = "removePrepaymentParameters", ignore = true)
    PrepaymentAccount toEntity(PrepaymentAccountDTO prepaymentAccountDTO);

    @Named("catalogueNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "catalogueNumber", source = "catalogueNumber")
    PrepaymentAccountDTO toDtoCatalogueNumber(PrepaymentAccount prepaymentAccount);
}
