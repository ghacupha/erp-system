package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.DepreciationNoticeStatusType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A DepreciationJobNotice.
 */
@Entity
@Table(name = "depreciation_job_notice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationjobnotice")
public class DepreciationJobNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "event_narrative", nullable = false)
    private String eventNarrative;

    @NotNull
    @Column(name = "event_time_stamp", nullable = false)
    private ZonedDateTime eventTimeStamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_notice_status", nullable = false)
    private DepreciationNoticeStatusType depreciationNoticeStatus;

    @Column(name = "source_module")
    private String sourceModule;

    @Column(name = "source_entity")
    private String sourceEntity;

    @Column(name = "error_code")
    private String errorCode;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "user_action")
    private String userAction;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "technical_details")
    private String technicalDetails;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "depreciationPeriod" }, allowSetters = true)
    private DepreciationJob depreciationJob;

    @ManyToOne
    @JsonIgnoreProperties(value = { "depreciationJob" }, allowSetters = true)
    private DepreciationBatchSequence depreciationBatchSequence;

    @ManyToOne
    @JsonIgnoreProperties(value = { "previousPeriod", "createdBy", "fiscalYear", "fiscalMonth", "fiscalQuarter" }, allowSetters = true)
    private DepreciationPeriod depreciationPeriod;

    @ManyToMany
    @JoinTable(
        name = "rel_depreciation_job_notice__placeholder",
        joinColumns = @JoinColumn(name = "depreciation_job_notice_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_depreciation_job_notice__universally_unique_mapping",
        joinColumns = @JoinColumn(name = "depreciation_job_notice_id"),
        inverseJoinColumns = @JoinColumn(name = "universally_unique_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> universallyUniqueMappings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser superintended;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepreciationJobNotice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventNarrative() {
        return this.eventNarrative;
    }

    public DepreciationJobNotice eventNarrative(String eventNarrative) {
        this.setEventNarrative(eventNarrative);
        return this;
    }

    public void setEventNarrative(String eventNarrative) {
        this.eventNarrative = eventNarrative;
    }

    public ZonedDateTime getEventTimeStamp() {
        return this.eventTimeStamp;
    }

    public DepreciationJobNotice eventTimeStamp(ZonedDateTime eventTimeStamp) {
        this.setEventTimeStamp(eventTimeStamp);
        return this;
    }

    public void setEventTimeStamp(ZonedDateTime eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
    }

    public DepreciationNoticeStatusType getDepreciationNoticeStatus() {
        return this.depreciationNoticeStatus;
    }

    public DepreciationJobNotice depreciationNoticeStatus(DepreciationNoticeStatusType depreciationNoticeStatus) {
        this.setDepreciationNoticeStatus(depreciationNoticeStatus);
        return this;
    }

    public void setDepreciationNoticeStatus(DepreciationNoticeStatusType depreciationNoticeStatus) {
        this.depreciationNoticeStatus = depreciationNoticeStatus;
    }

    public String getSourceModule() {
        return this.sourceModule;
    }

    public DepreciationJobNotice sourceModule(String sourceModule) {
        this.setSourceModule(sourceModule);
        return this;
    }

    public void setSourceModule(String sourceModule) {
        this.sourceModule = sourceModule;
    }

    public String getSourceEntity() {
        return this.sourceEntity;
    }

    public DepreciationJobNotice sourceEntity(String sourceEntity) {
        this.setSourceEntity(sourceEntity);
        return this;
    }

    public void setSourceEntity(String sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public DepreciationJobNotice errorCode(String errorCode) {
        this.setErrorCode(errorCode);
        return this;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public DepreciationJobNotice errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUserAction() {
        return this.userAction;
    }

    public DepreciationJobNotice userAction(String userAction) {
        this.setUserAction(userAction);
        return this;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getTechnicalDetails() {
        return this.technicalDetails;
    }

    public DepreciationJobNotice technicalDetails(String technicalDetails) {
        this.setTechnicalDetails(technicalDetails);
        return this;
    }

    public void setTechnicalDetails(String technicalDetails) {
        this.technicalDetails = technicalDetails;
    }

    public DepreciationJob getDepreciationJob() {
        return this.depreciationJob;
    }

    public void setDepreciationJob(DepreciationJob depreciationJob) {
        this.depreciationJob = depreciationJob;
    }

    public DepreciationJobNotice depreciationJob(DepreciationJob depreciationJob) {
        this.setDepreciationJob(depreciationJob);
        return this;
    }

    public DepreciationBatchSequence getDepreciationBatchSequence() {
        return this.depreciationBatchSequence;
    }

    public void setDepreciationBatchSequence(DepreciationBatchSequence depreciationBatchSequence) {
        this.depreciationBatchSequence = depreciationBatchSequence;
    }

    public DepreciationJobNotice depreciationBatchSequence(DepreciationBatchSequence depreciationBatchSequence) {
        this.setDepreciationBatchSequence(depreciationBatchSequence);
        return this;
    }

    public DepreciationPeriod getDepreciationPeriod() {
        return this.depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public DepreciationJobNotice depreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.setDepreciationPeriod(depreciationPeriod);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public DepreciationJobNotice placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public DepreciationJobNotice addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public DepreciationJobNotice removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getUniversallyUniqueMappings() {
        return this.universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public DepreciationJobNotice universallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setUniversallyUniqueMappings(universallyUniqueMappings);
        return this;
    }

    public DepreciationJobNotice addUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.add(universallyUniqueMapping);
        return this;
    }

    public DepreciationJobNotice removeUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.remove(universallyUniqueMapping);
        return this;
    }

    public ApplicationUser getSuperintended() {
        return this.superintended;
    }

    public void setSuperintended(ApplicationUser applicationUser) {
        this.superintended = applicationUser;
    }

    public DepreciationJobNotice superintended(ApplicationUser applicationUser) {
        this.setSuperintended(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationJobNotice)) {
            return false;
        }
        return id != null && id.equals(((DepreciationJobNotice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationJobNotice{" +
            "id=" + getId() +
            ", eventNarrative='" + getEventNarrative() + "'" +
            ", eventTimeStamp='" + getEventTimeStamp() + "'" +
            ", depreciationNoticeStatus='" + getDepreciationNoticeStatus() + "'" +
            ", sourceModule='" + getSourceModule() + "'" +
            ", sourceEntity='" + getSourceEntity() + "'" +
            ", errorCode='" + getErrorCode() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", userAction='" + getUserAction() + "'" +
            ", technicalDetails='" + getTechnicalDetails() + "'" +
            "}";
    }
}
