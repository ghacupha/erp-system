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
 * A CrbComplaintType.
 */
@Entity
@Table(name = "crb_complaint_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbcomplainttype")
public class CrbComplaintType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "complaint_type_code", nullable = false, unique = true)
    private String complaintTypeCode;

    @NotNull
    @Column(name = "complaint_type", nullable = false, unique = true)
    private String complaintType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "complaint_type_details")
    private String complaintTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbComplaintType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintTypeCode() {
        return this.complaintTypeCode;
    }

    public CrbComplaintType complaintTypeCode(String complaintTypeCode) {
        this.setComplaintTypeCode(complaintTypeCode);
        return this;
    }

    public void setComplaintTypeCode(String complaintTypeCode) {
        this.complaintTypeCode = complaintTypeCode;
    }

    public String getComplaintType() {
        return this.complaintType;
    }

    public CrbComplaintType complaintType(String complaintType) {
        this.setComplaintType(complaintType);
        return this;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintTypeDetails() {
        return this.complaintTypeDetails;
    }

    public CrbComplaintType complaintTypeDetails(String complaintTypeDetails) {
        this.setComplaintTypeDetails(complaintTypeDetails);
        return this;
    }

    public void setComplaintTypeDetails(String complaintTypeDetails) {
        this.complaintTypeDetails = complaintTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbComplaintType)) {
            return false;
        }
        return id != null && id.equals(((CrbComplaintType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbComplaintType{" +
            "id=" + getId() +
            ", complaintTypeCode='" + getComplaintTypeCode() + "'" +
            ", complaintType='" + getComplaintType() + "'" +
            ", complaintTypeDetails='" + getComplaintTypeDetails() + "'" +
            "}";
    }
}
