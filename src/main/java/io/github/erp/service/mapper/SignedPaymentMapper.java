package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.SignedPaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SignedPayment} and its DTO {@link SignedPaymentDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PaymentLabelMapper.class, DealerMapper.class, PaymentCategoryMapper.class, PlaceholderMapper.class }
)
public interface SignedPaymentMapper extends EntityMapper<SignedPaymentDTO, SignedPayment> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "categoryName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "signedPaymentGroup", source = "signedPaymentGroup", qualifiedByName = "id")
    SignedPaymentDTO toDto(SignedPayment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SignedPaymentDTO toDtoId(SignedPayment signedPayment);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    SignedPayment toEntity(SignedPaymentDTO signedPaymentDTO);
}
