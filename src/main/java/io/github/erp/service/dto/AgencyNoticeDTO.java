package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.enumeration.AgencyStatusType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AgencyNotice} entity.
 */
public class AgencyNoticeDTO implements Serializable {

    private Long id;

    @NotNull
    private String referenceNumber;

    private LocalDate referenceDate;

    @NotNull
    private BigDecimal assessmentAmount;

    @NotNull
    private AgencyStatusType agencyStatus;

    @Lob
    private byte[] assessmentNotice;

    private String assessmentNoticeContentType;
    private Set<DealerDTO> correspondents = new HashSet<>();

    private SettlementCurrencyDTO settlementCurrency;

    private DealerDTO assessor;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public BigDecimal getAssessmentAmount() {
        return assessmentAmount;
    }

    public void setAssessmentAmount(BigDecimal assessmentAmount) {
        this.assessmentAmount = assessmentAmount;
    }

    public AgencyStatusType getAgencyStatus() {
        return agencyStatus;
    }

    public void setAgencyStatus(AgencyStatusType agencyStatus) {
        this.agencyStatus = agencyStatus;
    }

    public byte[] getAssessmentNotice() {
        return assessmentNotice;
    }

    public void setAssessmentNotice(byte[] assessmentNotice) {
        this.assessmentNotice = assessmentNotice;
    }

    public String getAssessmentNoticeContentType() {
        return assessmentNoticeContentType;
    }

    public void setAssessmentNoticeContentType(String assessmentNoticeContentType) {
        this.assessmentNoticeContentType = assessmentNoticeContentType;
    }

    public Set<DealerDTO> getCorrespondents() {
        return correspondents;
    }

    public void setCorrespondents(Set<DealerDTO> correspondents) {
        this.correspondents = correspondents;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public DealerDTO getAssessor() {
        return assessor;
    }

    public void setAssessor(DealerDTO assessor) {
        this.assessor = assessor;
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
        if (!(o instanceof AgencyNoticeDTO)) {
            return false;
        }

        AgencyNoticeDTO agencyNoticeDTO = (AgencyNoticeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agencyNoticeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyNoticeDTO{" +
            "id=" + getId() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", referenceDate='" + getReferenceDate() + "'" +
            ", assessmentAmount=" + getAssessmentAmount() +
            ", agencyStatus='" + getAgencyStatus() + "'" +
            ", assessmentNotice='" + getAssessmentNotice() + "'" +
            ", correspondents=" + getCorrespondents() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", assessor=" + getAssessor() +
            ", placeholders=" + getPlaceholders() +
            ", businessDocuments=" + getBusinessDocuments() +
            "}";
    }
}
