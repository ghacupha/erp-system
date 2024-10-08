package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.InternalMemo} entity.
 */
public class InternalMemoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 8, max = 8)
    private String catalogueNumber;

    @NotNull
    private String referenceNumber;

    @NotNull
    private LocalDate memoDate;

    private String purposeDescription;

    private MemoActionDTO actionRequired;

    private DealerDTO addressedTo;

    private DealerDTO from;

    private Set<DealerDTO> preparedBies = new HashSet<>();

    private Set<DealerDTO> reviewedBies = new HashSet<>();

    private Set<DealerDTO> approvedBies = new HashSet<>();

    private Set<BusinessDocumentDTO> memoDocuments = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(LocalDate memoDate) {
        this.memoDate = memoDate;
    }

    public String getPurposeDescription() {
        return purposeDescription;
    }

    public void setPurposeDescription(String purposeDescription) {
        this.purposeDescription = purposeDescription;
    }

    public MemoActionDTO getActionRequired() {
        return actionRequired;
    }

    public void setActionRequired(MemoActionDTO actionRequired) {
        this.actionRequired = actionRequired;
    }

    public DealerDTO getAddressedTo() {
        return addressedTo;
    }

    public void setAddressedTo(DealerDTO addressedTo) {
        this.addressedTo = addressedTo;
    }

    public DealerDTO getFrom() {
        return from;
    }

    public void setFrom(DealerDTO from) {
        this.from = from;
    }

    public Set<DealerDTO> getPreparedBies() {
        return preparedBies;
    }

    public void setPreparedBies(Set<DealerDTO> preparedBies) {
        this.preparedBies = preparedBies;
    }

    public Set<DealerDTO> getReviewedBies() {
        return reviewedBies;
    }

    public void setReviewedBies(Set<DealerDTO> reviewedBies) {
        this.reviewedBies = reviewedBies;
    }

    public Set<DealerDTO> getApprovedBies() {
        return approvedBies;
    }

    public void setApprovedBies(Set<DealerDTO> approvedBies) {
        this.approvedBies = approvedBies;
    }

    public Set<BusinessDocumentDTO> getMemoDocuments() {
        return memoDocuments;
    }

    public void setMemoDocuments(Set<BusinessDocumentDTO> memoDocuments) {
        this.memoDocuments = memoDocuments;
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
        if (!(o instanceof InternalMemoDTO)) {
            return false;
        }

        InternalMemoDTO internalMemoDTO = (InternalMemoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, internalMemoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InternalMemoDTO{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", memoDate='" + getMemoDate() + "'" +
            ", purposeDescription='" + getPurposeDescription() + "'" +
            ", actionRequired=" + getActionRequired() +
            ", addressedTo=" + getAddressedTo() +
            ", from=" + getFrom() +
            ", preparedBies=" + getPreparedBies() +
            ", reviewedBies=" + getReviewedBies() +
            ", approvedBies=" + getApprovedBies() +
            ", memoDocuments=" + getMemoDocuments() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
