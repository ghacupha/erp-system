package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link io.github.erp.domain.ReportMetadata} entity.
 */
public class ReportMetadataDTO implements Serializable {

    private Long id;

    @NotBlank
    private String reportTitle;

    private String description;

    @NotBlank
    private String module;

    @NotBlank
    private String pagePath;

    @NotBlank
    private String backendApi;

    @NotNull
    private Boolean active;

    private List<ReportFilterDefinitionDTO> filters = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getBackendApi() {
        return backendApi;
    }

    public void setBackendApi(String backendApi) {
        this.backendApi = backendApi;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ReportFilterDefinitionDTO> getFilters() {
        return filters;
    }

    public void setFilters(List<ReportFilterDefinitionDTO> filters) {
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportMetadataDTO)) {
            return false;
        }

        ReportMetadataDTO reportMetadataDTO = (ReportMetadataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportMetadataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportMetadataDTO{" +
            "id=" + getId() +
            ", reportTitle='" + getReportTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", module='" + getModule() + "'" +
            ", pagePath='" + getPagePath() + "'" +
            ", backendApi='" + getBackendApi() + "'" +
            ", active=" + getActive() +
            ", filters=" + getFilters() +
            "}";
    }
}
