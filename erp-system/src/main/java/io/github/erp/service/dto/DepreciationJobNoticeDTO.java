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

import io.github.erp.domain.enumeration.DepreciationNoticeStatusType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationJobNotice} entity.
 */
public class DepreciationJobNoticeDTO implements Serializable {

    private Long id;

    private String eventNarrative;

    private ZonedDateTime eventTimeStamp;

    private DepreciationNoticeStatusType depreciationNoticeStatus;

    private String sourceModule;

    private String sourceEntity;

    private String errorCode;

    @Lob
    private String errorMessage;

    private String userAction;

    @Lob
    private String technicalDetails;

    private DepreciationJobDTO depreciationJob;

    private DepreciationBatchSequenceDTO depreciationBatchSequence;

    private DepreciationPeriodDTO depreciationPeriod;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventNarrative() {
        return eventNarrative;
    }

    public void setEventNarrative(String eventNarrative) {
        this.eventNarrative = eventNarrative;
    }

    public ZonedDateTime getEventTimeStamp() {
        return eventTimeStamp;
    }

    public void setEventTimeStamp(ZonedDateTime eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
    }

    public DepreciationNoticeStatusType getDepreciationNoticeStatus() {
        return depreciationNoticeStatus;
    }

    public void setDepreciationNoticeStatus(DepreciationNoticeStatusType depreciationNoticeStatus) {
        this.depreciationNoticeStatus = depreciationNoticeStatus;
    }

    public String getSourceModule() {
        return sourceModule;
    }

    public void setSourceModule(String sourceModule) {
        this.sourceModule = sourceModule;
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public void setSourceEntity(String sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getTechnicalDetails() {
        return technicalDetails;
    }

    public void setTechnicalDetails(String technicalDetails) {
        this.technicalDetails = technicalDetails;
    }

    public DepreciationJobDTO getDepreciationJob() {
        return depreciationJob;
    }

    public void setDepreciationJob(DepreciationJobDTO depreciationJob) {
        this.depreciationJob = depreciationJob;
    }

    public DepreciationBatchSequenceDTO getDepreciationBatchSequence() {
        return depreciationBatchSequence;
    }

    public void setDepreciationBatchSequence(DepreciationBatchSequenceDTO depreciationBatchSequence) {
        this.depreciationBatchSequence = depreciationBatchSequence;
    }

    public DepreciationPeriodDTO getDepreciationPeriod() {
        return depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriodDTO depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationJobNoticeDTO)) {
            return false;
        }

        DepreciationJobNoticeDTO depreciationJobNoticeDTO = (DepreciationJobNoticeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationJobNoticeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationJobNoticeDTO{" +
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
            ", depreciationJob=" + getDepreciationJob() +
            ", depreciationBatchSequence=" + getDepreciationBatchSequence() +
            ", depreciationPeriod=" + getDepreciationPeriod() +
            ", placeholders=" + getPlaceholders() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            "}";
    }
}
