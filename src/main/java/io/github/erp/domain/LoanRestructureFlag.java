package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.enumeration.FlagCodes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A LoanRestructureFlag.
 */
@Entity
@Table(name = "loan_restructure_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanrestructureflag")
public class LoanRestructureFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_restructure_flag_code", nullable = false)
    private FlagCodes loanRestructureFlagCode;

    @NotNull
    @Column(name = "loan_restructure_flag_type", nullable = false, unique = true)
    private String loanRestructureFlagType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "loan_restructure_flag_details")
    private String loanRestructureFlagDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanRestructureFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlagCodes getLoanRestructureFlagCode() {
        return this.loanRestructureFlagCode;
    }

    public LoanRestructureFlag loanRestructureFlagCode(FlagCodes loanRestructureFlagCode) {
        this.setLoanRestructureFlagCode(loanRestructureFlagCode);
        return this;
    }

    public void setLoanRestructureFlagCode(FlagCodes loanRestructureFlagCode) {
        this.loanRestructureFlagCode = loanRestructureFlagCode;
    }

    public String getLoanRestructureFlagType() {
        return this.loanRestructureFlagType;
    }

    public LoanRestructureFlag loanRestructureFlagType(String loanRestructureFlagType) {
        this.setLoanRestructureFlagType(loanRestructureFlagType);
        return this;
    }

    public void setLoanRestructureFlagType(String loanRestructureFlagType) {
        this.loanRestructureFlagType = loanRestructureFlagType;
    }

    public String getLoanRestructureFlagDetails() {
        return this.loanRestructureFlagDetails;
    }

    public LoanRestructureFlag loanRestructureFlagDetails(String loanRestructureFlagDetails) {
        this.setLoanRestructureFlagDetails(loanRestructureFlagDetails);
        return this;
    }

    public void setLoanRestructureFlagDetails(String loanRestructureFlagDetails) {
        this.loanRestructureFlagDetails = loanRestructureFlagDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanRestructureFlag)) {
            return false;
        }
        return id != null && id.equals(((LoanRestructureFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRestructureFlag{" +
            "id=" + getId() +
            ", loanRestructureFlagCode='" + getLoanRestructureFlagCode() + "'" +
            ", loanRestructureFlagType='" + getLoanRestructureFlagType() + "'" +
            ", loanRestructureFlagDetails='" + getLoanRestructureFlagDetails() + "'" +
            "}";
    }
}
