package io.github.erp.service.dto;

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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    private ApplicationUserDTO lastAccessedBy;

    private DepreciationPeriodDTO effectivePeriod;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private AssetRegistrationDTO assetWrittenOff;

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

    public AssetRegistrationDTO getAssetWrittenOff() {
        return assetWrittenOff;
    }

    public void setAssetWrittenOff(AssetRegistrationDTO assetWrittenOff) {
        this.assetWrittenOff = assetWrittenOff;
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
            ", lastAccessedBy=" + getLastAccessedBy() +
            ", effectivePeriod=" + getEffectivePeriod() +
            ", placeholders=" + getPlaceholders() +
            ", assetWrittenOff=" + getAssetWrittenOff() +
            "}";
    }
}
