package io.github.erp.service.mapper;

import io.github.erp.domain.ServiceOutlet;
import io.github.erp.service.dto.ServiceOutletDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceOutlet} and its DTO {@link ServiceOutletDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceholderMapper.class, BankBranchCodeMapper.class, OutletTypeMapper.class, OutletStatusMapper.class, CountyCodeMapper.class }
)
public interface ServiceOutletMapper extends EntityMapper<ServiceOutletDTO, ServiceOutlet> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "branchCode")
    @Mapping(target = "outletType", source = "outletType", qualifiedByName = "outletType")
    @Mapping(target = "outletStatus", source = "outletStatus", qualifiedByName = "branchStatusType")
    @Mapping(target = "countyName", source = "countyName", qualifiedByName = "countyName")
    @Mapping(target = "subCountyName", source = "subCountyName", qualifiedByName = "subCountyName")
    ServiceOutletDTO toDto(ServiceOutlet s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ServiceOutlet toEntity(ServiceOutletDTO serviceOutletDTO);
}
