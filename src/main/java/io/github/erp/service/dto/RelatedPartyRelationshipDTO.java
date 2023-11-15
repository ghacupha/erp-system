package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RelatedPartyRelationship} entity.
 */
public class RelatedPartyRelationshipDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String customerId;

    @NotNull
    private String relatedPartyId;

    private InstitutionCodeDTO bankCode;

    private BankBranchCodeDTO branchId;

    private PartyRelationTypeDTO relationshipType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRelatedPartyId() {
        return relatedPartyId;
    }

    public void setRelatedPartyId(String relatedPartyId) {
        this.relatedPartyId = relatedPartyId;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public BankBranchCodeDTO getBranchId() {
        return branchId;
    }

    public void setBranchId(BankBranchCodeDTO branchId) {
        this.branchId = branchId;
    }

    public PartyRelationTypeDTO getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(PartyRelationTypeDTO relationshipType) {
        this.relationshipType = relationshipType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelatedPartyRelationshipDTO)) {
            return false;
        }

        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = (RelatedPartyRelationshipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, relatedPartyRelationshipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelatedPartyRelationshipDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", relatedPartyId='" + getRelatedPartyId() + "'" +
            ", bankCode=" + getBankCode() +
            ", branchId=" + getBranchId() +
            ", relationshipType=" + getRelationshipType() +
            "}";
    }
}
