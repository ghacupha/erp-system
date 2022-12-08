package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
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

    @NotNull
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
