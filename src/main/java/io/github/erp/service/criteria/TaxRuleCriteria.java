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

    private LongFilter paymentId;

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
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
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

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
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
            Objects.equals(paymentId, that.paymentId)
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
            paymentId
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
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            "}";
    }
}
