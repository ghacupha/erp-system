package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CrbRecordFileType.
 */
@Entity
@Table(name = "crb_record_file_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbrecordfiletype")
public class CrbRecordFileType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "record_file_type_code", nullable = false, unique = true)
    private String recordFileTypeCode;

    @NotNull
    @Column(name = "record_file_type", nullable = false, unique = true)
    private String recordFileType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "record_file_type_details")
    private String recordFileTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbRecordFileType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordFileTypeCode() {
        return this.recordFileTypeCode;
    }

    public CrbRecordFileType recordFileTypeCode(String recordFileTypeCode) {
        this.setRecordFileTypeCode(recordFileTypeCode);
        return this;
    }

    public void setRecordFileTypeCode(String recordFileTypeCode) {
        this.recordFileTypeCode = recordFileTypeCode;
    }

    public String getRecordFileType() {
        return this.recordFileType;
    }

    public CrbRecordFileType recordFileType(String recordFileType) {
        this.setRecordFileType(recordFileType);
        return this;
    }

    public void setRecordFileType(String recordFileType) {
        this.recordFileType = recordFileType;
    }

    public String getRecordFileTypeDetails() {
        return this.recordFileTypeDetails;
    }

    public CrbRecordFileType recordFileTypeDetails(String recordFileTypeDetails) {
        this.setRecordFileTypeDetails(recordFileTypeDetails);
        return this;
    }

    public void setRecordFileTypeDetails(String recordFileTypeDetails) {
        this.recordFileTypeDetails = recordFileTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbRecordFileType)) {
            return false;
        }
        return id != null && id.equals(((CrbRecordFileType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbRecordFileType{" +
            "id=" + getId() +
            ", recordFileTypeCode='" + getRecordFileTypeCode() + "'" +
            ", recordFileType='" + getRecordFileType() + "'" +
            ", recordFileTypeDetails='" + getRecordFileTypeDetails() + "'" +
            "}";
    }
}
