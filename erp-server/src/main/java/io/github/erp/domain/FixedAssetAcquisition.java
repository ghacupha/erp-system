package io.github.erp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FixedAssetAcquisition id(Long id) {
        this.id = id;
        return this;
    }

    public Long getAssetNumber() {
        return this.assetNumber;
    }

    public FixedAssetAcquisition assetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
        return this;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public FixedAssetAcquisition serviceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public FixedAssetAcquisition assetTag(String assetTag) {
        this.assetTag = assetTag;
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public FixedAssetAcquisition assetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public FixedAssetAcquisition purchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public FixedAssetAcquisition assetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimal getPurchasePrice() {
        return this.purchasePrice;
    }

    public FixedAssetAcquisition purchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
        return this;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public FixedAssetAcquisition fileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
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
