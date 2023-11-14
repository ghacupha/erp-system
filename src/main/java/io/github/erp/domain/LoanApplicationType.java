package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
 * A LoanApplicationType.
 */
@Entity
@Table(name = "loan_application_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanapplicationtype")
public class LoanApplicationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loan_application_type_code", nullable = false, unique = true)
    private String loanApplicationTypeCode;

    @NotNull
    @Column(name = "loan_application_type", nullable = false, unique = true)
    private String loanApplicationType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "loan_application_details")
    private String loanApplicationDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanApplicationType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanApplicationTypeCode() {
        return this.loanApplicationTypeCode;
    }

    public LoanApplicationType loanApplicationTypeCode(String loanApplicationTypeCode) {
        this.setLoanApplicationTypeCode(loanApplicationTypeCode);
        return this;
    }

    public void setLoanApplicationTypeCode(String loanApplicationTypeCode) {
        this.loanApplicationTypeCode = loanApplicationTypeCode;
    }

    public String getLoanApplicationType() {
        return this.loanApplicationType;
    }

    public LoanApplicationType loanApplicationType(String loanApplicationType) {
        this.setLoanApplicationType(loanApplicationType);
        return this;
    }

    public void setLoanApplicationType(String loanApplicationType) {
        this.loanApplicationType = loanApplicationType;
    }

    public String getLoanApplicationDetails() {
        return this.loanApplicationDetails;
    }

    public LoanApplicationType loanApplicationDetails(String loanApplicationDetails) {
        this.setLoanApplicationDetails(loanApplicationDetails);
        return this;
    }

    public void setLoanApplicationDetails(String loanApplicationDetails) {
        this.loanApplicationDetails = loanApplicationDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanApplicationType)) {
            return false;
        }
        return id != null && id.equals(((LoanApplicationType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanApplicationType{" +
            "id=" + getId() +
            ", loanApplicationTypeCode='" + getLoanApplicationTypeCode() + "'" +
            ", loanApplicationType='" + getLoanApplicationType() + "'" +
            ", loanApplicationDetails='" + getLoanApplicationDetails() + "'" +
            "}";
    }
}
