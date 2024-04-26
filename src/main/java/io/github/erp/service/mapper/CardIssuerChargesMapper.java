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
import io.github.erp.domain.CardIssuerCharges;
import io.github.erp.service.dto.CardIssuerChargesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CardIssuerCharges} and its DTO {@link CardIssuerChargesDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        InstitutionCodeMapper.class,
        CardCategoryTypeMapper.class,
        CardTypesMapper.class,
        CardBrandTypeMapper.class,
        CardClassTypeMapper.class,
        CardChargesMapper.class,
    }
)
public interface CardIssuerChargesMapper extends EntityMapper<CardIssuerChargesDTO, CardIssuerCharges> {
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "institutionName")
    @Mapping(target = "cardCategory", source = "cardCategory", qualifiedByName = "cardCategoryDescription")
    @Mapping(target = "cardType", source = "cardType", qualifiedByName = "cardType")
    @Mapping(target = "cardBrand", source = "cardBrand", qualifiedByName = "cardBrandType")
    @Mapping(target = "cardClass", source = "cardClass", qualifiedByName = "cardClassType")
    @Mapping(target = "cardChargeType", source = "cardChargeType", qualifiedByName = "cardChargeTypeName")
    CardIssuerChargesDTO toDto(CardIssuerCharges s);
}
