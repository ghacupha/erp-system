package io.github.erp.domain;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.2.0
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.FileMediumTypes;
import io.github.erp.domain.enumeration.FileModelType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "id")
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, unique = true)
    private FileModelType fileType;

    @ManyToMany
    @JoinTable(
        name = "rel_file_type__placeholder",
        joinColumns = @JoinColumn(name = "file_type_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileTypeName() {
        return this.fileTypeName;
    }

    public FileType fileTypeName(String fileTypeName) {
        this.setFileTypeName(fileTypeName);
        return this;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public FileMediumTypes getFileMediumType() {
        return this.fileMediumType;
    }

    public FileType fileMediumType(FileMediumTypes fileMediumType) {
        this.setFileMediumType(fileMediumType);
        return this;
    }

    public void setFileMediumType(FileMediumTypes fileMediumType) {
        this.fileMediumType = fileMediumType;
    }

    public String getDescription() {
        return this.description;
    }

    public FileType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileTemplate() {
        return this.fileTemplate;
    }

    public FileType fileTemplate(byte[] fileTemplate) {
        this.setFileTemplate(fileTemplate);
        return this;
    }

    public void setFileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public String getFileTemplateContentType() {
        return this.fileTemplateContentType;
    }

    public FileType fileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
        return this;
    }

    public void setFileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
    }

    public FileModelType getFileType() {
        return this.fileType;
    }

    public FileType fileType(FileModelType fileType) {
        this.setFileType(fileType);
        return this;
    }

    public void setFileType(FileModelType fileType) {
        this.fileType = fileType;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public FileType placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public FileType addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public FileType removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
