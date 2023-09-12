package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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
 * A LoanRepaymentFrequency.
 */
@Entity
@Table(name = "loan_repayment_frequency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanrepaymentfrequency")
public class LoanRepaymentFrequency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "frequency_type_code", nullable = false, unique = true)
    private String frequencyTypeCode;

    @NotNull
    @Column(name = "frequency_type", nullable = false, unique = true)
    private String frequencyType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "frequency_type_details")
    private String frequencyTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanRepaymentFrequency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrequencyTypeCode() {
        return this.frequencyTypeCode;
    }

    public LoanRepaymentFrequency frequencyTypeCode(String frequencyTypeCode) {
        this.setFrequencyTypeCode(frequencyTypeCode);
        return this;
    }

    public void setFrequencyTypeCode(String frequencyTypeCode) {
        this.frequencyTypeCode = frequencyTypeCode;
    }

    public String getFrequencyType() {
        return this.frequencyType;
    }

    public LoanRepaymentFrequency frequencyType(String frequencyType) {
        this.setFrequencyType(frequencyType);
        return this;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getFrequencyTypeDetails() {
        return this.frequencyTypeDetails;
    }

    public LoanRepaymentFrequency frequencyTypeDetails(String frequencyTypeDetails) {
        this.setFrequencyTypeDetails(frequencyTypeDetails);
        return this;
    }

    public void setFrequencyTypeDetails(String frequencyTypeDetails) {
        this.frequencyTypeDetails = frequencyTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanRepaymentFrequency)) {
            return false;
        }
        return id != null && id.equals(((LoanRepaymentFrequency) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRepaymentFrequency{" +
            "id=" + getId() +
            ", frequencyTypeCode='" + getFrequencyTypeCode() + "'" +
            ", frequencyType='" + getFrequencyType() + "'" +
            ", frequencyTypeDetails='" + getFrequencyTypeDetails() + "'" +
            "}";
    }
}
