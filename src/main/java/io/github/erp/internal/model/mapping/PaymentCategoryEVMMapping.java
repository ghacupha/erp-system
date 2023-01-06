package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0
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
import io.github.erp.domain.enumeration.CategoryTypes;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentCategoryEVM;
import io.github.erp.service.dto.PaymentCategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentCategoryEVMMapping extends Mapping<PaymentCategoryEVM, PaymentCategoryDTO> {

    @org.mapstruct.Mapping(target = "categoryType", source = "categoryType")
    default CategoryTypes categoryStringToCategoryType(String categoryString) {
        return  PaymentCategoryMappingUtility.categoryStringToCategoryType(categoryString);
    }

    @org.mapstruct.Mapping(target = "categoryType", source = "categoryType")
    default String categoryTypeToCategoryString(CategoryTypes categoryType) {
        return PaymentCategoryMappingUtility.categoryTypeToCategoryString(categoryType);
    }
}
