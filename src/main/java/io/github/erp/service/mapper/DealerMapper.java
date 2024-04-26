package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.Dealer;
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
