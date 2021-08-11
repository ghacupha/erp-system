package io.github.erp.modules.models;

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
