package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
