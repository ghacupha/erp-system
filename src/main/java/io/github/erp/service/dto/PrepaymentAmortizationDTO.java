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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentAmortization} entity.
 */
public class PrepaymentAmortizationDTO implements Serializable {

    private Long id;

    private String description;

    private LocalDate prepaymentPeriod;

    private BigDecimal prepaymentAmount;

    private Boolean inactive;

    private PrepaymentAccountDTO prepaymentAccount;

    private SettlementCurrencyDTO settlementCurrency;

    private TransactionAccountDTO debitAccount;

    private TransactionAccountDTO creditAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private FiscalMonthDTO fiscalMonth;

    private PrepaymentCompilationRequestDTO prepaymentCompilationRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPrepaymentPeriod() {
        return prepaymentPeriod;
    }

    public void setPrepaymentPeriod(LocalDate prepaymentPeriod) {
        this.prepaymentPeriod = prepaymentPeriod;
    }

    public BigDecimal getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public PrepaymentAccountDTO getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccountDTO prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public TransactionAccountDTO getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(TransactionAccountDTO debitAccount) {
        this.debitAccount = debitAccount;
    }

    public TransactionAccountDTO getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(TransactionAccountDTO creditAccount) {
        this.creditAccount = creditAccount;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public FiscalMonthDTO getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonthDTO fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public PrepaymentCompilationRequestDTO getPrepaymentCompilationRequest() {
        return prepaymentCompilationRequest;
    }

    public void setPrepaymentCompilationRequest(PrepaymentCompilationRequestDTO prepaymentCompilationRequest) {
        this.prepaymentCompilationRequest = prepaymentCompilationRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentAmortizationDTO)) {
            return false;
        }

        PrepaymentAmortizationDTO prepaymentAmortizationDTO = (PrepaymentAmortizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentAmortizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAmortizationDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", prepaymentPeriod='" + getPrepaymentPeriod() + "'" +
            ", prepaymentAmount=" + getPrepaymentAmount() +
            ", inactive='" + getInactive() + "'" +
            ", prepaymentAccount=" + getPrepaymentAccount() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", debitAccount=" + getDebitAccount() +
            ", creditAccount=" + getCreditAccount() +
            ", placeholders=" + getPlaceholders() +
            ", fiscalMonth=" + getFiscalMonth() +
            ", prepaymentCompilationRequest=" + getPrepaymentCompilationRequest() +
            "}";
    }
}
