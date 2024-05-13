package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetGeneralAdjustment} entity.
 */
public class AssetGeneralAdjustmentDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private BigDecimal devaluationAmount;

    @NotNull
    private LocalDate adjustmentDate;

    @NotNull
    private ZonedDateTime timeOfCreation;

    @NotNull
    private UUID adjustmentReferenceId;

    private DepreciationPeriodDTO effectivePeriod;

    private AssetRegistrationDTO assetRegistration;

    private ApplicationUserDTO createdBy;

    private ApplicationUserDTO lastModifiedBy;

    private ApplicationUserDTO lastAccessedBy;

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

    public BigDecimal getDevaluationAmount() {
        return devaluationAmount;
    }

    public void setDevaluationAmount(BigDecimal devaluationAmount) {
        this.devaluationAmount = devaluationAmount;
    }

    public LocalDate getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(LocalDate adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public ZonedDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(ZonedDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public UUID getAdjustmentReferenceId() {
        return adjustmentReferenceId;
    }

    public void setAdjustmentReferenceId(UUID adjustmentReferenceId) {
        this.adjustmentReferenceId = adjustmentReferenceId;
    }

    public DepreciationPeriodDTO getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriodDTO effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public AssetRegistrationDTO getAssetRegistration() {
        return assetRegistration;
    }

    public void setAssetRegistration(AssetRegistrationDTO assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public ApplicationUserDTO getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(ApplicationUserDTO lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ApplicationUserDTO getLastAccessedBy() {
        return lastAccessedBy;
    }

    public void setLastAccessedBy(ApplicationUserDTO lastAccessedBy) {
        this.lastAccessedBy = lastAccessedBy;
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
        if (!(o instanceof AssetGeneralAdjustmentDTO)) {
            return false;
        }

        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = (AssetGeneralAdjustmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetGeneralAdjustmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetGeneralAdjustmentDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", devaluationAmount=" + getDevaluationAmount() +
            ", adjustmentDate='" + getAdjustmentDate() + "'" +
            ", timeOfCreation='" + getTimeOfCreation() + "'" +
            ", adjustmentReferenceId='" + getAdjustmentReferenceId() + "'" +
            ", effectivePeriod=" + getEffectivePeriod() +
            ", assetRegistration=" + getAssetRegistration() +
            ", createdBy=" + getCreatedBy() +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastAccessedBy=" + getLastAccessedBy() +
            ", placeholder=" + getPlaceholder() +
            "}";
    }
}
