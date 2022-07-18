package io.github.erp.domain;

/*-
 * Erp System - Mark II No 20 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.AgencyStatusType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AgencyNotice.
 */
@Entity
@Table(name = "agency_notice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "agencynotice")
public class AgencyNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reference_number", nullable = false, unique = true)
    private String referenceNumber;

    @Column(name = "reference_date")
    private LocalDate referenceDate;

    @NotNull
    @Column(name = "assessment_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal assessmentAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "agency_status", nullable = false)
    private AgencyStatusType agencyStatus;

    @Lob
    @Column(name = "assessment_notice")
    private byte[] assessmentNotice;

    @Column(name = "assessment_notice_content_type")
    private String assessmentNoticeContentType;

    @ManyToMany
    @JoinTable(
        name = "rel_agency_notice__correspondents",
        joinColumns = @JoinColumn(name = "agency_notice_id"),
        inverseJoinColumns = @JoinColumn(name = "correspondents_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> correspondents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer assessor;

    @ManyToMany
    @JoinTable(
        name = "rel_agency_notice__placeholder",
        joinColumns = @JoinColumn(name = "agency_notice_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgencyNotice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public AgencyNotice referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getReferenceDate() {
        return this.referenceDate;
    }

    public AgencyNotice referenceDate(LocalDate referenceDate) {
        this.setReferenceDate(referenceDate);
        return this;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public BigDecimal getAssessmentAmount() {
        return this.assessmentAmount;
    }

    public AgencyNotice assessmentAmount(BigDecimal assessmentAmount) {
        this.setAssessmentAmount(assessmentAmount);
        return this;
    }

    public void setAssessmentAmount(BigDecimal assessmentAmount) {
        this.assessmentAmount = assessmentAmount;
    }

    public AgencyStatusType getAgencyStatus() {
        return this.agencyStatus;
    }

    public AgencyNotice agencyStatus(AgencyStatusType agencyStatus) {
        this.setAgencyStatus(agencyStatus);
        return this;
    }

    public void setAgencyStatus(AgencyStatusType agencyStatus) {
        this.agencyStatus = agencyStatus;
    }

    public byte[] getAssessmentNotice() {
        return this.assessmentNotice;
    }

    public AgencyNotice assessmentNotice(byte[] assessmentNotice) {
        this.setAssessmentNotice(assessmentNotice);
        return this;
    }

    public void setAssessmentNotice(byte[] assessmentNotice) {
        this.assessmentNotice = assessmentNotice;
    }

    public String getAssessmentNoticeContentType() {
        return this.assessmentNoticeContentType;
    }

    public AgencyNotice assessmentNoticeContentType(String assessmentNoticeContentType) {
        this.assessmentNoticeContentType = assessmentNoticeContentType;
        return this;
    }

    public void setAssessmentNoticeContentType(String assessmentNoticeContentType) {
        this.assessmentNoticeContentType = assessmentNoticeContentType;
    }

    public Set<Dealer> getCorrespondents() {
        return this.correspondents;
    }

    public void setCorrespondents(Set<Dealer> dealers) {
        this.correspondents = dealers;
    }

    public AgencyNotice correspondents(Set<Dealer> dealers) {
        this.setCorrespondents(dealers);
        return this;
    }

    public AgencyNotice addCorrespondents(Dealer dealer) {
        this.correspondents.add(dealer);
        return this;
    }

    public AgencyNotice removeCorrespondents(Dealer dealer) {
        this.correspondents.remove(dealer);
        return this;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public AgencyNotice settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public Dealer getAssessor() {
        return this.assessor;
    }

    public void setAssessor(Dealer dealer) {
        this.assessor = dealer;
    }

    public AgencyNotice assessor(Dealer dealer) {
        this.setAssessor(dealer);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AgencyNotice placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AgencyNotice addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AgencyNotice removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgencyNotice)) {
            return false;
        }
        return id != null && id.equals(((AgencyNotice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyNotice{" +
            "id=" + getId() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", referenceDate='" + getReferenceDate() + "'" +
            ", assessmentAmount=" + getAssessmentAmount() +
            ", agencyStatus='" + getAgencyStatus() + "'" +
            ", assessmentNotice='" + getAssessmentNotice() + "'" +
            ", assessmentNoticeContentType='" + getAssessmentNoticeContentType() + "'" +
            "}";
    }
}
