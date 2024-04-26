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
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmortizationPostingReportMapper implements Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> {

    @Override
    public AmortizationPostingReportInternal toValue1(AmortizationPostingReportDTO vs) {
        return new AmortizationPostingReportInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getCatalogueNumber() {
                return vs.getCatalogueNumber();
            }

            @Override
            public String getDebitAccount() {
                return vs.getDebitAccount();
            }

            @Override
            public String getCreditAccount() {
                return vs.getCreditAccount();
            }

            @Override
            public String getDescription() {
                return vs.getDescription();
            }

            @Override
            public BigDecimal getAmortizationAmount() {
                return vs.getAmortizationAmount();
            }
        };
    }

    @Override
    public AmortizationPostingReportDTO toValue2(AmortizationPostingReportInternal vs) {
        AmortizationPostingReportDTO dto = new AmortizationPostingReportDTO();
        dto.setId(vs.getId());
        dto.setCatalogueNumber(vs.getCatalogueNumber());
        dto.setDebitAccount(vs.getDebitAccount());
        dto.setCreditAccount(vs.getCreditAccount());
        dto.setDescription(vs.getDescription());
        dto.setAmortizationAmount(vs.getAmortizationAmount());

        return dto;
    }
}
