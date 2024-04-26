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
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SettlementCurrency} and its DTO {@link SettlementCurrencyDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface SettlementCurrencyMapper extends EntityMapper<SettlementCurrencyDTO, SettlementCurrency> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    SettlementCurrencyDTO toDto(SettlementCurrency s);

    @Mapping(target = "removePlaceholder", ignore = true)
    SettlementCurrency toEntity(SettlementCurrencyDTO settlementCurrencyDTO);

    @Named("iso4217CurrencyCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "iso4217CurrencyCode", source = "iso4217CurrencyCode")
    SettlementCurrencyDTO toDtoIso4217CurrencyCode(SettlementCurrency settlementCurrency);
}
