package io.github.erp.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
import io.github.erp.domain.enumeration.DepreciationRegime;
import io.github.erp.internal.framework.batch.HasIndex;

/**
 * TODO implement custom object for asset-nbv
 *
 * A DTO for the {@link io.github.erp.domain.FixedAssetNetBookValue} entity.
 */
public class FixedAssetNetBookValueDTO implements Serializable, HasIndex {

    private Long id;

    private Long assetNumber;

    private String serviceOutletCode;

    private String assetTag;

    private String assetDescription;

    private LocalDate netBookValueDate;

    private String assetCategory;

    private BigDecimal netBookValue;

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

    public LocalDate getNetBookValueDate() {
        return netBookValueDate;
    }

    public void setNetBookValueDate(LocalDate netBookValueDate) {
        this.netBookValueDate = netBookValueDate;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
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
        if (!(o instanceof FixedAssetNetBookValueDTO)) {
            return false;
        }

        return id != null && id.equals(((FixedAssetNetBookValueDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FixedAssetNetBookValueDTO{" +
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
