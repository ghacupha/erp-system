package io.github.erp.modules.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;

/**
 * This object the base financial information that we need to know about a
 * requisition
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequisitionInt {

    /**
     * This is the amount that the biller is requesting for
     */
    private MonetaryAmount invoicedAmount;

    /**
     * This is the disbursement cost that the biller has used in order to
     * provide their service. This is useful to track because it is not
     * included as a factor for the assessment of Value added tax
     */
    private MonetaryAmount disbursementCost;

    /**
     * This is the amount from the invoice on which the value added tax is assessed
     */
    private MonetaryAmount vatableAmount;
}
