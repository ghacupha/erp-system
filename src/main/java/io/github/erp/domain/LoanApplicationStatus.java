package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
 * A LoanApplicationStatus.
 */
@Entity
@Table(name = "loan_application_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanapplicationstatus")
public class LoanApplicationStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loan_application_status_type_code", nullable = false, unique = true)
    private String loanApplicationStatusTypeCode;

    @NotNull
    @Column(name = "loan_application_status_type", nullable = false, unique = true)
    private String loanApplicationStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "loan_application_status_details")
    private String loanApplicationStatusDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanApplicationStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanApplicationStatusTypeCode() {
        return this.loanApplicationStatusTypeCode;
    }

    public LoanApplicationStatus loanApplicationStatusTypeCode(String loanApplicationStatusTypeCode) {
        this.setLoanApplicationStatusTypeCode(loanApplicationStatusTypeCode);
        return this;
    }

    public void setLoanApplicationStatusTypeCode(String loanApplicationStatusTypeCode) {
        this.loanApplicationStatusTypeCode = loanApplicationStatusTypeCode;
    }

    public String getLoanApplicationStatusType() {
        return this.loanApplicationStatusType;
    }

    public LoanApplicationStatus loanApplicationStatusType(String loanApplicationStatusType) {
        this.setLoanApplicationStatusType(loanApplicationStatusType);
        return this;
    }

    public void setLoanApplicationStatusType(String loanApplicationStatusType) {
        this.loanApplicationStatusType = loanApplicationStatusType;
    }

    public String getLoanApplicationStatusDetails() {
        return this.loanApplicationStatusDetails;
    }

    public LoanApplicationStatus loanApplicationStatusDetails(String loanApplicationStatusDetails) {
        this.setLoanApplicationStatusDetails(loanApplicationStatusDetails);
        return this;
    }

    public void setLoanApplicationStatusDetails(String loanApplicationStatusDetails) {
        this.loanApplicationStatusDetails = loanApplicationStatusDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanApplicationStatus)) {
            return false;
        }
        return id != null && id.equals(((LoanApplicationStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanApplicationStatus{" +
            "id=" + getId() +
            ", loanApplicationStatusTypeCode='" + getLoanApplicationStatusTypeCode() + "'" +
            ", loanApplicationStatusType='" + getLoanApplicationStatusType() + "'" +
            ", loanApplicationStatusDetails='" + getLoanApplicationStatusDetails() + "'" +
            "}";
    }
}
