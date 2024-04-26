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

/**
 * A BouncedChequeCategories.
 */
@Entity
@Table(name = "bounced_cheque_categories")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "bouncedchequecategories")
public class BouncedChequeCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "bounced_cheque_category_type_code", nullable = false, unique = true)
    private String bouncedChequeCategoryTypeCode;

    @NotNull
    @Column(name = "bounced_cheque_category_type", nullable = false, unique = true)
    private String bouncedChequeCategoryType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BouncedChequeCategories id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBouncedChequeCategoryTypeCode() {
        return this.bouncedChequeCategoryTypeCode;
    }

    public BouncedChequeCategories bouncedChequeCategoryTypeCode(String bouncedChequeCategoryTypeCode) {
        this.setBouncedChequeCategoryTypeCode(bouncedChequeCategoryTypeCode);
        return this;
    }

    public void setBouncedChequeCategoryTypeCode(String bouncedChequeCategoryTypeCode) {
        this.bouncedChequeCategoryTypeCode = bouncedChequeCategoryTypeCode;
    }

    public String getBouncedChequeCategoryType() {
        return this.bouncedChequeCategoryType;
    }

    public BouncedChequeCategories bouncedChequeCategoryType(String bouncedChequeCategoryType) {
        this.setBouncedChequeCategoryType(bouncedChequeCategoryType);
        return this;
    }

    public void setBouncedChequeCategoryType(String bouncedChequeCategoryType) {
        this.bouncedChequeCategoryType = bouncedChequeCategoryType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BouncedChequeCategories)) {
            return false;
        }
        return id != null && id.equals(((BouncedChequeCategories) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BouncedChequeCategories{" +
            "id=" + getId() +
            ", bouncedChequeCategoryTypeCode='" + getBouncedChequeCategoryTypeCode() + "'" +
            ", bouncedChequeCategoryType='" + getBouncedChequeCategoryType() + "'" +
            "}";
    }
}
