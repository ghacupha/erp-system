package io.github.erp.service.mapper;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.PaymentCalculation;
import io.github.erp.service.dto.PaymentCalculationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCalculation} and its DTO {@link PaymentCalculationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentLabelMapper.class, PaymentCategoryMapper.class, PlaceholderMapper.class })
public interface PaymentCalculationMapper extends EntityMapper<PaymentCalculationDTO, PaymentCalculation> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PaymentCalculationDTO toDto(PaymentCalculation s);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    PaymentCalculation toEntity(PaymentCalculationDTO paymentCalculationDTO);
}
