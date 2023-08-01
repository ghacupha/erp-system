package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetCategory} entity.
 */
public class AssetCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String assetCategoryName;

    private String description;

    private String notes;

    @Lob
    private String remarks;

    private BigDecimal depreciationRateYearly;

    private DepreciationMethodDTO depreciationMethod;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getDepreciationRateYearly() {
        return depreciationRateYearly;
    }

    public void setDepreciationRateYearly(BigDecimal depreciationRateYearly) {
        this.depreciationRateYearly = depreciationRateYearly;
    }

    public DepreciationMethodDTO getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethodDTO depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetCategoryDTO)) {
            return false;
        }

        AssetCategoryDTO assetCategoryDTO = (AssetCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetCategoryDTO{" +
            "id=" + getId() +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", depreciationRateYearly=" + getDepreciationRateYearly() +
            ", depreciationMethod=" + getDepreciationMethod() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
