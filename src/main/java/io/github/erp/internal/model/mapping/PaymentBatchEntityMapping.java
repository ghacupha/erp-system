package io.github.erp.internal.model.mapping;

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
import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.internal.framework.MapUtils;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentBEO;
import io.github.erp.internal.model.PaymentEVM;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.erp.internal.model.mapping.CurrencyMappingUtility.enumToString;
import static io.github.erp.internal.model.mapping.CurrencyMappingUtility.stringToEnum;

@Mapper(componentModel = "spring")
public interface PaymentBatchEntityMapping extends Mapping<PaymentEVM, PaymentBEO> {

    @org.mapstruct.Mapping(target = "paymentDate", source = "paymentDate")
    default LocalDate dateStringToLocalDate(String dateString) {
        return MapUtils.dateStringToLocalDate(dateString);
    }

    @org.mapstruct.Mapping(target = "paymentDate", source = "paymentDate")
    default String localDateToDateString(LocalDate localDateValue) {
        return MapUtils.localDateToDateString(localDateValue);
    }

    @org.mapstruct.Mapping(target = "invoicedAmount", source = "invoicedAmount")
    @org.mapstruct.Mapping(target = "paymentAmount", source = "paymentAmount")
    default BigDecimal toBigDecimalValue(Double doubleValue) {
        return MapUtils.doubleToBigDecimal(doubleValue);
    }

    @org.mapstruct.Mapping(target = "invoicedAmount", source = "invoicedAmount")
    @org.mapstruct.Mapping(target = "paymentAmount", source = "paymentAmount")
    default Double toDoubleValue(BigDecimal bigDecimalValue) {
        return MapUtils.bigDecimalToDouble(bigDecimalValue);
    }

    @org.mapstruct.Mapping(target = "settlementCurrency", source = "settlementCurrency")
    default CurrencyTypes currencyMapping(String currencyCode) {
        return stringToEnum(currencyCode);
    }

    @org.mapstruct.Mapping(target = "settlementCurrency", source = "settlementCurrency")
    default String currencyMapping(CurrencyTypes currencyCode) {
        return enumToString(currencyCode);
    }
}
