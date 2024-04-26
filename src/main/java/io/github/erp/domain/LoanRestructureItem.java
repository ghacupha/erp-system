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
