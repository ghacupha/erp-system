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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.WorkProjectRegister} entity.
 */
public class WorkProjectRegisterDTO implements Serializable {

    private Long id;

    @NotNull
    private String catalogueNumber;

    @NotNull
    private String projectTitle;

    private String description;

    @Lob
    private byte[] details;

    private String detailsContentType;
    private BigDecimal totalProjectCost;

    @Lob
    private byte[] additionalNotes;

    private String additionalNotesContentType;
    private Set<DealerDTO> dealers = new HashSet<>();

    private SettlementCurrencyDTO settlementCurrency;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogueNumber() {
        return catalogueNumber;
    }

    public void setCatalogueNumber(String catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getDetails() {
        return details;
    }

    public void setDetails(byte[] details) {
        this.details = details;
    }

    public String getDetailsContentType() {
        return detailsContentType;
    }

    public void setDetailsContentType(String detailsContentType) {
        this.detailsContentType = detailsContentType;
    }

    public BigDecimal getTotalProjectCost() {
        return totalProjectCost;
    }

    public void setTotalProjectCost(BigDecimal totalProjectCost) {
        this.totalProjectCost = totalProjectCost;
    }

    public byte[] getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(byte[] additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getAdditionalNotesContentType() {
        return additionalNotesContentType;
    }

    public void setAdditionalNotesContentType(String additionalNotesContentType) {
        this.additionalNotesContentType = additionalNotesContentType;
    }

    public Set<DealerDTO> getDealers() {
        return dealers;
    }

    public void setDealers(Set<DealerDTO> dealers) {
        this.dealers = dealers;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkProjectRegisterDTO)) {
            return false;
        }

        WorkProjectRegisterDTO workProjectRegisterDTO = (WorkProjectRegisterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workProjectRegisterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkProjectRegisterDTO{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", projectTitle='" + getProjectTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", details='" + getDetails() + "'" +
            ", totalProjectCost=" + getTotalProjectCost() +
            ", additionalNotes='" + getAdditionalNotes() + "'" +
            ", dealers=" + getDealers() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", placeholders=" + getPlaceholders() +
            ", businessDocuments=" + getBusinessDocuments() +
            "}";
    }
}
