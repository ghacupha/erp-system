package io.github.erp.modules;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentComputationTests {

    protected MonetaryAmount INVOICE_AMOUNT = Money.of(40000,BASE_SYSTEM_CURRENCY_CODE);
    protected MonetaryAmount DISBURSEMENT_AMOUNT = Money.of(7000,BASE_SYSTEM_CURRENCY_CODE);
    protected MonetaryAmount VATABLE_AMOUNT = Money.of(33000,BASE_SYSTEM_CURRENCY_CODE);

    protected PaymentRequisitionInt requisition = PaymentRequisitionInt.builder()
        .invoicedAmount(INVOICE_AMOUNT)
        .disbursementCost(DISBURSEMENT_AMOUNT)
        .vatableAmount(VATABLE_AMOUNT)
        .build();

    protected MonetaryAmount INVOICE_AMOUNT$ = Money.of(40000,"USD");
    protected MonetaryAmount DISBURSEMENT_AMOUNT$ = Money.of(7000,"USD");
    protected MonetaryAmount VATABLE_AMOUNT$ = Money.of(33000,"USD");

    protected PaymentRequisitionInt requisition$ = PaymentRequisitionInt.builder()
        .invoicedAmount(INVOICE_AMOUNT$)
        .disbursementCost(DISBURSEMENT_AMOUNT$)
        .vatableAmount(VATABLE_AMOUNT$)
        .build();

    protected BigDecimal TELCO_EXCISE_DUTY = BigDecimal.valueOf(0.15);
    protected BigDecimal VALUE_ADDED_TAX = BigDecimal.valueOf(0.16);
    protected BigDecimal WITHHOLDING_VAT = BigDecimal.valueOf(0.02);
    protected BigDecimal WITHHOLDING_TAX_ON_CONSULTANCY = BigDecimal.valueOf(0.05);
    protected BigDecimal WITHHOLDING_TAX_ON_RENT = BigDecimal.valueOf(0.1);
    protected BigDecimal CATERING_LEVY = BigDecimal.valueOf(0.02);
    protected BigDecimal SERVICE_CHARGE = BigDecimal.valueOf(0.07);
    protected BigDecimal WITHHOLDING_TX_IMPORTED_SERVICE = BigDecimal.valueOf(0.2);

    protected TaxRuleInt taxRule = TaxRuleInt.builder()
        .telcoExciseDuty(TELCO_EXCISE_DUTY)
        .valueAddedTax(VALUE_ADDED_TAX)
        .withholdingVAT(WITHHOLDING_VAT)
        .withholdingTaxConsultancy(WITHHOLDING_TAX_ON_CONSULTANCY)
        .withholdingTaxRent(WITHHOLDING_TAX_ON_RENT)
        .cateringLevy(CATERING_LEVY)
        .serviceCharge(SERVICE_CHARGE)
        .withholdingTaxImportedService(WITHHOLDING_TX_IMPORTED_SERVICE)
        .build();

    /**
     * Just running that JSR354 tests are going to be a problem due to differences in
     * object reference. All I needed to test is the amount and currency. That's why I
     * had to do this
     */
    void testEquality(MonetaryAmount m1, MonetaryAmount m2) {

        assertThat(m1.getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.HALF_EVEN))
            .isEqualTo(m2.getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.HALF_EVEN));
        assertThat(m1.getCurrency().getCurrencyCode())
            .isEqualTo(m1.getCurrency().getCurrencyCode());
    }
}
