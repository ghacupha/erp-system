package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A DerivativeUnderlyingAsset.
 */
@Entity
@Table(name = "derivative_underlying_asset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "derivativeunderlyingasset")
public class DerivativeUnderlyingAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "derivative_underlying_asset_type_code", nullable = false, unique = true)
    private String derivativeUnderlyingAssetTypeCode;

    @NotNull
    @Column(name = "financial_derivative_underlying_asset_type", nullable = false, unique = true)
    private String financialDerivativeUnderlyingAssetType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "derivative_underlying_asset_type_details")
    private String derivativeUnderlyingAssetTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DerivativeUnderlyingAsset id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDerivativeUnderlyingAssetTypeCode() {
        return this.derivativeUnderlyingAssetTypeCode;
    }

    public DerivativeUnderlyingAsset derivativeUnderlyingAssetTypeCode(String derivativeUnderlyingAssetTypeCode) {
        this.setDerivativeUnderlyingAssetTypeCode(derivativeUnderlyingAssetTypeCode);
        return this;
    }

    public void setDerivativeUnderlyingAssetTypeCode(String derivativeUnderlyingAssetTypeCode) {
        this.derivativeUnderlyingAssetTypeCode = derivativeUnderlyingAssetTypeCode;
    }

    public String getFinancialDerivativeUnderlyingAssetType() {
        return this.financialDerivativeUnderlyingAssetType;
    }

    public DerivativeUnderlyingAsset financialDerivativeUnderlyingAssetType(String financialDerivativeUnderlyingAssetType) {
        this.setFinancialDerivativeUnderlyingAssetType(financialDerivativeUnderlyingAssetType);
        return this;
    }

    public void setFinancialDerivativeUnderlyingAssetType(String financialDerivativeUnderlyingAssetType) {
        this.financialDerivativeUnderlyingAssetType = financialDerivativeUnderlyingAssetType;
    }

    public String getDerivativeUnderlyingAssetTypeDetails() {
        return this.derivativeUnderlyingAssetTypeDetails;
    }

    public DerivativeUnderlyingAsset derivativeUnderlyingAssetTypeDetails(String derivativeUnderlyingAssetTypeDetails) {
        this.setDerivativeUnderlyingAssetTypeDetails(derivativeUnderlyingAssetTypeDetails);
        return this;
    }

    public void setDerivativeUnderlyingAssetTypeDetails(String derivativeUnderlyingAssetTypeDetails) {
        this.derivativeUnderlyingAssetTypeDetails = derivativeUnderlyingAssetTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DerivativeUnderlyingAsset)) {
            return false;
        }
        return id != null && id.equals(((DerivativeUnderlyingAsset) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DerivativeUnderlyingAsset{" +
            "id=" + getId() +
            ", derivativeUnderlyingAssetTypeCode='" + getDerivativeUnderlyingAssetTypeCode() + "'" +
            ", financialDerivativeUnderlyingAssetType='" + getFinancialDerivativeUnderlyingAssetType() + "'" +
            ", derivativeUnderlyingAssetTypeDetails='" + getDerivativeUnderlyingAssetTypeDetails() + "'" +
            "}";
    }
}
