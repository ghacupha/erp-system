package io.github.erp.service.mapper;

import io.github.erp.domain.PaymentLabel;
import io.github.erp.service.dto.PaymentLabelDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentLabel} and its DTO {@link PaymentLabelDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface PaymentLabelMapper extends EntityMapper<PaymentLabelDTO, PaymentLabel> {
    @Mapping(target = "containingPaymentLabel", source = "containingPaymentLabel", qualifiedByName = "description")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PaymentLabelDTO toDto(PaymentLabel s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PaymentLabel toEntity(PaymentLabelDTO paymentLabelDTO);

    @Named("descriptionSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    Set<PaymentLabelDTO> toDtoDescriptionSet(Set<PaymentLabel> paymentLabel);

    @Named("description")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    PaymentLabelDTO toDtoDescription(PaymentLabel paymentLabel);
}
