package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetRevaluation} entity.
 */
public class AssetRevaluationDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private BigDecimal devaluationAmount;

    @NotNull
    private LocalDate revaluationDate;

    private UUID revaluationReferenceId;

    private ZonedDateTime timeOfCreation;

    private DealerDTO revaluer;

    private ApplicationUserDTO createdBy;

    private ApplicationUserDTO lastModifiedBy;

    private ApplicationUserDTO lastAccessedBy;

    private DepreciationPeriodDTO effectivePeriod;

    private AssetRegistrationDTO revaluedAsset;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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

    public LocalDate getRevaluationDate() {
        return revaluationDate;
    }

    public void setRevaluationDate(LocalDate revaluationDate) {
        this.revaluationDate = revaluationDate;
    }

    public UUID getRevaluationReferenceId() {
        return revaluationReferenceId;
    }

    public void setRevaluationReferenceId(UUID revaluationReferenceId) {
        this.revaluationReferenceId = revaluationReferenceId;
    }

    public ZonedDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(ZonedDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public DealerDTO getRevaluer() {
        return revaluer;
    }

    public void setRevaluer(DealerDTO revaluer) {
        this.revaluer = revaluer;
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

    public DepreciationPeriodDTO getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriodDTO effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public AssetRegistrationDTO getRevaluedAsset() {
        return revaluedAsset;
    }

    public void setRevaluedAsset(AssetRegistrationDTO revaluedAsset) {
        this.revaluedAsset = revaluedAsset;
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
        if (!(o instanceof AssetRevaluationDTO)) {
            return false;
        }

        AssetRevaluationDTO assetRevaluationDTO = (AssetRevaluationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetRevaluationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRevaluationDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", devaluationAmount=" + getDevaluationAmount() +
            ", revaluationDate='" + getRevaluationDate() + "'" +
            ", revaluationReferenceId='" + getRevaluationReferenceId() + "'" +
            ", timeOfCreation='" + getTimeOfCreation() + "'" +
            ", revaluer=" + getRevaluer() +
            ", createdBy=" + getCreatedBy() +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastAccessedBy=" + getLastAccessedBy() +
            ", effectivePeriod=" + getEffectivePeriod() +
            ", revaluedAsset=" + getRevaluedAsset() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
