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
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.WIPListItem} entity.
 */
public class WIPListItemDTO implements Serializable {

    private Long id;

    private String sequenceNumber;

    private String particulars;

    private LocalDate instalmentDate;

    private BigDecimal instalmentAmount;

    private String settlementCurrency;

    private String outletCode;

    private String settlementTransaction;

    private LocalDate settlementTransactionDate;

    private String dealerName;

    private String workProject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public LocalDate getInstalmentDate() {
        return instalmentDate;
    }

    public void setInstalmentDate(LocalDate instalmentDate) {
        this.instalmentDate = instalmentDate;
    }

    public BigDecimal getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getSettlementTransaction() {
        return settlementTransaction;
    }

    public void setSettlementTransaction(String settlementTransaction) {
        this.settlementTransaction = settlementTransaction;
    }

    public LocalDate getSettlementTransactionDate() {
        return settlementTransactionDate;
    }

    public void setSettlementTransactionDate(LocalDate settlementTransactionDate) {
        this.settlementTransactionDate = settlementTransactionDate;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getWorkProject() {
        return workProject;
    }

    public void setWorkProject(String workProject) {
        this.workProject = workProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WIPListItemDTO)) {
            return false;
        }

        WIPListItemDTO wIPListItemDTO = (WIPListItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wIPListItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WIPListItemDTO{" +
            "id=" + getId() +
            ", sequenceNumber='" + getSequenceNumber() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", instalmentDate='" + getInstalmentDate() + "'" +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", settlementCurrency='" + getSettlementCurrency() + "'" +
            ", outletCode='" + getOutletCode() + "'" +
            ", settlementTransaction='" + getSettlementTransaction() + "'" +
            ", settlementTransactionDate='" + getSettlementTransactionDate() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", workProject='" + getWorkProject() + "'" +
            "}";
    }
}
