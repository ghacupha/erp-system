package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
import io.github.erp.domain.enumeration.CurrencyAuthenticityFlags;
import io.github.erp.domain.enumeration.CurrencyAuthenticityTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CurrencyAuthenticityFlag.
 */
@Entity
@Table(name = "currency_authenticity_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "currencyauthenticityflag")
public class CurrencyAuthenticityFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_authenticity_flag", nullable = false)
    private CurrencyAuthenticityFlags currencyAuthenticityFlag;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_authenticity_type", nullable = false)
    private CurrencyAuthenticityTypes currencyAuthenticityType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "currency_authenticity_type_details")
    private String currencyAuthenticityTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CurrencyAuthenticityFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyAuthenticityFlags getCurrencyAuthenticityFlag() {
        return this.currencyAuthenticityFlag;
    }

    public CurrencyAuthenticityFlag currencyAuthenticityFlag(CurrencyAuthenticityFlags currencyAuthenticityFlag) {
        this.setCurrencyAuthenticityFlag(currencyAuthenticityFlag);
        return this;
    }

    public void setCurrencyAuthenticityFlag(CurrencyAuthenticityFlags currencyAuthenticityFlag) {
        this.currencyAuthenticityFlag = currencyAuthenticityFlag;
    }

    public CurrencyAuthenticityTypes getCurrencyAuthenticityType() {
        return this.currencyAuthenticityType;
    }

    public CurrencyAuthenticityFlag currencyAuthenticityType(CurrencyAuthenticityTypes currencyAuthenticityType) {
        this.setCurrencyAuthenticityType(currencyAuthenticityType);
        return this;
    }

    public void setCurrencyAuthenticityType(CurrencyAuthenticityTypes currencyAuthenticityType) {
        this.currencyAuthenticityType = currencyAuthenticityType;
    }

    public String getCurrencyAuthenticityTypeDetails() {
        return this.currencyAuthenticityTypeDetails;
    }

    public CurrencyAuthenticityFlag currencyAuthenticityTypeDetails(String currencyAuthenticityTypeDetails) {
        this.setCurrencyAuthenticityTypeDetails(currencyAuthenticityTypeDetails);
        return this;
    }

    public void setCurrencyAuthenticityTypeDetails(String currencyAuthenticityTypeDetails) {
        this.currencyAuthenticityTypeDetails = currencyAuthenticityTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyAuthenticityFlag)) {
            return false;
        }
        return id != null && id.equals(((CurrencyAuthenticityFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyAuthenticityFlag{" +
            "id=" + getId() +
            ", currencyAuthenticityFlag='" + getCurrencyAuthenticityFlag() + "'" +
            ", currencyAuthenticityType='" + getCurrencyAuthenticityType() + "'" +
            ", currencyAuthenticityTypeDetails='" + getCurrencyAuthenticityTypeDetails() + "'" +
            "}";
    }
}
