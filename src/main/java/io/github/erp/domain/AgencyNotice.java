package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "tax_code")
    private String taxCode;

    @NotNull
    @Column(name = "assessment_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal assessmentAmount;

    @NotNull
    @Column(name = "agency_status", nullable = false)
    private Boolean agencyStatus;

    @ManyToMany
    @JoinTable(
        name = "rel_agency_notice__correspondents",
        joinColumns = @JoinColumn(name = "agency_notice_id"),
        inverseJoinColumns = @JoinColumn(name = "correspondents_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> correspondents = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer assessor;

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

    public String getTaxCode() {
        return this.taxCode;
    }

    public AgencyNotice taxCode(String taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
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

    public Boolean getAgencyStatus() {
        return this.agencyStatus;
    }

    public AgencyNotice agencyStatus(Boolean agencyStatus) {
        this.setAgencyStatus(agencyStatus);
        return this;
    }

    public void setAgencyStatus(Boolean agencyStatus) {
        this.agencyStatus = agencyStatus;
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
            ", taxCode='" + getTaxCode() + "'" +
            ", assessmentAmount=" + getAssessmentAmount() +
            ", agencyStatus='" + getAgencyStatus() + "'" +
            "}";
    }
}
