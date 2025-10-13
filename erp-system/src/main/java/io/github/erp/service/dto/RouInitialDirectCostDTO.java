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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RouInitialDirectCost} entity.
 */
public class RouInitialDirectCostDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate transactionDate;

    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal cost;

    private Long referenceNumber;

    private IFRS16LeaseContractDTO leaseContract;

    private SettlementDTO settlementDetails;

    private TransactionAccountDTO targetROUAccount;

    private TransactionAccountDTO transferAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Long getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(Long referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public IFRS16LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    public SettlementDTO getSettlementDetails() {
        return settlementDetails;
    }

    public void setSettlementDetails(SettlementDTO settlementDetails) {
        this.settlementDetails = settlementDetails;
    }

    public TransactionAccountDTO getTargetROUAccount() {
        return targetROUAccount;
    }

    public void setTargetROUAccount(TransactionAccountDTO targetROUAccount) {
        this.targetROUAccount = targetROUAccount;
    }

    public TransactionAccountDTO getTransferAccount() {
        return transferAccount;
    }

    public void setTransferAccount(TransactionAccountDTO transferAccount) {
        this.transferAccount = transferAccount;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouInitialDirectCostDTO)) {
            return false;
        }

        RouInitialDirectCostDTO rouInitialDirectCostDTO = (RouInitialDirectCostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouInitialDirectCostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouInitialDirectCostDTO{" +
            "id=" + getId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", cost=" + getCost() +
            ", referenceNumber=" + getReferenceNumber() +
            ", leaseContract=" + getLeaseContract() +
            ", settlementDetails=" + getSettlementDetails() +
            ", targetROUAccount=" + getTargetROUAccount() +
            ", transferAccount=" + getTransferAccount() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
