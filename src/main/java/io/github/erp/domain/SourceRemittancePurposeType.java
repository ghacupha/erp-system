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
import io.github.erp.domain.enumeration.SourceOrPurposeOfRemittancFlag;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A SourceRemittancePurposeType.
 */
@Entity
@Table(name = "source_remittance_purpose_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sourceremittancepurposetype")
public class SourceRemittancePurposeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "source_or_purpose_type_code", nullable = false, unique = true)
    private String sourceOrPurposeTypeCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source_or_purpose_of_remittance_flag", nullable = false)
    private SourceOrPurposeOfRemittancFlag sourceOrPurposeOfRemittanceFlag;

    @NotNull
    @Column(name = "source_or_purpose_of_remittance_type", nullable = false)
    private String sourceOrPurposeOfRemittanceType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remittance_purpose_type_details")
    private String remittancePurposeTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SourceRemittancePurposeType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceOrPurposeTypeCode() {
        return this.sourceOrPurposeTypeCode;
    }

    public SourceRemittancePurposeType sourceOrPurposeTypeCode(String sourceOrPurposeTypeCode) {
        this.setSourceOrPurposeTypeCode(sourceOrPurposeTypeCode);
        return this;
    }

    public void setSourceOrPurposeTypeCode(String sourceOrPurposeTypeCode) {
        this.sourceOrPurposeTypeCode = sourceOrPurposeTypeCode;
    }

    public SourceOrPurposeOfRemittancFlag getSourceOrPurposeOfRemittanceFlag() {
        return this.sourceOrPurposeOfRemittanceFlag;
    }

    public SourceRemittancePurposeType sourceOrPurposeOfRemittanceFlag(SourceOrPurposeOfRemittancFlag sourceOrPurposeOfRemittanceFlag) {
        this.setSourceOrPurposeOfRemittanceFlag(sourceOrPurposeOfRemittanceFlag);
        return this;
    }

    public void setSourceOrPurposeOfRemittanceFlag(SourceOrPurposeOfRemittancFlag sourceOrPurposeOfRemittanceFlag) {
        this.sourceOrPurposeOfRemittanceFlag = sourceOrPurposeOfRemittanceFlag;
    }

    public String getSourceOrPurposeOfRemittanceType() {
        return this.sourceOrPurposeOfRemittanceType;
    }

    public SourceRemittancePurposeType sourceOrPurposeOfRemittanceType(String sourceOrPurposeOfRemittanceType) {
        this.setSourceOrPurposeOfRemittanceType(sourceOrPurposeOfRemittanceType);
        return this;
    }

    public void setSourceOrPurposeOfRemittanceType(String sourceOrPurposeOfRemittanceType) {
        this.sourceOrPurposeOfRemittanceType = sourceOrPurposeOfRemittanceType;
    }

    public String getRemittancePurposeTypeDetails() {
        return this.remittancePurposeTypeDetails;
    }

    public SourceRemittancePurposeType remittancePurposeTypeDetails(String remittancePurposeTypeDetails) {
        this.setRemittancePurposeTypeDetails(remittancePurposeTypeDetails);
        return this;
    }

    public void setRemittancePurposeTypeDetails(String remittancePurposeTypeDetails) {
        this.remittancePurposeTypeDetails = remittancePurposeTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceRemittancePurposeType)) {
            return false;
        }
        return id != null && id.equals(((SourceRemittancePurposeType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceRemittancePurposeType{" +
            "id=" + getId() +
            ", sourceOrPurposeTypeCode='" + getSourceOrPurposeTypeCode() + "'" +
            ", sourceOrPurposeOfRemittanceFlag='" + getSourceOrPurposeOfRemittanceFlag() + "'" +
            ", sourceOrPurposeOfRemittanceType='" + getSourceOrPurposeOfRemittanceType() + "'" +
            ", remittancePurposeTypeDetails='" + getRemittancePurposeTypeDetails() + "'" +
            "}";
    }
}
