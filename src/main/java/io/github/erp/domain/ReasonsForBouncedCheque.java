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
 * A ReasonsForBouncedCheque.
 */
@Entity
@Table(name = "reasons_for_bounced_cheque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reasonsforbouncedcheque")
public class ReasonsForBouncedCheque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "bounced_cheque_reasons_type_code", nullable = false, unique = true)
    private String bouncedChequeReasonsTypeCode;

    @Column(name = "bounced_cheque_reasons_type")
    private String bouncedChequeReasonsType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReasonsForBouncedCheque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBouncedChequeReasonsTypeCode() {
        return this.bouncedChequeReasonsTypeCode;
    }

    public ReasonsForBouncedCheque bouncedChequeReasonsTypeCode(String bouncedChequeReasonsTypeCode) {
        this.setBouncedChequeReasonsTypeCode(bouncedChequeReasonsTypeCode);
        return this;
    }

    public void setBouncedChequeReasonsTypeCode(String bouncedChequeReasonsTypeCode) {
        this.bouncedChequeReasonsTypeCode = bouncedChequeReasonsTypeCode;
    }

    public String getBouncedChequeReasonsType() {
        return this.bouncedChequeReasonsType;
    }

    public ReasonsForBouncedCheque bouncedChequeReasonsType(String bouncedChequeReasonsType) {
        this.setBouncedChequeReasonsType(bouncedChequeReasonsType);
        return this;
    }

    public void setBouncedChequeReasonsType(String bouncedChequeReasonsType) {
        this.bouncedChequeReasonsType = bouncedChequeReasonsType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReasonsForBouncedCheque)) {
            return false;
        }
        return id != null && id.equals(((ReasonsForBouncedCheque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReasonsForBouncedCheque{" +
            "id=" + getId() +
            ", bouncedChequeReasonsTypeCode='" + getBouncedChequeReasonsTypeCode() + "'" +
            ", bouncedChequeReasonsType='" + getBouncedChequeReasonsType() + "'" +
            "}";
    }
}
