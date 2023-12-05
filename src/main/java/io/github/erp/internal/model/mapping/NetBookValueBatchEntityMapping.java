package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
import io.github.erp.domain.enumeration.DepreciationRegime;
import io.github.erp.internal.framework.MapUtils;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.FixedAssetNetBookValueBEO;
import io.github.erp.internal.model.FixedAssetNetBookValueEVM;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface NetBookValueBatchEntityMapping extends Mapping<FixedAssetNetBookValueEVM, FixedAssetNetBookValueBEO> {

    @org.mapstruct.Mapping(target = "netBookValueDate", source = "netBookValueDate")
    default LocalDate dateStringToLocalDate(String dateString) {
        return MapUtils.dateStringToLocalDate(dateString);
    }

    @org.mapstruct.Mapping(target = "netBookValueDate", source = "netBookValueDate")
    default String localDateToDateString(LocalDate localDateValue) {
        return MapUtils.localDateToDateString(localDateValue);
    }

    @org.mapstruct.Mapping(target = "netBookValue", source = "netBookValue")
    default BigDecimal toBigDecimalValue(Double doubleValue) {
        return MapUtils.doubleToBigDecimal(doubleValue);
    }

    @org.mapstruct.Mapping(target = "netBookValue", source = "netBookValue")
    default Double toDoubleValue(BigDecimal bigDecimalValue) {
        return MapUtils.bigDecimalToDouble(bigDecimalValue);
    }

    @org.mapstruct.Mapping(target = "depreciationRegime", source = "depreciationRegime")
    default DepreciationRegime toEnumeratedValue(String depreciationRegimeDesignation) {
        return DepreciationRegimeMapUtils.depreciationRegime(depreciationRegimeDesignation);
    }

    @org.mapstruct.Mapping(target = "depreciationRegime", source = "depreciationRegime")
    default String toStringEnumeratedValue(DepreciationRegime depreciationRegimeEnum) {
        return DepreciationRegimeMapUtils.depreciationRegime(depreciationRegimeEnum);
    }
}
