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
import io.github.erp.domain.enumeration.FlagCodes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FraudCategoryFlag.
 */
@Entity
@Table(name = "fraud_category_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fraudcategoryflag")
public class FraudCategoryFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fraud_category_flag", nullable = false)
    private FlagCodes fraudCategoryFlag;

    @Column(name = "fraud_category_type_details")
    private String fraudCategoryTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FraudCategoryFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlagCodes getFraudCategoryFlag() {
        return this.fraudCategoryFlag;
    }

    public FraudCategoryFlag fraudCategoryFlag(FlagCodes fraudCategoryFlag) {
        this.setFraudCategoryFlag(fraudCategoryFlag);
        return this;
    }

    public void setFraudCategoryFlag(FlagCodes fraudCategoryFlag) {
        this.fraudCategoryFlag = fraudCategoryFlag;
    }

    public String getFraudCategoryTypeDetails() {
        return this.fraudCategoryTypeDetails;
    }

    public FraudCategoryFlag fraudCategoryTypeDetails(String fraudCategoryTypeDetails) {
        this.setFraudCategoryTypeDetails(fraudCategoryTypeDetails);
        return this;
    }

    public void setFraudCategoryTypeDetails(String fraudCategoryTypeDetails) {
        this.fraudCategoryTypeDetails = fraudCategoryTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FraudCategoryFlag)) {
            return false;
        }
        return id != null && id.equals(((FraudCategoryFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraudCategoryFlag{" +
            "id=" + getId() +
            ", fraudCategoryFlag='" + getFraudCategoryFlag() + "'" +
            ", fraudCategoryTypeDetails='" + getFraudCategoryTypeDetails() + "'" +
            "}";
    }
}
