package io.github.erp.domain;

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
import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface DepreciationEntryInternal {

    Long getId();

    String getAssetRegistrationDetails();

    String getPostedAt();

    String getAssetNumber();

    String getServiceOutlet();

    String getAssetCategory();

    String getDepreciationMethod();

    String getDepreciationPeriod();

    String getFiscalMonthCode();

    BigDecimal getAssetRegistrationCost();

    BigDecimal getDepreciationAmount();

    Long getElapsedMonths();

    Long getPriorMonths();

    BigDecimal getUsefulLifeYears();

    BigDecimal getPreviousNBV();

    BigDecimal getNetBookValue();

    LocalDate getDepreciationPeriodStartDate();

    LocalDate getDepreciationPeriodEndDate();

    LocalDate getCapitalizationDate();
}
