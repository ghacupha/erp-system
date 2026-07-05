package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WIPListItem.
 */
@Entity
@Table(name = "wiplist_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "wiplistitem")
public class WIPListItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_number")
    private String sequenceNumber;

    @Column(name = "particulars")
    private String particulars;

    @Column(name = "instalment_date")
    private LocalDate instalmentDate;

    @Column(name = "instalment_amount", precision = 21, scale = 2)
    private BigDecimal instalmentAmount;

    @Column(name = "settlement_currency")
    private String settlementCurrency;

    @Column(name = "outlet_code")
    private String outletCode;

    @Column(name = "settlement_transaction")
    private String settlementTransaction;

    @Column(name = "settlement_transaction_date")
    private LocalDate settlementTransactionDate;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "work_project")
    private String workProject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WIPListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return this.sequenceNumber;
    }

    public WIPListItem sequenceNumber(String sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return this.particulars;
    }

    public WIPListItem particulars(String particulars) {
        this.setParticulars(particulars);
        return this;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public LocalDate getInstalmentDate() {
        return this.instalmentDate;
    }

    public WIPListItem instalmentDate(LocalDate instalmentDate) {
        this.setInstalmentDate(instalmentDate);
        return this;
    }

    public void setInstalmentDate(LocalDate instalmentDate) {
        this.instalmentDate = instalmentDate;
    }

    public BigDecimal getInstalmentAmount() {
        return this.instalmentAmount;
    }

    public WIPListItem instalmentAmount(BigDecimal instalmentAmount) {
        this.setInstalmentAmount(instalmentAmount);
        return this;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public String getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public WIPListItem settlementCurrency(String settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getOutletCode() {
        return this.outletCode;
    }

    public WIPListItem outletCode(String outletCode) {
        this.setOutletCode(outletCode);
        return this;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getSettlementTransaction() {
        return this.settlementTransaction;
    }

    public WIPListItem settlementTransaction(String settlementTransaction) {
        this.setSettlementTransaction(settlementTransaction);
        return this;
    }

    public void setSettlementTransaction(String settlementTransaction) {
        this.settlementTransaction = settlementTransaction;
    }

    public LocalDate getSettlementTransactionDate() {
        return this.settlementTransactionDate;
    }

    public WIPListItem settlementTransactionDate(LocalDate settlementTransactionDate) {
        this.setSettlementTransactionDate(settlementTransactionDate);
        return this;
    }

    public void setSettlementTransactionDate(LocalDate settlementTransactionDate) {
        this.settlementTransactionDate = settlementTransactionDate;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public WIPListItem dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getWorkProject() {
        return this.workProject;
    }

    public WIPListItem workProject(String workProject) {
        this.setWorkProject(workProject);
        return this;
    }

    public void setWorkProject(String workProject) {
        this.workProject = workProject;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WIPListItem)) {
            return false;
        }
        return id != null && id.equals(((WIPListItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WIPListItem{" +
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
