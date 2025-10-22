package io.github.erp.service.criteria;

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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.ReportMetadata} entity.
 * This class is used in {@link io.github.erp.web.rest.ReportMetadataResource}
 * to receive all the possible filtering options from the Http GET request parameters.
 */
public class ReportMetadataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reportTitle;

    private StringFilter description;

    private StringFilter module;

    private StringFilter pagePath;

    private StringFilter backendApi;

    private BooleanFilter active;

    private BooleanFilter displayLeasePeriod;

    private BooleanFilter displayLeaseContract;

    private StringFilter leasePeriodQueryParam;

    private StringFilter leaseContractQueryParam;

    private Boolean distinct;

    public ReportMetadataCriteria() {}

    public ReportMetadataCriteria(ReportMetadataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportTitle = other.reportTitle == null ? null : other.reportTitle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.module = other.module == null ? null : other.module.copy();
        this.pagePath = other.pagePath == null ? null : other.pagePath.copy();
        this.backendApi = other.backendApi == null ? null : other.backendApi.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.displayLeasePeriod = other.displayLeasePeriod == null ? null : other.displayLeasePeriod.copy();
        this.displayLeaseContract = other.displayLeaseContract == null ? null : other.displayLeaseContract.copy();
        this.leasePeriodQueryParam = other.leasePeriodQueryParam == null ? null : other.leasePeriodQueryParam.copy();
        this.leaseContractQueryParam = other.leaseContractQueryParam == null ? null : other.leaseContractQueryParam.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportMetadataCriteria copy() {
        return new ReportMetadataCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReportTitle() {
        return reportTitle;
    }

    public StringFilter reportTitle() {
        if (reportTitle == null) {
            reportTitle = new StringFilter();
        }
        return reportTitle;
    }

    public void setReportTitle(StringFilter reportTitle) {
        this.reportTitle = reportTitle;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getModule() {
        return module;
    }

    public StringFilter module() {
        if (module == null) {
            module = new StringFilter();
        }
        return module;
    }

    public void setModule(StringFilter module) {
        this.module = module;
    }

    public StringFilter getPagePath() {
        return pagePath;
    }

    public StringFilter pagePath() {
        if (pagePath == null) {
            pagePath = new StringFilter();
        }
        return pagePath;
    }

    public void setPagePath(StringFilter pagePath) {
        this.pagePath = pagePath;
    }

    public StringFilter getBackendApi() {
        return backendApi;
    }

    public StringFilter backendApi() {
        if (backendApi == null) {
            backendApi = new StringFilter();
        }
        return backendApi;
    }

    public void setBackendApi(StringFilter backendApi) {
        this.backendApi = backendApi;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public BooleanFilter getDisplayLeasePeriod() {
        return displayLeasePeriod;
    }

    public BooleanFilter displayLeasePeriod() {
        if (displayLeasePeriod == null) {
            displayLeasePeriod = new BooleanFilter();
        }
        return displayLeasePeriod;
    }

    public void setDisplayLeasePeriod(BooleanFilter displayLeasePeriod) {
        this.displayLeasePeriod = displayLeasePeriod;
    }

    public BooleanFilter getDisplayLeaseContract() {
        return displayLeaseContract;
    }

    public BooleanFilter displayLeaseContract() {
        if (displayLeaseContract == null) {
            displayLeaseContract = new BooleanFilter();
        }
        return displayLeaseContract;
    }

    public void setDisplayLeaseContract(BooleanFilter displayLeaseContract) {
        this.displayLeaseContract = displayLeaseContract;
    }

    public StringFilter getLeasePeriodQueryParam() {
        return leasePeriodQueryParam;
    }

    public StringFilter leasePeriodQueryParam() {
        if (leasePeriodQueryParam == null) {
            leasePeriodQueryParam = new StringFilter();
        }
        return leasePeriodQueryParam;
    }

    public void setLeasePeriodQueryParam(StringFilter leasePeriodQueryParam) {
        this.leasePeriodQueryParam = leasePeriodQueryParam;
    }

    public StringFilter getLeaseContractQueryParam() {
        return leaseContractQueryParam;
    }

    public StringFilter leaseContractQueryParam() {
        if (leaseContractQueryParam == null) {
            leaseContractQueryParam = new StringFilter();
        }
        return leaseContractQueryParam;
    }

    public void setLeaseContractQueryParam(StringFilter leaseContractQueryParam) {
        this.leaseContractQueryParam = leaseContractQueryParam;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReportMetadataCriteria that = (ReportMetadataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportTitle, that.reportTitle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(module, that.module) &&
            Objects.equals(pagePath, that.pagePath) &&
            Objects.equals(backendApi, that.backendApi) &&
            Objects.equals(active, that.active) &&
            Objects.equals(displayLeasePeriod, that.displayLeasePeriod) &&
            Objects.equals(displayLeaseContract, that.displayLeaseContract) &&
            Objects.equals(leasePeriodQueryParam, that.leasePeriodQueryParam) &&
            Objects.equals(leaseContractQueryParam, that.leaseContractQueryParam) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportTitle,
            description,
            module,
            pagePath,
            backendApi,
            active,
            displayLeasePeriod,
            displayLeaseContract,
            leasePeriodQueryParam,
            leaseContractQueryParam,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportMetadataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportTitle != null ? "reportTitle=" + reportTitle + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (module != null ? "module=" + module + ", " : "") +
            (pagePath != null ? "pagePath=" + pagePath + ", " : "") +
            (backendApi != null ? "backendApi=" + backendApi + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (displayLeasePeriod != null ? "displayLeasePeriod=" + displayLeasePeriod + ", " : "") +
            (displayLeaseContract != null ? "displayLeaseContract=" + displayLeaseContract + ", " : "") +
            (leasePeriodQueryParam != null ? "leasePeriodQueryParam=" + leasePeriodQueryParam + ", " : "") +
            (leaseContractQueryParam != null ? "leaseContractQueryParam=" + leaseContractQueryParam + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
