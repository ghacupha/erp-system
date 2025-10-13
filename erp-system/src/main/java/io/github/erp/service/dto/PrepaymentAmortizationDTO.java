package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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

    private UUID amortizationIdentifier;

    private PrepaymentAccountDTO prepaymentAccount;

    private SettlementCurrencyDTO settlementCurrency;

    private TransactionAccountDTO debitAccount;

    private TransactionAccountDTO creditAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private FiscalMonthDTO fiscalMonth;

    private PrepaymentCompilationRequestDTO prepaymentCompilationRequest;

    private AmortizationPeriodDTO amortizationPeriod;

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

    public UUID getAmortizationIdentifier() {
        return amortizationIdentifier;
    }

    public void setAmortizationIdentifier(UUID amortizationIdentifier) {
        this.amortizationIdentifier = amortizationIdentifier;
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

    public AmortizationPeriodDTO getAmortizationPeriod() {
        return amortizationPeriod;
    }

    public void setAmortizationPeriod(AmortizationPeriodDTO amortizationPeriod) {
        this.amortizationPeriod = amortizationPeriod;
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
            ", amortizationIdentifier='" + getAmortizationIdentifier() + "'" +
            ", prepaymentAccount=" + getPrepaymentAccount() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", debitAccount=" + getDebitAccount() +
            ", creditAccount=" + getCreditAccount() +
            ", placeholders=" + getPlaceholders() +
            ", fiscalMonth=" + getFiscalMonth() +
            ", prepaymentCompilationRequest=" + getPrepaymentCompilationRequest() +
            ", amortizationPeriod=" + getAmortizationPeriod() +
            "}";
    }
}
