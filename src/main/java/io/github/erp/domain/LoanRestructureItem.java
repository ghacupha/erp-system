package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
 * A LoanRestructureItem.
 */
@Entity
@Table(name = "loan_restructure_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanrestructureitem")
public class LoanRestructureItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loan_restructure_item_code", nullable = false, unique = true)
    private String loanRestructureItemCode;

    @NotNull
    @Column(name = "loan_restructure_item_type", nullable = false, unique = true)
    private String loanRestructureItemType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "loan_restructure_item_details")
    private String loanRestructureItemDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanRestructureItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanRestructureItemCode() {
        return this.loanRestructureItemCode;
    }

    public LoanRestructureItem loanRestructureItemCode(String loanRestructureItemCode) {
        this.setLoanRestructureItemCode(loanRestructureItemCode);
        return this;
    }

    public void setLoanRestructureItemCode(String loanRestructureItemCode) {
        this.loanRestructureItemCode = loanRestructureItemCode;
    }

    public String getLoanRestructureItemType() {
        return this.loanRestructureItemType;
    }

    public LoanRestructureItem loanRestructureItemType(String loanRestructureItemType) {
        this.setLoanRestructureItemType(loanRestructureItemType);
        return this;
    }

    public void setLoanRestructureItemType(String loanRestructureItemType) {
        this.loanRestructureItemType = loanRestructureItemType;
    }

    public String getLoanRestructureItemDetails() {
        return this.loanRestructureItemDetails;
    }

    public LoanRestructureItem loanRestructureItemDetails(String loanRestructureItemDetails) {
        this.setLoanRestructureItemDetails(loanRestructureItemDetails);
        return this;
    }

    public void setLoanRestructureItemDetails(String loanRestructureItemDetails) {
        this.loanRestructureItemDetails = loanRestructureItemDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanRestructureItem)) {
            return false;
        }
        return id != null && id.equals(((LoanRestructureItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRestructureItem{" +
            "id=" + getId() +
            ", loanRestructureItemCode='" + getLoanRestructureItemCode() + "'" +
            ", loanRestructureItemType='" + getLoanRestructureItemType() + "'" +
            ", loanRestructureItemDetails='" + getLoanRestructureItemDetails() + "'" +
            "}";
    }
}
