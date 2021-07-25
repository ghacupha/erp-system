package io.github.erp.modules;

import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

public class PaymentComputationUtils {

    /**
     * This instance implies the requisition amount is always equal to the total expense
     *
     * @param paymentRequisition
     * @param currencyCode
     * @return
     */
    static MonetaryAmount requisitionEquivalentExpense(PaymentRequisitionInt paymentRequisition, String currencyCode) {

        return Money.of(paymentRequisition.getInvoicedAmount().getNumber().numberValue(BigDecimal.class), currencyUnit(currencyCode));
    }

    static BigDecimal onePlusVAT(TaxRuleInt taxRule) {
        return taxRule.getValueAddedTax().add(BigDecimal.ONE);
    }

    static CurrencyUnit currencyUnit(String currencyCode) {

        return currencyUnit(currencyCode, Locale.ENGLISH);
    }

    /**
     * Return a currency-unit instance for a given string currency-code
     * @param currencyCode
     * @return
     */
    static CurrencyUnit currencyUnit(String currencyCode, Locale locale) {

        return CurrencyUnitBuilder.of(currencyCode, "Provided_Currency_Code").build(true, locale);
    }

    static BigDecimal queryNumerical(MonetaryAmount monetaryAmount) {

        return queryNumerical(monetaryAmount, 2);
    }

    /**
     * Query numerical amount in a monetary amount
     * @param monetaryAmount
     * @return
     */
    static BigDecimal queryNumerical(MonetaryAmount monetaryAmount, final int fractionDigits) {
        Objects.requireNonNull(monetaryAmount, "Monetary-Amount required");
        BigDecimal number = monetaryAmount.getNumber().numberValue(BigDecimal.class);
        number = number.setScale(fractionDigits, RoundingMode.HALF_EVEN);
        return number.movePointRight(number.scale());
    }
}
