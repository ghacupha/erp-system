package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetDisposal} entity.
 */
public class AssetDisposalDTO implements Serializable {

    private Long id;

    private UUID assetDisposalReference;

    private String description;

    private BigDecimal assetCost;

    private BigDecimal historicalCost;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal accruedDepreciation;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal netBookValue;

    private LocalDate decommissioningDate;

    @NotNull
    private LocalDate disposalDate;

    private Boolean dormant;

    private ApplicationUserDTO createdBy;

    private ApplicationUserDTO modifiedBy;

    private ApplicationUserDTO lastAccessedBy;

    private DepreciationPeriodDTO effectivePeriod;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private AssetRegistrationDTO assetDisposed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getAssetDisposalReference() {
        return assetDisposalReference;
    }

    public void setAssetDisposalReference(UUID assetDisposalReference) {
        this.assetDisposalReference = assetDisposalReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAssetCost() {
        return assetCost;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
    }

    public BigDecimal getHistoricalCost() {
        return historicalCost;
    }

    public void setHistoricalCost(BigDecimal historicalCost) {
        this.historicalCost = historicalCost;
    }

    public BigDecimal getAccruedDepreciation() {
        return accruedDepreciation;
    }

    public void setAccruedDepreciation(BigDecimal accruedDepreciation) {
        this.accruedDepreciation = accruedDepreciation;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getDecommissioningDate() {
        return decommissioningDate;
    }

    public void setDecommissioningDate(LocalDate decommissioningDate) {
        this.decommissioningDate = decommissioningDate;
    }

    public LocalDate getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(LocalDate disposalDate) {
        this.disposalDate = disposalDate;
    }

    public Boolean getDormant() {
        return dormant;
    }

    public void setDormant(Boolean dormant) {
        this.dormant = dormant;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public ApplicationUserDTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(ApplicationUserDTO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ApplicationUserDTO getLastAccessedBy() {
        return lastAccessedBy;
    }

    public void setLastAccessedBy(ApplicationUserDTO lastAccessedBy) {
        this.lastAccessedBy = lastAccessedBy;
    }

    public DepreciationPeriodDTO getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriodDTO effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public AssetRegistrationDTO getAssetDisposed() {
        return assetDisposed;
    }

    public void setAssetDisposed(AssetRegistrationDTO assetDisposed) {
        this.assetDisposed = assetDisposed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDisposalDTO)) {
            return false;
        }

        AssetDisposalDTO assetDisposalDTO = (AssetDisposalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetDisposalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDisposalDTO{" +
            "id=" + getId() +
            ", assetDisposalReference='" + getAssetDisposalReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", assetCost=" + getAssetCost() +
            ", historicalCost=" + getHistoricalCost() +
            ", accruedDepreciation=" + getAccruedDepreciation() +
            ", netBookValue=" + getNetBookValue() +
            ", decommissioningDate='" + getDecommissioningDate() + "'" +
            ", disposalDate='" + getDisposalDate() + "'" +
            ", dormant='" + getDormant() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", modifiedBy=" + getModifiedBy() +
            ", lastAccessedBy=" + getLastAccessedBy() +
            ", effectivePeriod=" + getEffectivePeriod() +
            ", placeholders=" + getPlaceholders() +
            ", assetDisposed=" + getAssetDisposed() +
            "}";
    }
}
