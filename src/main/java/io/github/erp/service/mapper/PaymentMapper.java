package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PaymentLabelMapper.class,
        DealerMapper.class,
        PaymentCategoryMapper.class,
        TaxRuleMapper.class,
        PaymentCalculationMapper.class,
        PlaceholderMapper.class,
    }
)
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "categoryName")
    @Mapping(target = "taxRule", source = "taxRule", qualifiedByName = "id")
    @Mapping(target = "paymentCalculation", source = "paymentCalculation", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentGroup", source = "paymentGroup", qualifiedByName = "id")
    PaymentDTO toDto(Payment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoId(Payment payment);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    Payment toEntity(PaymentDTO paymentDTO);
}
