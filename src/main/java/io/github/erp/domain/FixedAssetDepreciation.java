package io.github.erp.domain;

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
 * A FixedAssetDepreciation.
 */
@Entity
@Table(name = "fixed_asset_depreciation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fixedassetdepreciation")
public class FixedAssetDepreciation implements Serializable {

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

    @Column(name = "depreciation_date")
    private LocalDate depreciationDate;

    @Column(name = "asset_category")
    private String assetCategory;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_regime")
    private DepreciationRegime depreciationRegime;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_fixed_asset_depreciation__placeholder",
        joinColumns = @JoinColumn(name = "fixed_asset_depreciation_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FixedAssetDepreciation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetNumber() {
        return this.assetNumber;
    }

    public FixedAssetDepreciation assetNumber(Long assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public FixedAssetDepreciation serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public FixedAssetDepreciation assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public FixedAssetDepreciation assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDate getDepreciationDate() {
        return this.depreciationDate;
    }

    public FixedAssetDepreciation depreciationDate(LocalDate depreciationDate) {
        this.setDepreciationDate(depreciationDate);
        return this;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public FixedAssetDepreciation assetCategory(String assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public FixedAssetDepreciation depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public DepreciationRegime getDepreciationRegime() {
        return this.depreciationRegime;
    }

    public FixedAssetDepreciation depreciationRegime(DepreciationRegime depreciationRegime) {
        this.setDepreciationRegime(depreciationRegime);
        return this;
    }

    public void setDepreciationRegime(DepreciationRegime depreciationRegime) {
        this.depreciationRegime = depreciationRegime;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public FixedAssetDepreciation fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public FixedAssetDepreciation compilationToken(String compilationToken) {
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

    public FixedAssetDepreciation placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public FixedAssetDepreciation addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public FixedAssetDepreciation removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FixedAssetDepreciation)) {
            return false;
        }
        return id != null && id.equals(((FixedAssetDepreciation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FixedAssetDepreciation{" +
            "id=" + getId() +
            ", assetNumber=" + getAssetNumber() +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", depreciationDate='" + getDepreciationDate() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", depreciationRegime='" + getDepreciationRegime() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
