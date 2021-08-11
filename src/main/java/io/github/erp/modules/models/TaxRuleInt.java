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

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This object holds the applicable tax regulation rates for a given payment
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxRuleInt implements Serializable {

    private BigDecimal telcoExciseDuty;

    private BigDecimal valueAddedTax;

    private BigDecimal withholdingVAT;

    private BigDecimal withholdingTaxConsultancy;

    private BigDecimal withholdingTaxRent;

    private BigDecimal cateringLevy;

    private BigDecimal serviceCharge;

    private BigDecimal withholdingTaxImportedService;
}
