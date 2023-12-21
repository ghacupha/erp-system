package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TaxRule.
 */
@Entity
@Table(name = "tax_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "taxrule")
public class TaxRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "telco_excise_duty")
    private Double telcoExciseDuty;

    @Column(name = "value_added_tax")
    private Double valueAddedTax;

    @Column(name = "withholding_vat")
    private Double withholdingVAT;

    @Column(name = "withholding_tax_consultancy")
    private Double withholdingTaxConsultancy;

    @Column(name = "withholding_tax_rent")
    private Double withholdingTaxRent;

    @Column(name = "catering_levy")
    private Double cateringLevy;

    @Column(name = "service_charge")
    private Double serviceCharge;

    @Column(name = "withholding_tax_imported_service")
    private Double withholdingTaxImportedService;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_tax_rule__placeholder",
        joinColumns = @JoinColumn(name = "tax_rule_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaxRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTelcoExciseDuty() {
        return this.telcoExciseDuty;
    }

    public TaxRule telcoExciseDuty(Double telcoExciseDuty) {
        this.setTelcoExciseDuty(telcoExciseDuty);
        return this;
    }

    public void setTelcoExciseDuty(Double telcoExciseDuty) {
        this.telcoExciseDuty = telcoExciseDuty;
    }

    public Double getValueAddedTax() {
        return this.valueAddedTax;
    }

    public TaxRule valueAddedTax(Double valueAddedTax) {
        this.setValueAddedTax(valueAddedTax);
        return this;
    }

    public void setValueAddedTax(Double valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    public Double getWithholdingVAT() {
        return this.withholdingVAT;
    }

    public TaxRule withholdingVAT(Double withholdingVAT) {
        this.setWithholdingVAT(withholdingVAT);
        return this;
    }

    public void setWithholdingVAT(Double withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public Double getWithholdingTaxConsultancy() {
        return this.withholdingTaxConsultancy;
    }

    public TaxRule withholdingTaxConsultancy(Double withholdingTaxConsultancy) {
        this.setWithholdingTaxConsultancy(withholdingTaxConsultancy);
        return this;
    }

    public void setWithholdingTaxConsultancy(Double withholdingTaxConsultancy) {
        this.withholdingTaxConsultancy = withholdingTaxConsultancy;
    }

    public Double getWithholdingTaxRent() {
        return this.withholdingTaxRent;
    }

    public TaxRule withholdingTaxRent(Double withholdingTaxRent) {
        this.setWithholdingTaxRent(withholdingTaxRent);
        return this;
    }

    public void setWithholdingTaxRent(Double withholdingTaxRent) {
        this.withholdingTaxRent = withholdingTaxRent;
    }

    public Double getCateringLevy() {
        return this.cateringLevy;
    }

    public TaxRule cateringLevy(Double cateringLevy) {
        this.setCateringLevy(cateringLevy);
        return this;
    }

    public void setCateringLevy(Double cateringLevy) {
        this.cateringLevy = cateringLevy;
    }

    public Double getServiceCharge() {
        return this.serviceCharge;
    }

    public TaxRule serviceCharge(Double serviceCharge) {
        this.setServiceCharge(serviceCharge);
        return this;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getWithholdingTaxImportedService() {
        return this.withholdingTaxImportedService;
    }

    public TaxRule withholdingTaxImportedService(Double withholdingTaxImportedService) {
        this.setWithholdingTaxImportedService(withholdingTaxImportedService);
        return this;
    }

    public void setWithholdingTaxImportedService(Double withholdingTaxImportedService) {
        this.withholdingTaxImportedService = withholdingTaxImportedService;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public TaxRule fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public TaxRule compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TaxRule placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TaxRule addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TaxRule removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxRule)) {
            return false;
        }
        return id != null && id.equals(((TaxRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxRule{" +
            "id=" + getId() +
            ", telcoExciseDuty=" + getTelcoExciseDuty() +
            ", valueAddedTax=" + getValueAddedTax() +
            ", withholdingVAT=" + getWithholdingVAT() +
            ", withholdingTaxConsultancy=" + getWithholdingTaxConsultancy() +
            ", withholdingTaxRent=" + getWithholdingTaxRent() +
            ", cateringLevy=" + getCateringLevy() +
            ", serviceCharge=" + getServiceCharge() +
            ", withholdingTaxImportedService=" + getWithholdingTaxImportedService() +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
