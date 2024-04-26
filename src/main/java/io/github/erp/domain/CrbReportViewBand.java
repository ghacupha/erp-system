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
 * A CrbReportViewBand.
 */
@Entity
@Table(name = "crb_report_view_band")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbreportviewband")
public class CrbReportViewBand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_view_code", nullable = false, unique = true)
    private String reportViewCode;

    @NotNull
    @Column(name = "report_view_category", nullable = false, unique = true)
    private String reportViewCategory;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "report_view_category_description")
    private String reportViewCategoryDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbReportViewBand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportViewCode() {
        return this.reportViewCode;
    }

    public CrbReportViewBand reportViewCode(String reportViewCode) {
        this.setReportViewCode(reportViewCode);
        return this;
    }

    public void setReportViewCode(String reportViewCode) {
        this.reportViewCode = reportViewCode;
    }

    public String getReportViewCategory() {
        return this.reportViewCategory;
    }

    public CrbReportViewBand reportViewCategory(String reportViewCategory) {
        this.setReportViewCategory(reportViewCategory);
        return this;
    }

    public void setReportViewCategory(String reportViewCategory) {
        this.reportViewCategory = reportViewCategory;
    }

    public String getReportViewCategoryDescription() {
        return this.reportViewCategoryDescription;
    }

    public CrbReportViewBand reportViewCategoryDescription(String reportViewCategoryDescription) {
        this.setReportViewCategoryDescription(reportViewCategoryDescription);
        return this;
    }

    public void setReportViewCategoryDescription(String reportViewCategoryDescription) {
        this.reportViewCategoryDescription = reportViewCategoryDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbReportViewBand)) {
            return false;
        }
        return id != null && id.equals(((CrbReportViewBand) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbReportViewBand{" +
            "id=" + getId() +
            ", reportViewCode='" + getReportViewCode() + "'" +
            ", reportViewCategory='" + getReportViewCategory() + "'" +
            ", reportViewCategoryDescription='" + getReportViewCategoryDescription() + "'" +
            "}";
    }
}
