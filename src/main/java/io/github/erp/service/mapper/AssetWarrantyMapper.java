package io.github.erp.service.mapper;

import io.github.erp.domain.AssetWarranty;
import io.github.erp.service.dto.AssetWarrantyDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetWarranty} and its DTO {@link AssetWarrantyDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class, DealerMapper.class, BusinessDocumentMapper.class }
)
public interface AssetWarrantyMapper extends EntityMapper<AssetWarrantyDTO, AssetWarranty> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "warrantyAttachments", source = "warrantyAttachments", qualifiedByName = "documentTitleSet")
    AssetWarrantyDTO toDto(AssetWarranty s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    @Mapping(target = "removeWarrantyAttachment", ignore = true)
    AssetWarranty toEntity(AssetWarrantyDTO assetWarrantyDTO);

    @Named("descriptionSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    Set<AssetWarrantyDTO> toDtoDescriptionSet(Set<AssetWarranty> assetWarranty);
}
