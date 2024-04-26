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
 * A IssuersOfSecurities.
 */
@Entity
@Table(name = "issuers_of_securities")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "issuersofsecurities")
public class IssuersOfSecurities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "issuer_of_securities_code", nullable = false, unique = true)
    private String issuerOfSecuritiesCode;

    @NotNull
    @Column(name = "issuer_of_securities", nullable = false, unique = true)
    private String issuerOfSecurities;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "issuer_of_securities_description")
    private String issuerOfSecuritiesDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IssuersOfSecurities id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssuerOfSecuritiesCode() {
        return this.issuerOfSecuritiesCode;
    }

    public IssuersOfSecurities issuerOfSecuritiesCode(String issuerOfSecuritiesCode) {
        this.setIssuerOfSecuritiesCode(issuerOfSecuritiesCode);
        return this;
    }

    public void setIssuerOfSecuritiesCode(String issuerOfSecuritiesCode) {
        this.issuerOfSecuritiesCode = issuerOfSecuritiesCode;
    }

    public String getIssuerOfSecurities() {
        return this.issuerOfSecurities;
    }

    public IssuersOfSecurities issuerOfSecurities(String issuerOfSecurities) {
        this.setIssuerOfSecurities(issuerOfSecurities);
        return this;
    }

    public void setIssuerOfSecurities(String issuerOfSecurities) {
        this.issuerOfSecurities = issuerOfSecurities;
    }

    public String getIssuerOfSecuritiesDescription() {
        return this.issuerOfSecuritiesDescription;
    }

    public IssuersOfSecurities issuerOfSecuritiesDescription(String issuerOfSecuritiesDescription) {
        this.setIssuerOfSecuritiesDescription(issuerOfSecuritiesDescription);
        return this;
    }

    public void setIssuerOfSecuritiesDescription(String issuerOfSecuritiesDescription) {
        this.issuerOfSecuritiesDescription = issuerOfSecuritiesDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssuersOfSecurities)) {
            return false;
        }
        return id != null && id.equals(((IssuersOfSecurities) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssuersOfSecurities{" +
            "id=" + getId() +
            ", issuerOfSecuritiesCode='" + getIssuerOfSecuritiesCode() + "'" +
            ", issuerOfSecurities='" + getIssuerOfSecurities() + "'" +
            ", issuerOfSecuritiesDescription='" + getIssuerOfSecuritiesDescription() + "'" +
            "}";
    }
}
