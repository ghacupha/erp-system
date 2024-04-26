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
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetAdditionsReport} entity.
 */
public class AssetAdditionsReportDTO implements Serializable {

    private Long id;

    private LocalDate timeOfRequest;

    private LocalDate reportStartDate;

    private LocalDate reportEndDate;

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

    public LocalDate getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(LocalDate timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public LocalDate getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(LocalDate reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public LocalDate getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(LocalDate reportEndDate) {
        this.reportEndDate = reportEndDate;
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
        if (!(o instanceof AssetAdditionsReportDTO)) {
            return false;
        }

        AssetAdditionsReportDTO assetAdditionsReportDTO = (AssetAdditionsReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetAdditionsReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAdditionsReportDTO{" +
            "id=" + getId() +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", reportStartDate='" + getReportStartDate() + "'" +
            ", reportEndDate='" + getReportEndDate() + "'" +
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
