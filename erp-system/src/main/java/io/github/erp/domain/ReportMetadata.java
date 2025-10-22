package io.github.erp.domain;

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
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Metadata describing dynamic ERP reports available to the client application.
 */
@Entity
@Table(name = "report_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportmetadata")
public class ReportMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "report_title", nullable = false, unique = true)
    private String reportTitle;

    @Column(name = "report_description", length = 2000)
    private String description;

    @NotBlank
    @Column(name = "module_name", nullable = false)
    private String module;

    @NotBlank
    @Column(name = "page_path", nullable = false, unique = true)
    private String pagePath;

    @NotBlank
    @Column(name = "backend_api", nullable = false)
    private String backendApi;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @NotNull
    @Column(name = "display_lease_period", nullable = false)
    private Boolean displayLeasePeriod = Boolean.FALSE;

    @NotNull
    @Column(name = "display_lease_contract", nullable = false)
    private Boolean displayLeaseContract = Boolean.FALSE;

    @Column(name = "lease_period_query_param")
    private String leasePeriodQueryParam;

    @Column(name = "lease_contract_query_param")
    private String leaseContractQueryParam;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportTitle() {
        return this.reportTitle;
    }

    public ReportMetadata reportTitle(String reportTitle) {
        this.setReportTitle(reportTitle);
        return this;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public ReportMetadata description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return this.module;
    }

    public ReportMetadata module(String module) {
        this.setModule(module);
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPagePath() {
        return this.pagePath;
    }

    public ReportMetadata pagePath(String pagePath) {
        this.setPagePath(pagePath);
        return this;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getBackendApi() {
        return this.backendApi;
    }

    public ReportMetadata backendApi(String backendApi) {
        this.setBackendApi(backendApi);
        return this;
    }

    public void setBackendApi(String backendApi) {
        this.backendApi = backendApi;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ReportMetadata active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDisplayLeasePeriod() {
        return this.displayLeasePeriod;
    }

    public ReportMetadata displayLeasePeriod(Boolean displayLeasePeriod) {
        this.setDisplayLeasePeriod(displayLeasePeriod);
        return this;
    }

    public void setDisplayLeasePeriod(Boolean displayLeasePeriod) {
        this.displayLeasePeriod = displayLeasePeriod;
    }

    public Boolean getDisplayLeaseContract() {
        return this.displayLeaseContract;
    }

    public ReportMetadata displayLeaseContract(Boolean displayLeaseContract) {
        this.setDisplayLeaseContract(displayLeaseContract);
        return this;
    }

    public void setDisplayLeaseContract(Boolean displayLeaseContract) {
        this.displayLeaseContract = displayLeaseContract;
    }

    public String getLeasePeriodQueryParam() {
        return this.leasePeriodQueryParam;
    }

    public ReportMetadata leasePeriodQueryParam(String leasePeriodQueryParam) {
        this.setLeasePeriodQueryParam(leasePeriodQueryParam);
        return this;
    }

    public void setLeasePeriodQueryParam(String leasePeriodQueryParam) {
        this.leasePeriodQueryParam = leasePeriodQueryParam;
    }

    public String getLeaseContractQueryParam() {
        return this.leaseContractQueryParam;
    }

    public ReportMetadata leaseContractQueryParam(String leaseContractQueryParam) {
        this.setLeaseContractQueryParam(leaseContractQueryParam);
        return this;
    }

    public void setLeaseContractQueryParam(String leaseContractQueryParam) {
        this.leaseContractQueryParam = leaseContractQueryParam;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportMetadata)) {
            return false;
        }
        return id != null && id.equals(((ReportMetadata) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportMetadata{" +
            "id=" + getId() +
            ", reportTitle='" + getReportTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", module='" + getModule() + "'" +
            ", pagePath='" + getPagePath() + "'" +
            ", backendApi='" + getBackendApi() + "'" +
            ", active=" + getActive() +
            ", displayLeasePeriod=" + getDisplayLeasePeriod() +
            ", displayLeaseContract=" + getDisplayLeaseContract() +
            ", leasePeriodQueryParam='" + getLeasePeriodQueryParam() + "'" +
            ", leaseContractQueryParam='" + getLeaseContractQueryParam() + "'" +
            "}";
    }
}
