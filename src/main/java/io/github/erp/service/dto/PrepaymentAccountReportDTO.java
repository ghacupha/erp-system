package io.github.erp.service.dto;

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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentAccountReport} entity.
 */
public class PrepaymentAccountReportDTO implements Serializable {

    private Long id;

    private String prepaymentAccount;

    private BigDecimal prepaymentAmount;

    private BigDecimal amortisedAmount;

    private BigDecimal outstandingAmount;

    private Integer numberOfPrepaymentAccounts;

    private Integer numberOfAmortisedItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(String prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public BigDecimal getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public BigDecimal getAmortisedAmount() {
        return amortisedAmount;
    }

    public void setAmortisedAmount(BigDecimal amortisedAmount) {
        this.amortisedAmount = amortisedAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public Integer getNumberOfPrepaymentAccounts() {
        return numberOfPrepaymentAccounts;
    }

    public void setNumberOfPrepaymentAccounts(Integer numberOfPrepaymentAccounts) {
        this.numberOfPrepaymentAccounts = numberOfPrepaymentAccounts;
    }

    public Integer getNumberOfAmortisedItems() {
        return numberOfAmortisedItems;
    }

    public void setNumberOfAmortisedItems(Integer numberOfAmortisedItems) {
        this.numberOfAmortisedItems = numberOfAmortisedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentAccountReportDTO)) {
            return false;
        }

        PrepaymentAccountReportDTO prepaymentAccountReportDTO = (PrepaymentAccountReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentAccountReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAccountReportDTO{" +
            "id=" + getId() +
            ", prepaymentAccount='" + getPrepaymentAccount() + "'" +
            ", prepaymentAmount=" + getPrepaymentAmount() +
            ", amortisedAmount=" + getAmortisedAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            ", numberOfPrepaymentAccounts=" + getNumberOfPrepaymentAccounts() +
            ", numberOfAmortisedItems=" + getNumberOfAmortisedItems() +
            "}";
    }
}
