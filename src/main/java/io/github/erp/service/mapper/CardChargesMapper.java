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
import io.github.erp.domain.CardCharges;
import io.github.erp.service.dto.CardChargesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CardCharges} and its DTO {@link CardChargesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CardChargesMapper extends EntityMapper<CardChargesDTO, CardCharges> {
    @Named("cardChargeTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cardChargeTypeName", source = "cardChargeTypeName")
    CardChargesDTO toDtoCardChargeTypeName(CardCharges cardCharges);
}
