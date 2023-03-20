package io.github.erp.service.mapper;

import io.github.erp.domain.TransactionAccount;
import io.github.erp.service.dto.TransactionAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionAccount} and its DTO {@link TransactionAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface TransactionAccountMapper extends EntityMapper<TransactionAccountDTO, TransactionAccount> {
    @Mapping(target = "parentAccount", source = "parentAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    TransactionAccountDTO toDto(TransactionAccount s);

    @Mapping(target = "removePlaceholder", ignore = true)
    TransactionAccount toEntity(TransactionAccountDTO transactionAccountDTO);

    @Named("accountNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountNumber", source = "accountNumber")
    TransactionAccountDTO toDtoAccountNumber(TransactionAccount transactionAccount);

    @Named("accountName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountName", source = "accountName")
    TransactionAccountDTO toDtoAccountName(TransactionAccount transactionAccount);
}
