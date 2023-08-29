package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FixedAssetAcquisition.
 */
@Entity
@Table(name = "fixed_asset_acquisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fixedassetacquisition")
public class FixedAssetAcquisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_number", unique = true)
    private Long assetNumber;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "asset_tag")
    private String assetTag;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "asset_category")
    private String assetCategory;

    @Column(name = "purchase_price", precision = 21, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @ManyToMany
    @JoinTable(
        name = "rel_fixed_asset_acquisition__placeholder",
        joinColumns = @JoinColumn(name = "fixed_asset_acquisition_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FixedAssetAcquisition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetNumber() {
        return this.assetNumber;
    }

    public FixedAssetAcquisition assetNumber(Long assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public FixedAssetAcquisition serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public FixedAssetAcquisition assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public FixedAssetAcquisition assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public FixedAssetAcquisition purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public FixedAssetAcquisition assetCategory(String assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimal getPurchasePrice() {
        return this.purchasePrice;
    }

    public FixedAssetAcquisition purchasePrice(BigDecimal purchasePrice) {
        this.setPurchasePrice(purchasePrice);
        return this;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public FixedAssetAcquisition fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public FixedAssetAcquisition placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public FixedAssetAcquisition addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public FixedAssetAcquisition removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FixedAssetAcquisition)) {
            return false;
        }
        return id != null && id.equals(((FixedAssetAcquisition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FixedAssetAcquisition{" +
            "id=" + getId() +
            ", assetNumber=" + getAssetNumber() +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            "}";
    }
}
