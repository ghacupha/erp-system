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
