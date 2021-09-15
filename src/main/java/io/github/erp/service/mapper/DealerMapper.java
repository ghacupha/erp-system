package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.DealerDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dealer} and its DTO {@link DealerDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentMapper.class, PlaceholderMapper.class })
public interface DealerMapper extends EntityMapper<DealerDTO, Dealer> {
    @Mapping(target = "payments", source = "payments", qualifiedByName = "idSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    DealerDTO toDto(Dealer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DealerDTO toDtoId(Dealer dealer);

    @Mapping(target = "removePayment", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    Dealer toEntity(DealerDTO dealerDTO);
}
