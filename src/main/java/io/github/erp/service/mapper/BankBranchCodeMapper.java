package io.github.erp.service.mapper;

import io.github.erp.domain.BankBranchCode;
import io.github.erp.service.dto.BankBranchCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankBranchCode} and its DTO {@link BankBranchCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface BankBranchCodeMapper extends EntityMapper<BankBranchCodeDTO, BankBranchCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    BankBranchCodeDTO toDto(BankBranchCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    BankBranchCode toEntity(BankBranchCodeDTO bankBranchCodeDTO);

    @Named("branchCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "branchCode", source = "branchCode")
    BankBranchCodeDTO toDtoBranchCode(BankBranchCode bankBranchCode);
}
