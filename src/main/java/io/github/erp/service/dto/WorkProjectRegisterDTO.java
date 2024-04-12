package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
