package io.github.erp.service.dto;

import io.github.erp.domain.enumeration.FileMediumTypes;
import io.github.erp.domain.enumeration.FileModelType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.FileType} entity.
 */
public class FileTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String fileTypeName;

    @NotNull
    private FileMediumTypes fileMediumType;

    private String description;

    @Lob
    private byte[] fileTemplate;

    private String fileTemplateContentType;
    private FileModelType fileType;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public FileMediumTypes getFileMediumType() {
        return fileMediumType;
    }

    public void setFileMediumType(FileMediumTypes fileMediumType) {
        this.fileMediumType = fileMediumType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public String getFileTemplateContentType() {
        return fileTemplateContentType;
    }

    public void setFileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
    }

    public FileModelType getFileType() {
        return fileType;
    }

    public void setFileType(FileModelType fileType) {
        this.fileType = fileType;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileTypeDTO)) {
            return false;
        }

        FileTypeDTO fileTypeDTO = (FileTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileTypeDTO{" +
            "id=" + getId() +
            ", fileTypeName='" + getFileTypeName() + "'" +
            ", fileMediumType='" + getFileMediumType() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileTemplate='" + getFileTemplate() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
