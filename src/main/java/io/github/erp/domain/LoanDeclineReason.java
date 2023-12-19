package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
 * A LoanDeclineReason.
 */
@Entity
@Table(name = "loan_decline_reason")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loandeclinereason")
public class LoanDeclineReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loan_decline_reason_type_code", nullable = false, unique = true)
    private String loanDeclineReasonTypeCode;

    @NotNull
    @Column(name = "loan_decline_reason_type", nullable = false, unique = true)
    private String loanDeclineReasonType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "loan_decline_reason_details")
    private String loanDeclineReasonDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanDeclineReason id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanDeclineReasonTypeCode() {
        return this.loanDeclineReasonTypeCode;
    }

    public LoanDeclineReason loanDeclineReasonTypeCode(String loanDeclineReasonTypeCode) {
        this.setLoanDeclineReasonTypeCode(loanDeclineReasonTypeCode);
        return this;
    }

    public void setLoanDeclineReasonTypeCode(String loanDeclineReasonTypeCode) {
        this.loanDeclineReasonTypeCode = loanDeclineReasonTypeCode;
    }

    public String getLoanDeclineReasonType() {
        return this.loanDeclineReasonType;
    }

    public LoanDeclineReason loanDeclineReasonType(String loanDeclineReasonType) {
        this.setLoanDeclineReasonType(loanDeclineReasonType);
        return this;
    }

    public void setLoanDeclineReasonType(String loanDeclineReasonType) {
        this.loanDeclineReasonType = loanDeclineReasonType;
    }

    public String getLoanDeclineReasonDetails() {
        return this.loanDeclineReasonDetails;
    }

    public LoanDeclineReason loanDeclineReasonDetails(String loanDeclineReasonDetails) {
        this.setLoanDeclineReasonDetails(loanDeclineReasonDetails);
        return this;
    }

    public void setLoanDeclineReasonDetails(String loanDeclineReasonDetails) {
        this.loanDeclineReasonDetails = loanDeclineReasonDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanDeclineReason)) {
            return false;
        }
        return id != null && id.equals(((LoanDeclineReason) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanDeclineReason{" +
            "id=" + getId() +
            ", loanDeclineReasonTypeCode='" + getLoanDeclineReasonTypeCode() + "'" +
            ", loanDeclineReasonType='" + getLoanDeclineReasonType() + "'" +
            ", loanDeclineReasonDetails='" + getLoanDeclineReasonDetails() + "'" +
            "}";
    }
}
