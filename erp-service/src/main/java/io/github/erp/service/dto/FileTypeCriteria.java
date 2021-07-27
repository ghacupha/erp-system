package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.erp.domain.enumeration.fileMediumTypes;
import io.github.erp.domain.enumeration.fileModelType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

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
     * Class for filtering fileMediumTypes
     */
    public static class fileMediumTypesFilter extends Filter<fileMediumTypes> {

        public fileMediumTypesFilter() {
        }

        public fileMediumTypesFilter(fileMediumTypesFilter filter) {
            super(filter);
        }

        @Override
        public fileMediumTypesFilter copy() {
            return new fileMediumTypesFilter(this);
        }

    }
    /**
     * Class for filtering fileModelType
     */
    public static class fileModelTypeFilter extends Filter<fileModelType> {

        public fileModelTypeFilter() {
        }

        public fileModelTypeFilter(fileModelTypeFilter filter) {
            super(filter);
        }

        @Override
        public fileModelTypeFilter copy() {
            return new fileModelTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileTypeName;

    private fileMediumTypesFilter fileMediumType;

    private StringFilter description;

    private fileModelTypeFilter fileType;

    public FileTypeCriteria() {
    }

    public FileTypeCriteria(FileTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileTypeName = other.fileTypeName == null ? null : other.fileTypeName.copy();
        this.fileMediumType = other.fileMediumType == null ? null : other.fileMediumType.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.fileType = other.fileType == null ? null : other.fileType.copy();
    }

    @Override
    public FileTypeCriteria copy() {
        return new FileTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(StringFilter fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public fileMediumTypesFilter getFileMediumType() {
        return fileMediumType;
    }

    public void setFileMediumType(fileMediumTypesFilter fileMediumType) {
        this.fileMediumType = fileMediumType;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public fileModelTypeFilter getFileType() {
        return fileType;
    }

    public void setFileType(fileModelTypeFilter fileType) {
        this.fileType = fileType;
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
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fileTypeName, that.fileTypeName) &&
            Objects.equals(fileMediumType, that.fileMediumType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fileTypeName,
        fileMediumType,
        description,
        fileType
        );
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
            "}";
    }

}
