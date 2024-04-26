package io.github.erp.internal.model.mapping;

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
import io.github.erp.domain.MonthlyPrepaymentOutstandingReportItemInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.MonthlyPrepaymentOutstandingReportItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class MonthlyPrepaymentOutstandingReportInternalMapper
    implements Mapping<MonthlyPrepaymentOutstandingReportItemInternal,
    MonthlyPrepaymentOutstandingReportItemDTO> {


    @Override
    public MonthlyPrepaymentOutstandingReportItemInternal toValue1(MonthlyPrepaymentOutstandingReportItemDTO vs) {
        return new MonthlyPrepaymentOutstandingReportItemInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public LocalDate getFiscalMonthEndDate() {

                return vs.getFiscalMonthEndDate();
            }

            @Override
            public BigDecimal getTotalPrepaymentAmount() {

                return vs.getTotalPrepaymentAmount();
            }

            @Override
            public BigDecimal getTotalAmortisedAmount() {

                return vs.getTotalAmortisedAmount();
            }

            @Override
            public BigDecimal getTotalOutstandingAmount() {

                return vs.getTotalOutstandingAmount();
            }

            @Override
            public Integer getNumberOfPrepaymentAccounts() {

                return vs.getNumberOfPrepaymentAccounts();
            }
        };
    }

    @Override
    public MonthlyPrepaymentOutstandingReportItemDTO toValue2(MonthlyPrepaymentOutstandingReportItemInternal vs) {

        MonthlyPrepaymentOutstandingReportItemDTO dto = new MonthlyPrepaymentOutstandingReportItemDTO();

        dto.setId(vs.getId());
        dto.setFiscalMonthEndDate(vs.getFiscalMonthEndDate());
        dto.setTotalPrepaymentAmount(vs.getTotalPrepaymentAmount());
        dto.setTotalAmortisedAmount(vs.getTotalAmortisedAmount());
        dto.setTotalOutstandingAmount(vs.getTotalOutstandingAmount());
        dto.setNumberOfPrepaymentAccounts(vs.getNumberOfPrepaymentAccounts());
        return dto;
    }
}
