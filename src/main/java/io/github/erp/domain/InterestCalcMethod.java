package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
