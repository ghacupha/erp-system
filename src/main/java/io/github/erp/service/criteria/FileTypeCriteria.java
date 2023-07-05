package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.0
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
import io.github.erp.domain.enumeration.FileMediumTypes;
import io.github.erp.domain.enumeration.FileModelType;
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

/**
 * Criteria class for the {@link io.github.erp.domain.FileType} entity. This class is used
 * in {@link io.github.erp.web.rest.FileTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /file-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FileTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FileMediumTypes
     */
    public static class FileMediumTypesFilter extends Filter<FileMediumTypes> {

        public FileMediumTypesFilter() {}

        public FileMediumTypesFilter(FileMediumTypesFilter filter) {
            super(filter);
        }

        @Override
        public FileMediumTypesFilter copy() {
            return new FileMediumTypesFilter(this);
        }
    }

    /**
     * Class for filtering FileModelType
     */
    public static class FileModelTypeFilter extends Filter<FileModelType> {

        public FileModelTypeFilter() {}

        public FileModelTypeFilter(FileModelTypeFilter filter) {
            super(filter);
        }

        @Override
        public FileModelTypeFilter copy() {
            return new FileModelTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileTypeName;

    private FileMediumTypesFilter fileMediumType;

    private StringFilter description;

    private FileModelTypeFilter fileType;

    private LongFilter placeholderId;

    private Boolean distinct;

    public FileTypeCriteria() {}

    public FileTypeCriteria(FileTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileTypeName = other.fileTypeName == null ? null : other.fileTypeName.copy();
        this.fileMediumType = other.fileMediumType == null ? null : other.fileMediumType.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.fileType = other.fileType == null ? null : other.fileType.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FileTypeCriteria copy() {
        return new FileTypeCriteria(this);
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

    public StringFilter getFileTypeName() {
        return fileTypeName;
    }

    public StringFilter fileTypeName() {
        if (fileTypeName == null) {
            fileTypeName = new StringFilter();
        }
        return fileTypeName;
    }

    public void setFileTypeName(StringFilter fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public FileMediumTypesFilter getFileMediumType() {
        return fileMediumType;
    }

    public FileMediumTypesFilter fileMediumType() {
        if (fileMediumType == null) {
            fileMediumType = new FileMediumTypesFilter();
        }
        return fileMediumType;
    }

    public void setFileMediumType(FileMediumTypesFilter fileMediumType) {
        this.fileMediumType = fileMediumType;
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

    public FileModelTypeFilter getFileType() {
        return fileType;
    }

    public FileModelTypeFilter fileType() {
        if (fileType == null) {
            fileType = new FileModelTypeFilter();
        }
        return fileType;
    }

    public void setFileType(FileModelTypeFilter fileType) {
        this.fileType = fileType;
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
        final FileTypeCriteria that = (FileTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fileTypeName, that.fileTypeName) &&
            Objects.equals(fileMediumType, that.fileMediumType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fileType, that.fileType) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileTypeName, fileMediumType, description, fileType, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fileTypeName != null ? "fileTypeName=" + fileTypeName + ", " : "") +
            (fileMediumType != null ? "fileMediumType=" + fileMediumType + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (fileType != null ? "fileType=" + fileType + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
