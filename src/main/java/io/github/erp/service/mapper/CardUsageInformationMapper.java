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
import io.github.erp.domain.CardUsageInformation;
import io.github.erp.service.dto.CardUsageInformationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CardUsageInformation} and its DTO {@link CardUsageInformationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        InstitutionCodeMapper.class,
        CardTypesMapper.class,
        CardBrandTypeMapper.class,
        CardCategoryTypeMapper.class,
        BankTransactionTypeMapper.class,
        ChannelTypeMapper.class,
        CardStateMapper.class,
    }
)
public interface CardUsageInformationMapper extends EntityMapper<CardUsageInformationDTO, CardUsageInformation> {
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "institutionName")
    @Mapping(target = "cardType", source = "cardType", qualifiedByName = "cardType")
    @Mapping(target = "cardBrand", source = "cardBrand", qualifiedByName = "cardBrandType")
    @Mapping(target = "cardCategoryType", source = "cardCategoryType", qualifiedByName = "cardCategoryDescription")
    @Mapping(target = "transactionType", source = "transactionType", qualifiedByName = "transactionTypeDetails")
    @Mapping(target = "channelType", source = "channelType", qualifiedByName = "channelTypes")
    @Mapping(target = "cardState", source = "cardState", qualifiedByName = "cardStateFlagDetails")
    CardUsageInformationDTO toDto(CardUsageInformation s);
}
