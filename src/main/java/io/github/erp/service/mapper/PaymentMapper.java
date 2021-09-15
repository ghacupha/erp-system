package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PaymentCategoryMapper.class,
        TaxRuleMapper.class,
        PaymentCalculationMapper.class,
        PaymentRequisitionMapper.class,
        PlaceholderMapper.class,
    }
)
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "id")
    @Mapping(target = "taxRule", source = "taxRule", qualifiedByName = "id")
    @Mapping(target = "paymentCalculation", source = "paymentCalculation", qualifiedByName = "id")
    @Mapping(target = "paymentRequisition", source = "paymentRequisition", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    PaymentDTO toDto(Payment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoId(Payment payment);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PaymentDTO> toDtoIdSet(Set<Payment> payment);

    @Mapping(target = "removePlaceholder", ignore = true)
    Payment toEntity(PaymentDTO paymentDTO);
}
