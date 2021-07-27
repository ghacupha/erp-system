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
     * This instance implies the requisition amount is always equal to the total expense. This applies to
     * most computation types except those that include reverse VAT
     */
    static MonetaryAmount requisitionEquivalentExpense(PaymentRequisitionInt paymentRequisition, String currencyCode) {

        return Money.of(paymentRequisition.getInvoicedAmount().getNumber().numberValue(BigDecimal.class), currencyUnit(currencyCode));
    }

    /**
     * Given the prevailing tax-rule it returns the value of 1 + vat-rate
     */
    static BigDecimal onePlusVAT(TaxRuleInt taxRule) {
        return taxRule.getValueAddedTax().add(BigDecimal.ONE);
    }

    /**
     * Creates currency-unit object from string description code of a
     * currency using the English locale
     */
    static CurrencyUnit currencyUnit(String currencyCode) {
        return currencyUnit(currencyCode, Locale.ENGLISH);
    }

    /**
     * Return a currency-unit instance for a given string currency-code
     */
    static CurrencyUnit currencyUnit(String currencyCode, Locale locale) {

        return CurrencyUnitBuilder.of(currencyCode, "Provided_Currency_Code").build(true, locale);
    }

    /**
     * Query numerical value for 2 decimal places
     */
    static BigDecimal queryNumerical(MonetaryAmount monetaryAmount) {

        return queryNumerical(monetaryAmount, 2);
    }

    /**
     * Query numerical amount in a monetary amount
     */
    static BigDecimal queryNumerical(MonetaryAmount monetaryAmount, final int fractionDigits) {
        Objects.requireNonNull(monetaryAmount, "Monetary-Amount required");
        BigDecimal number = monetaryAmount.getNumber().numberValue(BigDecimal.class);
        return number.setScale(fractionDigits, RoundingMode.HALF_EVEN);
    }

    /**
     * takes the monetary-amount provided and rounds the value of decimal places to zero
     * rounding up if any modulus exists
     */
    static MonetaryAmount roundToZeroDP(MonetaryAmount amount) {
        return Money.of(queryNumerical(amount).setScale(0, RoundingMode.UP), amount.getCurrency());
    }
}
