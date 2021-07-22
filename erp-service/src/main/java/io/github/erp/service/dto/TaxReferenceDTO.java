package io.github.erp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import io.github.erp.domain.enumeration.taxReferenceTypes;

/**
 * A DTO for the {@link io.github.erp.domain.TaxReference} entity.
 */
public class TaxReferenceDTO implements Serializable {
    
    private Long id;

    
    private String taxName;

    private String taxDescription;

    @NotNull
    private Double taxPercentage;

    private taxReferenceTypes taxReferenceType;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    public Double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public taxReferenceTypes getTaxReferenceType() {
        return taxReferenceType;
    }

    public void setTaxReferenceType(taxReferenceTypes taxReferenceType) {
        this.taxReferenceType = taxReferenceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxReferenceDTO)) {
            return false;
        }

        return id != null && id.equals(((TaxReferenceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxReferenceDTO{" +
            "id=" + getId() +
            ", taxName='" + getTaxName() + "'" +
            ", taxDescription='" + getTaxDescription() + "'" +
            ", taxPercentage=" + getTaxPercentage() +
            ", taxReferenceType='" + getTaxReferenceType() + "'" +
            "}";
    }
}
