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
 * A WIPTransferListItem.
 */
@Entity
@Table(name = "wiptransfer_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "wiptransferlistitem")
public class WIPTransferListItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "wip_sequence")
    private Long wipSequence;

    @Column(name = "wip_particulars")
    private String wipParticulars;

    @Column(name = "transfer_type")
    private String transferType;

    @Column(name = "transfer_settlement")
    private String transferSettlement;

    @Column(name = "transfer_settlement_date")
    private LocalDate transferSettlementDate;

    @Column(name = "transfer_amount", precision = 21, scale = 2)
    private BigDecimal transferAmount;

    @Column(name = "wip_transfer_date")
    private LocalDate wipTransferDate;

    @Column(name = "original_settlement")
    private String originalSettlement;

    @Column(name = "original_settlement_date")
    private LocalDate originalSettlementDate;

    @Column(name = "asset_category")
    private String assetCategory;

    @Column(name = "service_outlet")
    private String serviceOutlet;

    @Column(name = "work_project")
    private String workProject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WIPTransferListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWipSequence() {
        return this.wipSequence;
    }

    public WIPTransferListItem wipSequence(Long wipSequence) {
        this.setWipSequence(wipSequence);
        return this;
    }

    public void setWipSequence(Long wipSequence) {
        this.wipSequence = wipSequence;
    }

    public String getWipParticulars() {
        return this.wipParticulars;
    }

    public WIPTransferListItem wipParticulars(String wipParticulars) {
        this.setWipParticulars(wipParticulars);
        return this;
    }

    public void setWipParticulars(String wipParticulars) {
        this.wipParticulars = wipParticulars;
    }

    public String getTransferType() {
        return this.transferType;
    }

    public WIPTransferListItem transferType(String transferType) {
        this.setTransferType(transferType);
        return this;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferSettlement() {
        return this.transferSettlement;
    }

    public WIPTransferListItem transferSettlement(String transferSettlement) {
        this.setTransferSettlement(transferSettlement);
        return this;
    }

    public void setTransferSettlement(String transferSettlement) {
        this.transferSettlement = transferSettlement;
    }

    public LocalDate getTransferSettlementDate() {
        return this.transferSettlementDate;
    }

    public WIPTransferListItem transferSettlementDate(LocalDate transferSettlementDate) {
        this.setTransferSettlementDate(transferSettlementDate);
        return this;
    }

    public void setTransferSettlementDate(LocalDate transferSettlementDate) {
        this.transferSettlementDate = transferSettlementDate;
    }

    public BigDecimal getTransferAmount() {
        return this.transferAmount;
    }

    public WIPTransferListItem transferAmount(BigDecimal transferAmount) {
        this.setTransferAmount(transferAmount);
        return this;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDate getWipTransferDate() {
        return this.wipTransferDate;
    }

    public WIPTransferListItem wipTransferDate(LocalDate wipTransferDate) {
        this.setWipTransferDate(wipTransferDate);
        return this;
    }

    public void setWipTransferDate(LocalDate wipTransferDate) {
        this.wipTransferDate = wipTransferDate;
    }

    public String getOriginalSettlement() {
        return this.originalSettlement;
    }

    public WIPTransferListItem originalSettlement(String originalSettlement) {
        this.setOriginalSettlement(originalSettlement);
        return this;
    }

    public void setOriginalSettlement(String originalSettlement) {
        this.originalSettlement = originalSettlement;
    }

    public LocalDate getOriginalSettlementDate() {
        return this.originalSettlementDate;
    }

    public WIPTransferListItem originalSettlementDate(LocalDate originalSettlementDate) {
        this.setOriginalSettlementDate(originalSettlementDate);
        return this;
    }

    public void setOriginalSettlementDate(LocalDate originalSettlementDate) {
        this.originalSettlementDate = originalSettlementDate;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public WIPTransferListItem assetCategory(String assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getServiceOutlet() {
        return this.serviceOutlet;
    }

    public WIPTransferListItem serviceOutlet(String serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public void setServiceOutlet(String serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public String getWorkProject() {
        return this.workProject;
    }

    public WIPTransferListItem workProject(String workProject) {
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
        if (!(o instanceof WIPTransferListItem)) {
            return false;
        }
        return id != null && id.equals(((WIPTransferListItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WIPTransferListItem{" +
            "id=" + getId() +
            ", wipSequence=" + getWipSequence() +
            ", wipParticulars='" + getWipParticulars() + "'" +
            ", transferType='" + getTransferType() + "'" +
            ", transferSettlement='" + getTransferSettlement() + "'" +
            ", transferSettlementDate='" + getTransferSettlementDate() + "'" +
            ", transferAmount=" + getTransferAmount() +
            ", wipTransferDate='" + getWipTransferDate() + "'" +
            ", originalSettlement='" + getOriginalSettlement() + "'" +
            ", originalSettlementDate='" + getOriginalSettlementDate() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", serviceOutlet='" + getServiceOutlet() + "'" +
            ", workProject='" + getWorkProject() + "'" +
            "}";
    }
}
