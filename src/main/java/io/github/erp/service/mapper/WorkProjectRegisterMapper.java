package io.github.erp.service.mapper;

import io.github.erp.domain.WorkProjectRegister;
import io.github.erp.service.dto.WorkProjectRegisterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkProjectRegister} and its DTO {@link WorkProjectRegisterDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DealerMapper.class, SettlementCurrencyMapper.class, PlaceholderMapper.class, BusinessDocumentMapper.class }
)
public interface WorkProjectRegisterMapper extends EntityMapper<WorkProjectRegisterDTO, WorkProjectRegister> {
    @Mapping(target = "dealers", source = "dealers", qualifiedByName = "dealerNameSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    WorkProjectRegisterDTO toDto(WorkProjectRegister s);

    @Mapping(target = "removeDealers", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    WorkProjectRegister toEntity(WorkProjectRegisterDTO workProjectRegisterDTO);

    @Named("catalogueNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "catalogueNumber", source = "catalogueNumber")
    WorkProjectRegisterDTO toDtoCatalogueNumber(WorkProjectRegister workProjectRegister);
}
