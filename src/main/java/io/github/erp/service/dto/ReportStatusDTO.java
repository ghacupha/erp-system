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
import java.util.UUID;

/**
 * A DTO for the {@link io.github.erp.domain.ReportStatus} entity.
 */
public class ReportStatusDTO implements Serializable {

    private Long id;

    private String reportName;

    private UUID reportId;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private ProcessStatusDTO processStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public UUID getReportId() {
        return reportId;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public ProcessStatusDTO getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatusDTO processStatus) {
        this.processStatus = processStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportStatusDTO)) {
            return false;
        }

        ReportStatusDTO reportStatusDTO = (ReportStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportStatusDTO{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportId='" + getReportId() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", processStatus=" + getProcessStatus() +
            "}";
    }
}
