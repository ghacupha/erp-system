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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ReportTemplate} entity.
 */
public class ReportTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    private String catalogueNumber;

    @Lob
    private String description;

    @Lob
    private byte[] notes;

    private String notesContentType;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    @Lob
    private byte[] compileReportFile;

    private String compileReportFileContentType;
    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogueNumber() {
        return catalogueNumber;
    }

    public void setCatalogueNumber(String catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getNotes() {
        return notes;
    }

    public void setNotes(byte[] notes) {
        this.notes = notes;
    }

    public String getNotesContentType() {
        return notesContentType;
    }

    public void setNotesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
    }

    public byte[] getReportFile() {
        return reportFile;
    }

    public void setReportFile(byte[] reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportFileContentType() {
        return reportFileContentType;
    }

    public void setReportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
    }

    public byte[] getCompileReportFile() {
        return compileReportFile;
    }

    public void setCompileReportFile(byte[] compileReportFile) {
        this.compileReportFile = compileReportFile;
    }

    public String getCompileReportFileContentType() {
        return compileReportFileContentType;
    }

    public void setCompileReportFileContentType(String compileReportFileContentType) {
        this.compileReportFileContentType = compileReportFileContentType;
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
        if (!(o instanceof ReportTemplateDTO)) {
            return false;
        }

        ReportTemplateDTO reportTemplateDTO = (ReportTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportTemplateDTO{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", compileReportFile='" + getCompileReportFile() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
