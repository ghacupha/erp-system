package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.DealerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dealer} and its DTO {@link DealerDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface DealerMapper extends EntityMapper<DealerDTO, Dealer> {


    @Mapping(target = "removePayment", ignore = true)

    default Dealer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dealer dealer = new Dealer();
        dealer.setId(id);
        return dealer;
    }
}
