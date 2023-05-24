package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.4
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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.BusinessDocument} entity. This class is used
 * in {@link io.github.erp.web.rest.BusinessDocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /business-documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusinessDocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentTitle;

    private StringFilter description;

    private UUIDFilter documentSerial;

    private ZonedDateTimeFilter lastModified;

    private StringFilter attachmentFilePath;

    private StringFilter documentFileContentType;

    private BooleanFilter fileTampered;

    private StringFilter documentFileChecksum;

    private LongFilter createdById;

    private LongFilter lastModifiedById;

    private LongFilter originatingDepartmentId;

    private LongFilter applicationMappingsId;

    private LongFilter placeholderId;

    private LongFilter fileChecksumAlgorithmId;

    private LongFilter securityClearanceId;

    private Boolean distinct;

    public BusinessDocumentCriteria() {}

    public BusinessDocumentCriteria(BusinessDocumentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.documentTitle = other.documentTitle == null ? null : other.documentTitle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.documentSerial = other.documentSerial == null ? null : other.documentSerial.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.attachmentFilePath = other.attachmentFilePath == null ? null : other.attachmentFilePath.copy();
        this.documentFileContentType = other.documentFileContentType == null ? null : other.documentFileContentType.copy();
        this.fileTampered = other.fileTampered == null ? null : other.fileTampered.copy();
        this.documentFileChecksum = other.documentFileChecksum == null ? null : other.documentFileChecksum.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.lastModifiedById = other.lastModifiedById == null ? null : other.lastModifiedById.copy();
        this.originatingDepartmentId = other.originatingDepartmentId == null ? null : other.originatingDepartmentId.copy();
        this.applicationMappingsId = other.applicationMappingsId == null ? null : other.applicationMappingsId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.fileChecksumAlgorithmId = other.fileChecksumAlgorithmId == null ? null : other.fileChecksumAlgorithmId.copy();
        this.securityClearanceId = other.securityClearanceId == null ? null : other.securityClearanceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusinessDocumentCriteria copy() {
        return new BusinessDocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDocumentTitle() {
        return documentTitle;
    }

    public StringFilter documentTitle() {
        if (documentTitle == null) {
            documentTitle = new StringFilter();
        }
        return documentTitle;
    }

    public void setDocumentTitle(StringFilter documentTitle) {
        this.documentTitle = documentTitle;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public UUIDFilter getDocumentSerial() {
        return documentSerial;
    }

    public UUIDFilter documentSerial() {
        if (documentSerial == null) {
            documentSerial = new UUIDFilter();
        }
        return documentSerial;
    }

    public void setDocumentSerial(UUIDFilter documentSerial) {
        this.documentSerial = documentSerial;
    }

    public ZonedDateTimeFilter getLastModified() {
        return lastModified;
    }

    public ZonedDateTimeFilter lastModified() {
        if (lastModified == null) {
            lastModified = new ZonedDateTimeFilter();
        }
        return lastModified;
    }

    public void setLastModified(ZonedDateTimeFilter lastModified) {
        this.lastModified = lastModified;
    }

    public StringFilter getAttachmentFilePath() {
        return attachmentFilePath;
    }

    public StringFilter attachmentFilePath() {
        if (attachmentFilePath == null) {
            attachmentFilePath = new StringFilter();
        }
        return attachmentFilePath;
    }

    public void setAttachmentFilePath(StringFilter attachmentFilePath) {
        this.attachmentFilePath = attachmentFilePath;
    }

    public StringFilter getDocumentFileContentType() {
        return documentFileContentType;
    }

    public StringFilter documentFileContentType() {
        if (documentFileContentType == null) {
            documentFileContentType = new StringFilter();
        }
        return documentFileContentType;
    }

    public void setDocumentFileContentType(StringFilter documentFileContentType) {
        this.documentFileContentType = documentFileContentType;
    }

    public BooleanFilter getFileTampered() {
        return fileTampered;
    }

    public BooleanFilter fileTampered() {
        if (fileTampered == null) {
            fileTampered = new BooleanFilter();
        }
        return fileTampered;
    }

    public void setFileTampered(BooleanFilter fileTampered) {
        this.fileTampered = fileTampered;
    }

    public StringFilter getDocumentFileChecksum() {
        return documentFileChecksum;
    }

    public StringFilter documentFileChecksum() {
        if (documentFileChecksum == null) {
            documentFileChecksum = new StringFilter();
        }
        return documentFileChecksum;
    }

    public void setDocumentFileChecksum(StringFilter documentFileChecksum) {
        this.documentFileChecksum = documentFileChecksum;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getLastModifiedById() {
        return lastModifiedById;
    }

    public LongFilter lastModifiedById() {
        if (lastModifiedById == null) {
            lastModifiedById = new LongFilter();
        }
        return lastModifiedById;
    }

    public void setLastModifiedById(LongFilter lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public LongFilter getOriginatingDepartmentId() {
        return originatingDepartmentId;
    }

    public LongFilter originatingDepartmentId() {
        if (originatingDepartmentId == null) {
            originatingDepartmentId = new LongFilter();
        }
        return originatingDepartmentId;
    }

    public void setOriginatingDepartmentId(LongFilter originatingDepartmentId) {
        this.originatingDepartmentId = originatingDepartmentId;
    }

    public LongFilter getApplicationMappingsId() {
        return applicationMappingsId;
    }

    public LongFilter applicationMappingsId() {
        if (applicationMappingsId == null) {
            applicationMappingsId = new LongFilter();
        }
        return applicationMappingsId;
    }

    public void setApplicationMappingsId(LongFilter applicationMappingsId) {
        this.applicationMappingsId = applicationMappingsId;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getFileChecksumAlgorithmId() {
        return fileChecksumAlgorithmId;
    }

    public LongFilter fileChecksumAlgorithmId() {
        if (fileChecksumAlgorithmId == null) {
            fileChecksumAlgorithmId = new LongFilter();
        }
        return fileChecksumAlgorithmId;
    }

    public void setFileChecksumAlgorithmId(LongFilter fileChecksumAlgorithmId) {
        this.fileChecksumAlgorithmId = fileChecksumAlgorithmId;
    }

    public LongFilter getSecurityClearanceId() {
        return securityClearanceId;
    }

    public LongFilter securityClearanceId() {
        if (securityClearanceId == null) {
            securityClearanceId = new LongFilter();
        }
        return securityClearanceId;
    }

    public void setSecurityClearanceId(LongFilter securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BusinessDocumentCriteria that = (BusinessDocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentTitle, that.documentTitle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(documentSerial, that.documentSerial) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(attachmentFilePath, that.attachmentFilePath) &&
            Objects.equals(documentFileContentType, that.documentFileContentType) &&
            Objects.equals(fileTampered, that.fileTampered) &&
            Objects.equals(documentFileChecksum, that.documentFileChecksum) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(lastModifiedById, that.lastModifiedById) &&
            Objects.equals(originatingDepartmentId, that.originatingDepartmentId) &&
            Objects.equals(applicationMappingsId, that.applicationMappingsId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(fileChecksumAlgorithmId, that.fileChecksumAlgorithmId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentTitle,
            description,
            documentSerial,
            lastModified,
            attachmentFilePath,
            documentFileContentType,
            fileTampered,
            documentFileChecksum,
            createdById,
            lastModifiedById,
            originatingDepartmentId,
            applicationMappingsId,
            placeholderId,
            fileChecksumAlgorithmId,
            securityClearanceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessDocumentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (documentTitle != null ? "documentTitle=" + documentTitle + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (documentSerial != null ? "documentSerial=" + documentSerial + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (attachmentFilePath != null ? "attachmentFilePath=" + attachmentFilePath + ", " : "") +
            (documentFileContentType != null ? "documentFileContentType=" + documentFileContentType + ", " : "") +
            (fileTampered != null ? "fileTampered=" + fileTampered + ", " : "") +
            (documentFileChecksum != null ? "documentFileChecksum=" + documentFileChecksum + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (lastModifiedById != null ? "lastModifiedById=" + lastModifiedById + ", " : "") +
            (originatingDepartmentId != null ? "originatingDepartmentId=" + originatingDepartmentId + ", " : "") +
            (applicationMappingsId != null ? "applicationMappingsId=" + applicationMappingsId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (fileChecksumAlgorithmId != null ? "fileChecksumAlgorithmId=" + fileChecksumAlgorithmId + ", " : "") +
            (securityClearanceId != null ? "securityClearanceId=" + securityClearanceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
