package io.github.erp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.ReportDesign} entity. This class is used
 * in {@link io.github.erp.web.rest.ReportDesignResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /report-designs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportDesignCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter catalogueNumber;

    private StringFilter designation;

    private LongFilter parametersId;

    private LongFilter securityClearanceId;

    private LongFilter reportDesignerId;

    private LongFilter organizationId;

    private LongFilter departmentId;

    private LongFilter placeholderId;

    private LongFilter systemModuleId;

    private Boolean distinct;

    public ReportDesignCriteria() {}

    public ReportDesignCriteria(ReportDesignCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.catalogueNumber = other.catalogueNumber == null ? null : other.catalogueNumber.copy();
        this.designation = other.designation == null ? null : other.designation.copy();
        this.parametersId = other.parametersId == null ? null : other.parametersId.copy();
        this.securityClearanceId = other.securityClearanceId == null ? null : other.securityClearanceId.copy();
        this.reportDesignerId = other.reportDesignerId == null ? null : other.reportDesignerId.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.systemModuleId = other.systemModuleId == null ? null : other.systemModuleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportDesignCriteria copy() {
        return new ReportDesignCriteria(this);
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

    public UUIDFilter getCatalogueNumber() {
        return catalogueNumber;
    }

    public UUIDFilter catalogueNumber() {
        if (catalogueNumber == null) {
            catalogueNumber = new UUIDFilter();
        }
        return catalogueNumber;
    }

    public void setCatalogueNumber(UUIDFilter catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public StringFilter designation() {
        if (designation == null) {
            designation = new StringFilter();
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public LongFilter getParametersId() {
        return parametersId;
    }

    public LongFilter parametersId() {
        if (parametersId == null) {
            parametersId = new LongFilter();
        }
        return parametersId;
    }

    public void setParametersId(LongFilter parametersId) {
        this.parametersId = parametersId;
    }

    public LongFilter getSecurityClearanceId() {
        return securityClearanceId;
    }

    public LongFilter securityClearanceId() {
        if (securityClearanceId == null) {
            securityClearanceId = new LongFilter();
        }
        return securityClearanceId;
    }

    public void setSecurityClearanceId(LongFilter securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public LongFilter getReportDesignerId() {
        return reportDesignerId;
    }

    public LongFilter reportDesignerId() {
        if (reportDesignerId == null) {
            reportDesignerId = new LongFilter();
        }
        return reportDesignerId;
    }

    public void setReportDesignerId(LongFilter reportDesignerId) {
        this.reportDesignerId = reportDesignerId;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            organizationId = new LongFilter();
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getSystemModuleId() {
        return systemModuleId;
    }

    public LongFilter systemModuleId() {
        if (systemModuleId == null) {
            systemModuleId = new LongFilter();
        }
        return systemModuleId;
    }

    public void setSystemModuleId(LongFilter systemModuleId) {
        this.systemModuleId = systemModuleId;
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
        final ReportDesignCriteria that = (ReportDesignCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(catalogueNumber, that.catalogueNumber) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(parametersId, that.parametersId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(reportDesignerId, that.reportDesignerId) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(systemModuleId, that.systemModuleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            catalogueNumber,
            designation,
            parametersId,
            securityClearanceId,
            reportDesignerId,
            organizationId,
            departmentId,
            placeholderId,
            systemModuleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDesignCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (catalogueNumber != null ? "catalogueNumber=" + catalogueNumber + ", " : "") +
            (designation != null ? "designation=" + designation + ", " : "") +
            (parametersId != null ? "parametersId=" + parametersId + ", " : "") +
            (securityClearanceId != null ? "securityClearanceId=" + securityClearanceId + ", " : "") +
            (reportDesignerId != null ? "reportDesignerId=" + reportDesignerId + ", " : "") +
            (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (systemModuleId != null ? "systemModuleId=" + systemModuleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
