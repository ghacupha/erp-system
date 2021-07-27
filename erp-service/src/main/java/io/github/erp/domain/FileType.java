package io.github.erp.domain;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import io.github.erp.domain.enumeration.FileMediumTypes;

import io.github.erp.domain.enumeration.FileModelType;

/**
 * A FileType.
 */
@Entity
@Table(name = "file_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "filetype")
public class FileType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "file_type_name", nullable = false, unique = true)
    private String fileTypeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_medium_type", nullable = false)
    private FileMediumTypes fileMediumType;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "file_template")
    private byte[] fileTemplate;

    @Column(name = "file_template_content_type")
    private String fileTemplateContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileModelType fileType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public FileType fileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
        return this;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public FileMediumTypes getFileMediumType() {
        return fileMediumType;
    }

    public FileType fileMediumType(FileMediumTypes fileMediumType) {
        this.fileMediumType = fileMediumType;
        return this;
    }

    public void setFileMediumType(FileMediumTypes fileMediumType) {
        this.fileMediumType = fileMediumType;
    }

    public String getDescription() {
        return description;
    }

    public FileType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileTemplate() {
        return fileTemplate;
    }

    public FileType fileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
        return this;
    }

    public void setFileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public String getFileTemplateContentType() {
        return fileTemplateContentType;
    }

    public FileType fileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
        return this;
    }

    public void setFileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
    }

    public FileModelType getFileType() {
        return fileType;
    }

    public FileType fileType(FileModelType fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(FileModelType fileType) {
        this.fileType = fileType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileType)) {
            return false;
        }
        return id != null && id.equals(((FileType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileType{" +
            "id=" + getId() +
            ", fileTypeName='" + getFileTypeName() + "'" +
            ", fileMediumType='" + getFileMediumType() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileTemplate='" + getFileTemplate() + "'" +
            ", fileTemplateContentType='" + getFileTemplateContentType() + "'" +
            ", fileType='" + getFileType() + "'" +
            "}";
    }
}
