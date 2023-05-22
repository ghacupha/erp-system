package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
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
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.BusinessDocument} entity.
 */
public class BusinessDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String documentTitle;

    private String description;

    @NotNull
    private UUID documentSerial;

    private ZonedDateTime lastModified;

    @NotNull
    private String attachmentFilePath;

    @NotNull
    private String documentFileContentType;

    private Boolean fileTampered;

    @NotNull
    private String documentFileChecksum;

    private ApplicationUserDTO createdBy;

    private ApplicationUserDTO lastModifiedBy;

    private DealerDTO originatingDepartment;

    private Set<UniversallyUniqueMappingDTO> applicationMappings = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private AlgorithmDTO fileChecksumAlgorithm;

    private SecurityClearanceDTO securityClearance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getDocumentSerial() {
        return documentSerial;
    }

    public void setDocumentSerial(UUID documentSerial) {
        this.documentSerial = documentSerial;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getAttachmentFilePath() {
        return attachmentFilePath;
    }

    public void setAttachmentFilePath(String attachmentFilePath) {
        this.attachmentFilePath = attachmentFilePath;
    }

    public String getDocumentFileContentType() {
        return documentFileContentType;
    }

    public void setDocumentFileContentType(String documentFileContentType) {
        this.documentFileContentType = documentFileContentType;
    }

    public Boolean getFileTampered() {
        return fileTampered;
    }

    public void setFileTampered(Boolean fileTampered) {
        this.fileTampered = fileTampered;
    }

    public String getDocumentFileChecksum() {
        return documentFileChecksum;
    }

    public void setDocumentFileChecksum(String documentFileChecksum) {
        this.documentFileChecksum = documentFileChecksum;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public ApplicationUserDTO getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(ApplicationUserDTO lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public DealerDTO getOriginatingDepartment() {
        return originatingDepartment;
    }

    public void setOriginatingDepartment(DealerDTO originatingDepartment) {
        this.originatingDepartment = originatingDepartment;
    }

    public Set<UniversallyUniqueMappingDTO> getApplicationMappings() {
        return applicationMappings;
    }

    public void setApplicationMappings(Set<UniversallyUniqueMappingDTO> applicationMappings) {
        this.applicationMappings = applicationMappings;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public AlgorithmDTO getFileChecksumAlgorithm() {
        return fileChecksumAlgorithm;
    }

    public void setFileChecksumAlgorithm(AlgorithmDTO fileChecksumAlgorithm) {
        this.fileChecksumAlgorithm = fileChecksumAlgorithm;
    }

    public SecurityClearanceDTO getSecurityClearance() {
        return securityClearance;
    }

    public void setSecurityClearance(SecurityClearanceDTO securityClearance) {
        this.securityClearance = securityClearance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessDocumentDTO)) {
            return false;
        }

        BusinessDocumentDTO businessDocumentDTO = (BusinessDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessDocumentDTO{" +
            "id=" + getId() +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentSerial='" + getDocumentSerial() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", attachmentFilePath='" + getAttachmentFilePath() + "'" +
            ", documentFileContentType='" + getDocumentFileContentType() + "'" +
            ", fileTampered='" + getFileTampered() + "'" +
            ", documentFileChecksum='" + getDocumentFileChecksum() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", originatingDepartment=" + getOriginatingDepartment() +
            ", applicationMappings=" + getApplicationMappings() +
            ", placeholders=" + getPlaceholders() +
            ", fileChecksumAlgorithm=" + getFileChecksumAlgorithm() +
            ", securityClearance=" + getSecurityClearance() +
            "}";
    }
}
