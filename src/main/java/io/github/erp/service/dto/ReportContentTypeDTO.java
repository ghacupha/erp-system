package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ReportContentType} entity.
 */
public class ReportContentTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String reportTypeName;

    @NotNull
    private String reportFileExtension;

    private SystemContentTypeDTO systemContentType;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public String getReportFileExtension() {
        return reportFileExtension;
    }

    public void setReportFileExtension(String reportFileExtension) {
        this.reportFileExtension = reportFileExtension;
    }

    public SystemContentTypeDTO getSystemContentType() {
        return systemContentType;
    }

    public void setSystemContentType(SystemContentTypeDTO systemContentType) {
        this.systemContentType = systemContentType;
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
        if (!(o instanceof ReportContentTypeDTO)) {
            return false;
        }

        ReportContentTypeDTO reportContentTypeDTO = (ReportContentTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportContentTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportContentTypeDTO{" +
            "id=" + getId() +
            ", reportTypeName='" + getReportTypeName() + "'" +
            ", reportFileExtension='" + getReportFileExtension() + "'" +
            ", systemContentType=" + getSystemContentType() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
