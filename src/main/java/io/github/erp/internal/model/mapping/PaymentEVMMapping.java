package io.github.erp.internal.model.mapping;

import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.internal.framework.MapUtils;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentEVM;
import io.github.erp.service.dto.PaymentDTO;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.erp.internal.model.mapping.CurrencyMappingUtility.enumToString;
import static io.github.erp.internal.model.mapping.CurrencyMappingUtility.stringToEnum;

@Mapper(componentModel = "spring")
public interface PaymentEVMMapping extends Mapping<PaymentEVM, PaymentDTO> {

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
