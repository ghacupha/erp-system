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
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.WIPListReport} entity.
 */
public class WIPListReportDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime timeOfRequest;

    @NotNull
    private UUID requestId;

    private String fileChecksum;

    private Boolean tampered;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;
    private ApplicationUserDTO requestedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getFileChecksum() {
        return fileChecksum;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public Boolean getTampered() {
        return tampered;
    }

    public void setTampered(Boolean tampered) {
        this.tampered = tampered;
    }

    public UUID getFilename() {
        return filename;
    }

    public void setFilename(UUID filename) {
        this.filename = filename;
    }

    public String getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(String reportParameters) {
        this.reportParameters = reportParameters;
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

    public ApplicationUserDTO getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(ApplicationUserDTO requestedBy) {
        this.requestedBy = requestedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WIPListReportDTO)) {
            return false;
        }

        WIPListReportDTO wIPListReportDTO = (WIPListReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wIPListReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WIPListReportDTO{" +
            "id=" + getId() +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", requestId='" + getRequestId() + "'" +
            ", fileChecksum='" + getFileChecksum() + "'" +
            ", tampered='" + getTampered() + "'" +
            ", filename='" + getFilename() + "'" +
            ", reportParameters='" + getReportParameters() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", requestedBy=" + getRequestedBy() +
            "}";
    }
}
