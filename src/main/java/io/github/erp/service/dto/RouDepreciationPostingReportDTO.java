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
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RouDepreciationPostingReport} entity.
 */
public class RouDepreciationPostingReportDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID requestId;

    private ZonedDateTime timeOfRequest;

    private Boolean reportIsCompiled;

    private String fileChecksum;

    private Boolean tampered;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;
    private FiscalMonthDTO fiscalMonth;

    private ApplicationUserDTO requestedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public ZonedDateTime getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public Boolean getReportIsCompiled() {
        return reportIsCompiled;
    }

    public void setReportIsCompiled(Boolean reportIsCompiled) {
        this.reportIsCompiled = reportIsCompiled;
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

    public FiscalMonthDTO getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonthDTO fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
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
        if (!(o instanceof RouDepreciationPostingReportDTO)) {
            return false;
        }

        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = (RouDepreciationPostingReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouDepreciationPostingReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationPostingReportDTO{" +
            "id=" + getId() +
            ", requestId='" + getRequestId() + "'" +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", reportIsCompiled='" + getReportIsCompiled() + "'" +
            ", fileChecksum='" + getFileChecksum() + "'" +
            ", tampered='" + getTampered() + "'" +
            ", filename='" + getFilename() + "'" +
            ", reportParameters='" + getReportParameters() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", fiscalMonth=" + getFiscalMonth() +
            ", requestedBy=" + getRequestedBy() +
            "}";
    }
}
