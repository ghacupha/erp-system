package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.TaxRule} entity. This class is used
 * in {@link io.github.erp.web.rest.TaxRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tax-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaxRuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter telcoExciseDuty;

    private DoubleFilter valueAddedTax;

    private DoubleFilter withholdingVAT;

    private DoubleFilter withholdingTaxConsultancy;

    private DoubleFilter withholdingTaxRent;

    private DoubleFilter cateringLevy;

    private DoubleFilter serviceCharge;

    private DoubleFilter withholdingTaxImportedService;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter placeholderId;

    private Boolean distinct;

    public TaxRuleCriteria() {}

    public TaxRuleCriteria(TaxRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.telcoExciseDuty = other.telcoExciseDuty == null ? null : other.telcoExciseDuty.copy();
        this.valueAddedTax = other.valueAddedTax == null ? null : other.valueAddedTax.copy();
        this.withholdingVAT = other.withholdingVAT == null ? null : other.withholdingVAT.copy();
        this.withholdingTaxConsultancy = other.withholdingTaxConsultancy == null ? null : other.withholdingTaxConsultancy.copy();
        this.withholdingTaxRent = other.withholdingTaxRent == null ? null : other.withholdingTaxRent.copy();
        this.cateringLevy = other.cateringLevy == null ? null : other.cateringLevy.copy();
        this.serviceCharge = other.serviceCharge == null ? null : other.serviceCharge.copy();
        this.withholdingTaxImportedService =
            other.withholdingTaxImportedService == null ? null : other.withholdingTaxImportedService.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TaxRuleCriteria copy() {
        return new TaxRuleCriteria(this);
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

    public DoubleFilter getTelcoExciseDuty() {
        return telcoExciseDuty;
    }

    public DoubleFilter telcoExciseDuty() {
        if (telcoExciseDuty == null) {
            telcoExciseDuty = new DoubleFilter();
        }
        return telcoExciseDuty;
    }

    public void setTelcoExciseDuty(DoubleFilter telcoExciseDuty) {
        this.telcoExciseDuty = telcoExciseDuty;
    }

    public DoubleFilter getValueAddedTax() {
        return valueAddedTax;
    }

    public DoubleFilter valueAddedTax() {
        if (valueAddedTax == null) {
            valueAddedTax = new DoubleFilter();
        }
        return valueAddedTax;
    }

    public void setValueAddedTax(DoubleFilter valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    public DoubleFilter getWithholdingVAT() {
        return withholdingVAT;
    }

    public DoubleFilter withholdingVAT() {
        if (withholdingVAT == null) {
            withholdingVAT = new DoubleFilter();
        }
        return withholdingVAT;
    }

    public void setWithholdingVAT(DoubleFilter withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public DoubleFilter getWithholdingTaxConsultancy() {
        return withholdingTaxConsultancy;
    }

    public DoubleFilter withholdingTaxConsultancy() {
        if (withholdingTaxConsultancy == null) {
            withholdingTaxConsultancy = new DoubleFilter();
        }
        return withholdingTaxConsultancy;
    }

    public void setWithholdingTaxConsultancy(DoubleFilter withholdingTaxConsultancy) {
        this.withholdingTaxConsultancy = withholdingTaxConsultancy;
    }

    public DoubleFilter getWithholdingTaxRent() {
        return withholdingTaxRent;
    }

    public DoubleFilter withholdingTaxRent() {
        if (withholdingTaxRent == null) {
            withholdingTaxRent = new DoubleFilter();
        }
        return withholdingTaxRent;
    }

    public void setWithholdingTaxRent(DoubleFilter withholdingTaxRent) {
        this.withholdingTaxRent = withholdingTaxRent;
    }

    public DoubleFilter getCateringLevy() {
        return cateringLevy;
    }

    public DoubleFilter cateringLevy() {
        if (cateringLevy == null) {
            cateringLevy = new DoubleFilter();
        }
        return cateringLevy;
    }

    public void setCateringLevy(DoubleFilter cateringLevy) {
        this.cateringLevy = cateringLevy;
    }

    public DoubleFilter getServiceCharge() {
        return serviceCharge;
    }

    public DoubleFilter serviceCharge() {
        if (serviceCharge == null) {
            serviceCharge = new DoubleFilter();
        }
        return serviceCharge;
    }

    public void setServiceCharge(DoubleFilter serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public DoubleFilter getWithholdingTaxImportedService() {
        return withholdingTaxImportedService;
    }

    public DoubleFilter withholdingTaxImportedService() {
        if (withholdingTaxImportedService == null) {
            withholdingTaxImportedService = new DoubleFilter();
        }
        return withholdingTaxImportedService;
    }

    public void setWithholdingTaxImportedService(DoubleFilter withholdingTaxImportedService) {
        this.withholdingTaxImportedService = withholdingTaxImportedService;
    }

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            fileUploadToken = new StringFilter();
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new StringFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
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
        final TaxRuleCriteria that = (TaxRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(telcoExciseDuty, that.telcoExciseDuty) &&
            Objects.equals(valueAddedTax, that.valueAddedTax) &&
            Objects.equals(withholdingVAT, that.withholdingVAT) &&
            Objects.equals(withholdingTaxConsultancy, that.withholdingTaxConsultancy) &&
            Objects.equals(withholdingTaxRent, that.withholdingTaxRent) &&
            Objects.equals(cateringLevy, that.cateringLevy) &&
            Objects.equals(serviceCharge, that.serviceCharge) &&
            Objects.equals(withholdingTaxImportedService, that.withholdingTaxImportedService) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            telcoExciseDuty,
            valueAddedTax,
            withholdingVAT,
            withholdingTaxConsultancy,
            withholdingTaxRent,
            cateringLevy,
            serviceCharge,
            withholdingTaxImportedService,
            fileUploadToken,
            compilationToken,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxRuleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (telcoExciseDuty != null ? "telcoExciseDuty=" + telcoExciseDuty + ", " : "") +
            (valueAddedTax != null ? "valueAddedTax=" + valueAddedTax + ", " : "") +
            (withholdingVAT != null ? "withholdingVAT=" + withholdingVAT + ", " : "") +
            (withholdingTaxConsultancy != null ? "withholdingTaxConsultancy=" + withholdingTaxConsultancy + ", " : "") +
            (withholdingTaxRent != null ? "withholdingTaxRent=" + withholdingTaxRent + ", " : "") +
            (cateringLevy != null ? "cateringLevy=" + cateringLevy + ", " : "") +
            (serviceCharge != null ? "serviceCharge=" + serviceCharge + ", " : "") +
            (withholdingTaxImportedService != null ? "withholdingTaxImportedService=" + withholdingTaxImportedService + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
