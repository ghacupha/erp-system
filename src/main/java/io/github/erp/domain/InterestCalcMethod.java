package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * A InterestCalcMethod.
 */
@Entity
@Table(name = "interest_calc_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "interestcalcmethod")
public class InterestCalcMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "interest_calculation_method_code", nullable = false, unique = true)
    private String interestCalculationMethodCode;

    @NotNull
    @Column(name = "interest_calculation_mthod_type", nullable = false, unique = true)
    private String interestCalculationMthodType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "interest_calculation_method_details")
    private String interestCalculationMethodDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InterestCalcMethod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterestCalculationMethodCode() {
        return this.interestCalculationMethodCode;
    }

    public InterestCalcMethod interestCalculationMethodCode(String interestCalculationMethodCode) {
        this.setInterestCalculationMethodCode(interestCalculationMethodCode);
        return this;
    }

    public void setInterestCalculationMethodCode(String interestCalculationMethodCode) {
        this.interestCalculationMethodCode = interestCalculationMethodCode;
    }

    public String getInterestCalculationMthodType() {
        return this.interestCalculationMthodType;
    }

    public InterestCalcMethod interestCalculationMthodType(String interestCalculationMthodType) {
        this.setInterestCalculationMthodType(interestCalculationMthodType);
        return this;
    }

    public void setInterestCalculationMthodType(String interestCalculationMthodType) {
        this.interestCalculationMthodType = interestCalculationMthodType;
    }

    public String getInterestCalculationMethodDetails() {
        return this.interestCalculationMethodDetails;
    }

    public InterestCalcMethod interestCalculationMethodDetails(String interestCalculationMethodDetails) {
        this.setInterestCalculationMethodDetails(interestCalculationMethodDetails);
        return this;
    }

    public void setInterestCalculationMethodDetails(String interestCalculationMethodDetails) {
        this.interestCalculationMethodDetails = interestCalculationMethodDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterestCalcMethod)) {
            return false;
        }
        return id != null && id.equals(((InterestCalcMethod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterestCalcMethod{" +
            "id=" + getId() +
            ", interestCalculationMethodCode='" + getInterestCalculationMethodCode() + "'" +
            ", interestCalculationMthodType='" + getInterestCalculationMthodType() + "'" +
            ", interestCalculationMethodDetails='" + getInterestCalculationMethodDetails() + "'" +
            "}";
    }
}
