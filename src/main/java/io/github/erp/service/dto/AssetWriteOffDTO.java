package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetWriteOff} entity.
 */
public class AssetWriteOffDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal writeOffAmount;

    @NotNull
    private LocalDate writeOffDate;

    private UUID writeOffReferenceId;

    private ApplicationUserDTO createdBy;

    private ApplicationUserDTO modifiedBy;

    private ApplicationUserDTO accessedBy;

    private AssetRegistrationDTO assetWrittenOff;

    private DepreciationPeriodDTO effectivePeriod;

    private PlaceholderDTO placeholder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public LocalDate getWriteOffDate() {
        return writeOffDate;
    }

    public void setWriteOffDate(LocalDate writeOffDate) {
        this.writeOffDate = writeOffDate;
    }

    public UUID getWriteOffReferenceId() {
        return writeOffReferenceId;
    }

    public void setWriteOffReferenceId(UUID writeOffReferenceId) {
        this.writeOffReferenceId = writeOffReferenceId;
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

    public ApplicationUserDTO getAccessedBy() {
        return accessedBy;
    }

    public void setAccessedBy(ApplicationUserDTO accessedBy) {
        this.accessedBy = accessedBy;
    }

    public AssetRegistrationDTO getAssetWrittenOff() {
        return assetWrittenOff;
    }

    public void setAssetWrittenOff(AssetRegistrationDTO assetWrittenOff) {
        this.assetWrittenOff = assetWrittenOff;
    }

    public DepreciationPeriodDTO getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriodDTO effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public PlaceholderDTO getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(PlaceholderDTO placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetWriteOffDTO)) {
            return false;
        }

        AssetWriteOffDTO assetWriteOffDTO = (AssetWriteOffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetWriteOffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetWriteOffDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", writeOffAmount=" + getWriteOffAmount() +
            ", writeOffDate='" + getWriteOffDate() + "'" +
            ", writeOffReferenceId='" + getWriteOffReferenceId() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", modifiedBy=" + getModifiedBy() +
            ", accessedBy=" + getAccessedBy() +
            ", assetWrittenOff=" + getAssetWrittenOff() +
            ", effectivePeriod=" + getEffectivePeriod() +
            ", placeholder=" + getPlaceholder() +
            "}";
    }
}
