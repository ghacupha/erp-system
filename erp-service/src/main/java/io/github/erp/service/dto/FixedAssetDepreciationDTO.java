package io.github.erp.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
import io.github.erp.domain.enumeration.DepreciationRegime;
import io.github.erp.internal.framework.batch.HasIndex;

/**
 * TODO implement custom object for asset-depreciation
 *
 * A DTO for the {@link io.github.erp.domain.FixedAssetDepreciation} entity.
 */
public class FixedAssetDepreciationDTO implements Serializable, HasIndex {

    private Long id;

    private Long assetNumber;

    private String serviceOutletCode;

    private String assetTag;

    private String assetDescription;

    private LocalDate depreciationDate;

    private String assetCategory;

    private BigDecimal depreciationAmount;

    private DepreciationRegime depreciationRegime;

    private String fileUploadToken;

    private String compilationToken;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return serviceOutletCode;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDate getDepreciationDate() {
        return depreciationDate;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public DepreciationRegime getDepreciationRegime() {
        return depreciationRegime;
    }

    public void setDepreciationRegime(DepreciationRegime depreciationRegime) {
        this.depreciationRegime = depreciationRegime;
    }

    public String getFileUploadToken() {
        return fileUploadToken;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FixedAssetDepreciationDTO)) {
            return false;
        }

        return id != null && id.equals(((FixedAssetDepreciationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FixedAssetDepreciationDTO{" +
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
