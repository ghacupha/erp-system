package io.github.erp.service.mapper;

import io.github.erp.domain.AmortizationRecurrence;
import io.github.erp.service.dto.AmortizationRecurrenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AmortizationRecurrence} and its DTO {@link AmortizationRecurrenceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        PrepaymentMappingMapper.class,
        UniversallyUniqueMappingMapper.class,
        DepreciationMethodMapper.class,
        PrepaymentAccountMapper.class,
    }
)
public interface AmortizationRecurrenceMapper extends EntityMapper<AmortizationRecurrenceDTO, AmortizationRecurrence> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "parameterSet")
    @Mapping(target = "applicationParameters", source = "applicationParameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "depreciationMethod", source = "depreciationMethod", qualifiedByName = "description")
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    AmortizationRecurrenceDTO toDto(AmortizationRecurrence s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    @Mapping(target = "removeApplicationParameters", ignore = true)
    AmortizationRecurrence toEntity(AmortizationRecurrenceDTO amortizationRecurrenceDTO);

    @Named("particulars")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "particulars", source = "particulars")
    AmortizationRecurrenceDTO toDtoParticulars(AmortizationRecurrence amortizationRecurrence);
}
