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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetWarranty} entity.
 */
public class AssetWarrantyDTO implements Serializable {

    private Long id;

    private String assetTag;

    private String description;

    private String modelNumber;

    private String serialNumber;

    private LocalDate expiryDate;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

    private DealerDTO dealer;

    private Set<BusinessDocumentDTO> warrantyAttachments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getUniversallyUniqueMappings() {
        return universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMappingDTO> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public DealerDTO getDealer() {
        return dealer;
    }

    public void setDealer(DealerDTO dealer) {
        this.dealer = dealer;
    }

    public Set<BusinessDocumentDTO> getWarrantyAttachments() {
        return warrantyAttachments;
    }

    public void setWarrantyAttachments(Set<BusinessDocumentDTO> warrantyAttachments) {
        this.warrantyAttachments = warrantyAttachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetWarrantyDTO)) {
            return false;
        }

        AssetWarrantyDTO assetWarrantyDTO = (AssetWarrantyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetWarrantyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetWarrantyDTO{" +
            "id=" + getId() +
            ", assetTag='" + getAssetTag() + "'" +
            ", description='" + getDescription() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            ", dealer=" + getDealer() +
            ", warrantyAttachments=" + getWarrantyAttachments() +
            "}";
    }
}
