package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AgencyNotice} entity.
 */
public class AgencyNoticeDTO implements Serializable {

    private Long id;

    @NotNull
    private String referenceNumber;

    private LocalDate referenceDate;

    private String taxCode;

    @NotNull
    private BigDecimal assessmentAmount;

    @NotNull
    private Boolean agencyStatus;

    private Set<DealerDTO> correspondents = new HashSet<>();

    private SettlementCurrencyDTO settlementCurrency;

    private DealerDTO assessor;

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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getAssessmentAmount() {
        return assessmentAmount;
    }

    public void setAssessmentAmount(BigDecimal assessmentAmount) {
        this.assessmentAmount = assessmentAmount;
    }

    public Boolean getAgencyStatus() {
        return agencyStatus;
    }

    public void setAgencyStatus(Boolean agencyStatus) {
        this.agencyStatus = agencyStatus;
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
            ", taxCode='" + getTaxCode() + "'" +
            ", assessmentAmount=" + getAssessmentAmount() +
            ", agencyStatus='" + getAgencyStatus() + "'" +
            ", correspondents=" + getCorrespondents() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", assessor=" + getAssessor() +
            "}";
    }
}
