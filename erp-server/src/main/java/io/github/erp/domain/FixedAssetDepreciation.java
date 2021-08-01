package io.github.erp.domain;

import io.github.erp.domain.enumeration.DepreciationRegime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FixedAssetDepreciation id(Long id) {
        this.id = id;
        return this;
    }

    public Long getAssetNumber() {
        return this.assetNumber;
    }

    public FixedAssetDepreciation assetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
        return this;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public FixedAssetDepreciation serviceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public FixedAssetDepreciation assetTag(String assetTag) {
        this.assetTag = assetTag;
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public FixedAssetDepreciation assetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDate getDepreciationDate() {
        return this.depreciationDate;
    }

    public FixedAssetDepreciation depreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
        return this;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public FixedAssetDepreciation assetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public FixedAssetDepreciation depreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public DepreciationRegime getDepreciationRegime() {
        return this.depreciationRegime;
    }

    public FixedAssetDepreciation depreciationRegime(DepreciationRegime depreciationRegime) {
        this.depreciationRegime = depreciationRegime;
        return this;
    }

    public void setDepreciationRegime(DepreciationRegime depreciationRegime) {
        this.depreciationRegime = depreciationRegime;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public FixedAssetDepreciation fileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public FixedAssetDepreciation compilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
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
