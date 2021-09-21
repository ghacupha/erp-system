package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.DealerDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dealer} and its DTO {@link DealerDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentLabelMapper.class, PlaceholderMapper.class })
public interface DealerMapper extends EntityMapper<DealerDTO, Dealer> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "dealerGroup", source = "dealerGroup", qualifiedByName = "dealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    DealerDTO toDto(Dealer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DealerDTO toDtoId(Dealer dealer);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    Dealer toEntity(DealerDTO dealerDTO);

    @Named("dealerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dealerName", source = "dealerName")
    DealerDTO toDtoDealerName(Dealer dealer);

    @Named("dealerNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dealerName", source = "dealerName")
    Set<DealerDTO> toDtoDealerNameSet(Set<Dealer> dealer);
}
