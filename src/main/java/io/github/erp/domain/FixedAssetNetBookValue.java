package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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
import io.github.erp.domain.enumeration.DepreciationRegime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FixedAssetNetBookValue.
 */
@Entity
@Table(name = "fixed_asset_net_book_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fixedassetnetbookvalue")
public class FixedAssetNetBookValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_number")
    private Long assetNumber;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "asset_tag")
    private String assetTag;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "net_book_value_date")
    private LocalDate netBookValueDate;

    @Column(name = "asset_category")
    private String assetCategory;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_regime")
    private DepreciationRegime depreciationRegime;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_fixed_asset_net_book_value__placeholder",
        joinColumns = @JoinColumn(name = "fixed_asset_net_book_value_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FixedAssetNetBookValue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetNumber() {
        return this.assetNumber;
    }

    public FixedAssetNetBookValue assetNumber(Long assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public FixedAssetNetBookValue serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public FixedAssetNetBookValue assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public FixedAssetNetBookValue assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDate getNetBookValueDate() {
        return this.netBookValueDate;
    }

    public FixedAssetNetBookValue netBookValueDate(LocalDate netBookValueDate) {
        this.setNetBookValueDate(netBookValueDate);
        return this;
    }

    public void setNetBookValueDate(LocalDate netBookValueDate) {
        this.netBookValueDate = netBookValueDate;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public FixedAssetNetBookValue assetCategory(String assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public FixedAssetNetBookValue netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public DepreciationRegime getDepreciationRegime() {
        return this.depreciationRegime;
    }

    public FixedAssetNetBookValue depreciationRegime(DepreciationRegime depreciationRegime) {
        this.setDepreciationRegime(depreciationRegime);
        return this;
    }

    public void setDepreciationRegime(DepreciationRegime depreciationRegime) {
        this.depreciationRegime = depreciationRegime;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public FixedAssetNetBookValue fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public FixedAssetNetBookValue compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public FixedAssetNetBookValue placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public FixedAssetNetBookValue addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public FixedAssetNetBookValue removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FixedAssetNetBookValue)) {
            return false;
        }
        return id != null && id.equals(((FixedAssetNetBookValue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FixedAssetNetBookValue{" +
            "id=" + getId() +
            ", assetNumber=" + getAssetNumber() +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", netBookValueDate='" + getNetBookValueDate() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", netBookValue=" + getNetBookValue() +
            ", depreciationRegime='" + getDepreciationRegime() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
