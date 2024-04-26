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
 * A SecurityType.
 */
@Entity
@Table(name = "security_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "securitytype")
public class SecurityType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "security_type_code", nullable = false, unique = true)
    private String securityTypeCode;

    @NotNull
    @Column(name = "security_type", nullable = false, unique = true)
    private String securityType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "security_type_details")
    private String securityTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SecurityType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecurityTypeCode() {
        return this.securityTypeCode;
    }

    public SecurityType securityTypeCode(String securityTypeCode) {
        this.setSecurityTypeCode(securityTypeCode);
        return this;
    }

    public void setSecurityTypeCode(String securityTypeCode) {
        this.securityTypeCode = securityTypeCode;
    }

    public String getSecurityType() {
        return this.securityType;
    }

    public SecurityType securityType(String securityType) {
        this.setSecurityType(securityType);
        return this;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getSecurityTypeDetails() {
        return this.securityTypeDetails;
    }

    public SecurityType securityTypeDetails(String securityTypeDetails) {
        this.setSecurityTypeDetails(securityTypeDetails);
        return this;
    }

    public void setSecurityTypeDetails(String securityTypeDetails) {
        this.securityTypeDetails = securityTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityType)) {
            return false;
        }
        return id != null && id.equals(((SecurityType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityType{" +
            "id=" + getId() +
            ", securityTypeCode='" + getSecurityTypeCode() + "'" +
            ", securityType='" + getSecurityType() + "'" +
            ", securityTypeDetails='" + getSecurityTypeDetails() + "'" +
            "}";
    }
}
