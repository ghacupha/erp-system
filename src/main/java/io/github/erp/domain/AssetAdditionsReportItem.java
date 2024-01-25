package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
 * A AssetAdditionsReportItem.
 */
@Entity
@Table(name = "asset_additions_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetadditionsreportitem")
public class AssetAdditionsReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_number")
    private String assetNumber;

    @Column(name = "asset_tag")
    private String assetTag;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "capitalization_date")
    private LocalDate capitalizationDate;

    @Column(name = "asset_category")
    private String assetCategory;

    @Column(name = "asset_details")
    private String assetDetails;

    @Column(name = "asset_cost", precision = 21, scale = 2)
    private BigDecimal assetCost;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "lpo_number")
    private String lpoNumber;

    @Column(name = "wip_transfer")
    private Boolean wipTransfer;

    @Column(name = "wip_transfer_amount", precision = 21, scale = 2)
    private BigDecimal wipTransferAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetAdditionsReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return this.assetNumber;
    }

    public AssetAdditionsReportItem assetNumber(String assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public AssetAdditionsReportItem assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public AssetAdditionsReportItem serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public AssetAdditionsReportItem transactionId(String transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public AssetAdditionsReportItem transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDate getCapitalizationDate() {
        return this.capitalizationDate;
    }

    public AssetAdditionsReportItem capitalizationDate(LocalDate capitalizationDate) {
        this.setCapitalizationDate(capitalizationDate);
        return this;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public AssetAdditionsReportItem assetCategory(String assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getAssetDetails() {
        return this.assetDetails;
    }

    public AssetAdditionsReportItem assetDetails(String assetDetails) {
        this.setAssetDetails(assetDetails);
        return this;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public BigDecimal getAssetCost() {
        return this.assetCost;
    }

    public AssetAdditionsReportItem assetCost(BigDecimal assetCost) {
        this.setAssetCost(assetCost);
        return this;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public AssetAdditionsReportItem supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public AssetAdditionsReportItem invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getLpoNumber() {
        return this.lpoNumber;
    }

    public AssetAdditionsReportItem lpoNumber(String lpoNumber) {
        this.setLpoNumber(lpoNumber);
        return this;
    }

    public void setLpoNumber(String lpoNumber) {
        this.lpoNumber = lpoNumber;
    }

    public Boolean getWipTransfer() {
        return this.wipTransfer;
    }

    public AssetAdditionsReportItem wipTransfer(Boolean wipTransfer) {
        this.setWipTransfer(wipTransfer);
        return this;
    }

    public void setWipTransfer(Boolean wipTransfer) {
        this.wipTransfer = wipTransfer;
    }

    public BigDecimal getWipTransferAmount() {
        return this.wipTransferAmount;
    }

    public AssetAdditionsReportItem wipTransferAmount(BigDecimal wipTransferAmount) {
        this.setWipTransferAmount(wipTransferAmount);
        return this;
    }

    public void setWipTransferAmount(BigDecimal wipTransferAmount) {
        this.wipTransferAmount = wipTransferAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetAdditionsReportItem)) {
            return false;
        }
        return id != null && id.equals(((AssetAdditionsReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAdditionsReportItem{" +
            "id=" + getId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", assetDetails='" + getAssetDetails() + "'" +
            ", assetCost=" + getAssetCost() +
            ", supplier='" + getSupplier() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", lpoNumber='" + getLpoNumber() + "'" +
            ", wipTransfer='" + getWipTransfer() + "'" +
            ", wipTransferAmount=" + getWipTransferAmount() +
            "}";
    }
}
