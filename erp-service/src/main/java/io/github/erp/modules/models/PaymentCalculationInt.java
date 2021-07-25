package io.github.erp.modules.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import java.io.Serializable;

/**
 * Holds the results of a payment calculation
 * Created for convenience to stop invalidating the DTO tests for the payment-calculation-dto
 * who main purpose is interaction with the external API.
 * Internally we use this one and do mapping when external interactions are needed
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCalculationInt implements Serializable {

    private MonetaryAmount paymentExpense;

    private MonetaryAmount withholdingVAT;

    private MonetaryAmount withholdingTax;

    private MonetaryAmount paymentAmount;
}
