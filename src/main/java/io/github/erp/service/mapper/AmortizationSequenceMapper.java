package io.github.erp.service.mapper;

import io.github.erp.domain.AmortizationSequence;
import io.github.erp.service.dto.AmortizationSequenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AmortizationSequence} and its DTO {@link AmortizationSequenceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PrepaymentAccountMapper.class,
        AmortizationRecurrenceMapper.class,
        PlaceholderMapper.class,
        PrepaymentMappingMapper.class,
        UniversallyUniqueMappingMapper.class,
    }
)
public interface AmortizationSequenceMapper extends EntityMapper<AmortizationSequenceDTO, AmortizationSequence> {
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    @Mapping(target = "amortizationRecurrence", source = "amortizationRecurrence", qualifiedByName = "particulars")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "prepaymentMappings", source = "prepaymentMappings", qualifiedByName = "parameterSet")
    @Mapping(target = "applicationParameters", source = "applicationParameters", qualifiedByName = "mappedValueSet")
    AmortizationSequenceDTO toDto(AmortizationSequence s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePrepaymentMapping", ignore = true)
    @Mapping(target = "removeApplicationParameters", ignore = true)
    AmortizationSequence toEntity(AmortizationSequenceDTO amortizationSequenceDTO);
}
