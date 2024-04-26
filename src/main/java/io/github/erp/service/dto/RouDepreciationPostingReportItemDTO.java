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
 * A DTO for the {@link io.github.erp.domain.RouDepreciationPostingReportItem} entity.
 */
public class RouDepreciationPostingReportItemDTO implements Serializable {

    private Long id;

    private String leaseContractNumber;

    private String leaseDescription;

    private String fiscalMonthCode;

    private String accountForCredit;

    private String accountForDebit;

    private BigDecimal depreciationAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public String getLeaseDescription() {
        return leaseDescription;
    }

    public void setLeaseDescription(String leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public String getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public String getAccountForCredit() {
        return accountForCredit;
    }

    public void setAccountForCredit(String accountForCredit) {
        this.accountForCredit = accountForCredit;
    }

    public String getAccountForDebit() {
        return accountForDebit;
    }

    public void setAccountForDebit(String accountForDebit) {
        this.accountForDebit = accountForDebit;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationPostingReportItemDTO)) {
            return false;
        }

        RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO = (RouDepreciationPostingReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouDepreciationPostingReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationPostingReportItemDTO{" +
            "id=" + getId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", leaseDescription='" + getLeaseDescription() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            ", accountForCredit='" + getAccountForCredit() + "'" +
            ", accountForDebit='" + getAccountForDebit() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            "}";
    }
}
