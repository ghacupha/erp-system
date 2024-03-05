package io.github.erp.domain;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.enumeration.CurrencyServiceability;
import io.github.erp.domain.enumeration.CurrencyServiceabilityFlagTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CurrencyServiceabilityFlag.
 */
@Entity
@Table(name = "currency_serviceability_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "currencyserviceabilityflag")
public class CurrencyServiceabilityFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_serviceability_flag", nullable = false)
    private CurrencyServiceabilityFlagTypes currencyServiceabilityFlag;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_serviceability", nullable = false)
    private CurrencyServiceability currencyServiceability;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "currency_serviceability_flag_details")
    private String currencyServiceabilityFlagDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CurrencyServiceabilityFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyServiceabilityFlagTypes getCurrencyServiceabilityFlag() {
        return this.currencyServiceabilityFlag;
    }

    public CurrencyServiceabilityFlag currencyServiceabilityFlag(CurrencyServiceabilityFlagTypes currencyServiceabilityFlag) {
        this.setCurrencyServiceabilityFlag(currencyServiceabilityFlag);
        return this;
    }

    public void setCurrencyServiceabilityFlag(CurrencyServiceabilityFlagTypes currencyServiceabilityFlag) {
        this.currencyServiceabilityFlag = currencyServiceabilityFlag;
    }

    public CurrencyServiceability getCurrencyServiceability() {
        return this.currencyServiceability;
    }

    public CurrencyServiceabilityFlag currencyServiceability(CurrencyServiceability currencyServiceability) {
        this.setCurrencyServiceability(currencyServiceability);
        return this;
    }

    public void setCurrencyServiceability(CurrencyServiceability currencyServiceability) {
        this.currencyServiceability = currencyServiceability;
    }

    public String getCurrencyServiceabilityFlagDetails() {
        return this.currencyServiceabilityFlagDetails;
    }

    public CurrencyServiceabilityFlag currencyServiceabilityFlagDetails(String currencyServiceabilityFlagDetails) {
        this.setCurrencyServiceabilityFlagDetails(currencyServiceabilityFlagDetails);
        return this;
    }

    public void setCurrencyServiceabilityFlagDetails(String currencyServiceabilityFlagDetails) {
        this.currencyServiceabilityFlagDetails = currencyServiceabilityFlagDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyServiceabilityFlag)) {
            return false;
        }
        return id != null && id.equals(((CurrencyServiceabilityFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyServiceabilityFlag{" +
            "id=" + getId() +
            ", currencyServiceabilityFlag='" + getCurrencyServiceabilityFlag() + "'" +
            ", currencyServiceability='" + getCurrencyServiceability() + "'" +
            ", currencyServiceabilityFlagDetails='" + getCurrencyServiceabilityFlagDetails() + "'" +
            "}";
    }
}
