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
import io.github.erp.domain.enumeration.CounterpartyCategory;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CounterPartyCategory.
 */
@Entity
@Table(name = "counter_party_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "counterpartycategory")
public class CounterPartyCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "counterparty_category_code", nullable = false, unique = true)
    private String counterpartyCategoryCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "counterparty_category_code_details", nullable = false)
    private CounterpartyCategory counterpartyCategoryCodeDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "counterparty_category_description")
    private String counterpartyCategoryDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CounterPartyCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounterpartyCategoryCode() {
        return this.counterpartyCategoryCode;
    }

    public CounterPartyCategory counterpartyCategoryCode(String counterpartyCategoryCode) {
        this.setCounterpartyCategoryCode(counterpartyCategoryCode);
        return this;
    }

    public void setCounterpartyCategoryCode(String counterpartyCategoryCode) {
        this.counterpartyCategoryCode = counterpartyCategoryCode;
    }

    public CounterpartyCategory getCounterpartyCategoryCodeDetails() {
        return this.counterpartyCategoryCodeDetails;
    }

    public CounterPartyCategory counterpartyCategoryCodeDetails(CounterpartyCategory counterpartyCategoryCodeDetails) {
        this.setCounterpartyCategoryCodeDetails(counterpartyCategoryCodeDetails);
        return this;
    }

    public void setCounterpartyCategoryCodeDetails(CounterpartyCategory counterpartyCategoryCodeDetails) {
        this.counterpartyCategoryCodeDetails = counterpartyCategoryCodeDetails;
    }

    public String getCounterpartyCategoryDescription() {
        return this.counterpartyCategoryDescription;
    }

    public CounterPartyCategory counterpartyCategoryDescription(String counterpartyCategoryDescription) {
        this.setCounterpartyCategoryDescription(counterpartyCategoryDescription);
        return this;
    }

    public void setCounterpartyCategoryDescription(String counterpartyCategoryDescription) {
        this.counterpartyCategoryDescription = counterpartyCategoryDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CounterPartyCategory)) {
            return false;
        }
        return id != null && id.equals(((CounterPartyCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterPartyCategory{" +
            "id=" + getId() +
            ", counterpartyCategoryCode='" + getCounterpartyCategoryCode() + "'" +
            ", counterpartyCategoryCodeDetails='" + getCounterpartyCategoryCodeDetails() + "'" +
            ", counterpartyCategoryDescription='" + getCounterpartyCategoryDescription() + "'" +
            "}";
    }
}
