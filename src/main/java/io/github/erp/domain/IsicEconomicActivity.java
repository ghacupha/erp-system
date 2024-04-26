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
 * A IsicEconomicActivity.
 */
@Entity
@Table(name = "isic_economic_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "isiceconomicactivity")
public class IsicEconomicActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "business_economic_activity_code", nullable = false, unique = true)
    private String businessEconomicActivityCode;

    @NotNull
    @Column(name = "section", nullable = false)
    private String section;

    @NotNull
    @Column(name = "section_label", nullable = false)
    private String sectionLabel;

    @NotNull
    @Column(name = "division", nullable = false)
    private String division;

    @NotNull
    @Column(name = "division_label", nullable = false)
    private String divisionLabel;

    @Column(name = "group_code")
    private String groupCode;

    @NotNull
    @Column(name = "group_label", nullable = false)
    private String groupLabel;

    @NotNull
    @Column(name = "class_code", nullable = false)
    private String classCode;

    @Column(name = "business_economic_activity_type")
    private String businessEconomicActivityType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "business_economic_activity_type_description")
    private String businessEconomicActivityTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IsicEconomicActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessEconomicActivityCode() {
        return this.businessEconomicActivityCode;
    }

    public IsicEconomicActivity businessEconomicActivityCode(String businessEconomicActivityCode) {
        this.setBusinessEconomicActivityCode(businessEconomicActivityCode);
        return this;
    }

    public void setBusinessEconomicActivityCode(String businessEconomicActivityCode) {
        this.businessEconomicActivityCode = businessEconomicActivityCode;
    }

    public String getSection() {
        return this.section;
    }

    public IsicEconomicActivity section(String section) {
        this.setSection(section);
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSectionLabel() {
        return this.sectionLabel;
    }

    public IsicEconomicActivity sectionLabel(String sectionLabel) {
        this.setSectionLabel(sectionLabel);
        return this;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public String getDivision() {
        return this.division;
    }

    public IsicEconomicActivity division(String division) {
        this.setDivision(division);
        return this;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDivisionLabel() {
        return this.divisionLabel;
    }

    public IsicEconomicActivity divisionLabel(String divisionLabel) {
        this.setDivisionLabel(divisionLabel);
        return this;
    }

    public void setDivisionLabel(String divisionLabel) {
        this.divisionLabel = divisionLabel;
    }

    public String getGroupCode() {
        return this.groupCode;
    }

    public IsicEconomicActivity groupCode(String groupCode) {
        this.setGroupCode(groupCode);
        return this;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupLabel() {
        return this.groupLabel;
    }

    public IsicEconomicActivity groupLabel(String groupLabel) {
        this.setGroupLabel(groupLabel);
        return this;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public String getClassCode() {
        return this.classCode;
    }

    public IsicEconomicActivity classCode(String classCode) {
        this.setClassCode(classCode);
        return this;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getBusinessEconomicActivityType() {
        return this.businessEconomicActivityType;
    }

    public IsicEconomicActivity businessEconomicActivityType(String businessEconomicActivityType) {
        this.setBusinessEconomicActivityType(businessEconomicActivityType);
        return this;
    }

    public void setBusinessEconomicActivityType(String businessEconomicActivityType) {
        this.businessEconomicActivityType = businessEconomicActivityType;
    }

    public String getBusinessEconomicActivityTypeDescription() {
        return this.businessEconomicActivityTypeDescription;
    }

    public IsicEconomicActivity businessEconomicActivityTypeDescription(String businessEconomicActivityTypeDescription) {
        this.setBusinessEconomicActivityTypeDescription(businessEconomicActivityTypeDescription);
        return this;
    }

    public void setBusinessEconomicActivityTypeDescription(String businessEconomicActivityTypeDescription) {
        this.businessEconomicActivityTypeDescription = businessEconomicActivityTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsicEconomicActivity)) {
            return false;
        }
        return id != null && id.equals(((IsicEconomicActivity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsicEconomicActivity{" +
            "id=" + getId() +
            ", businessEconomicActivityCode='" + getBusinessEconomicActivityCode() + "'" +
            ", section='" + getSection() + "'" +
            ", sectionLabel='" + getSectionLabel() + "'" +
            ", division='" + getDivision() + "'" +
            ", divisionLabel='" + getDivisionLabel() + "'" +
            ", groupCode='" + getGroupCode() + "'" +
            ", groupLabel='" + getGroupLabel() + "'" +
            ", classCode='" + getClassCode() + "'" +
            ", businessEconomicActivityType='" + getBusinessEconomicActivityType() + "'" +
            ", businessEconomicActivityTypeDescription='" + getBusinessEconomicActivityTypeDescription() + "'" +
            "}";
    }
}
