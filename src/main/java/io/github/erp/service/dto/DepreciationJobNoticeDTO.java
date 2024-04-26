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
