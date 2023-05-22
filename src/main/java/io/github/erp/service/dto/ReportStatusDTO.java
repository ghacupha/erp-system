package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ReportStatus} entity.
 */
public class ReportStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String reportName;

    @NotNull
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
