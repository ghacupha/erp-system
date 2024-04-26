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
import io.github.erp.domain.enumeration.SubmittedFileStatusTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CrbFileTransmissionStatus.
 */
@Entity
@Table(name = "crb_file_transmission_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbfiletransmissionstatus")
public class CrbFileTransmissionStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "submitted_file_status_type_code", nullable = false, unique = true)
    private String submittedFileStatusTypeCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "submitted_file_status_type", nullable = false)
    private SubmittedFileStatusTypes submittedFileStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "submitted_file_status_type_description")
    private String submittedFileStatusTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbFileTransmissionStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmittedFileStatusTypeCode() {
        return this.submittedFileStatusTypeCode;
    }

    public CrbFileTransmissionStatus submittedFileStatusTypeCode(String submittedFileStatusTypeCode) {
        this.setSubmittedFileStatusTypeCode(submittedFileStatusTypeCode);
        return this;
    }

    public void setSubmittedFileStatusTypeCode(String submittedFileStatusTypeCode) {
        this.submittedFileStatusTypeCode = submittedFileStatusTypeCode;
    }

    public SubmittedFileStatusTypes getSubmittedFileStatusType() {
        return this.submittedFileStatusType;
    }

    public CrbFileTransmissionStatus submittedFileStatusType(SubmittedFileStatusTypes submittedFileStatusType) {
        this.setSubmittedFileStatusType(submittedFileStatusType);
        return this;
    }

    public void setSubmittedFileStatusType(SubmittedFileStatusTypes submittedFileStatusType) {
        this.submittedFileStatusType = submittedFileStatusType;
    }

    public String getSubmittedFileStatusTypeDescription() {
        return this.submittedFileStatusTypeDescription;
    }

    public CrbFileTransmissionStatus submittedFileStatusTypeDescription(String submittedFileStatusTypeDescription) {
        this.setSubmittedFileStatusTypeDescription(submittedFileStatusTypeDescription);
        return this;
    }

    public void setSubmittedFileStatusTypeDescription(String submittedFileStatusTypeDescription) {
        this.submittedFileStatusTypeDescription = submittedFileStatusTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbFileTransmissionStatus)) {
            return false;
        }
        return id != null && id.equals(((CrbFileTransmissionStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbFileTransmissionStatus{" +
            "id=" + getId() +
            ", submittedFileStatusTypeCode='" + getSubmittedFileStatusTypeCode() + "'" +
            ", submittedFileStatusType='" + getSubmittedFileStatusType() + "'" +
            ", submittedFileStatusTypeDescription='" + getSubmittedFileStatusTypeDescription() + "'" +
            "}";
    }
}
