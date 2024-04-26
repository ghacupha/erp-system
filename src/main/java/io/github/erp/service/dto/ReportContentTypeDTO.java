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
