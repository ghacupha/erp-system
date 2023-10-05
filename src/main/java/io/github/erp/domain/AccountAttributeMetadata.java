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

import io.github.erp.domain.enumeration.MandatoryFieldFlagTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AccountAttributeMetadata.
 */
@Entity
@Table(name = "account_attribute_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountattributemetadata")
public class AccountAttributeMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "precedence", nullable = false)
    private Integer precedence;

    @NotNull
    @Column(name = "column_name", nullable = false)
    private String columnName;

    @NotNull
    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "detailed_definition")
    private String detailedDefinition;

    @NotNull
    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "length")
    private Integer length;

    @Column(name = "column_index")
    private String columnIndex;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mandatory_field_flag", nullable = false)
    private MandatoryFieldFlagTypes mandatoryFieldFlag;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "business_validation")
    private String businessValidation;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "technical_validation")
    private String technicalValidation;

    @Column(name = "db_column_name")
    private String dbColumnName;

    @Column(name = "metadata_version")
    private Integer metadataVersion;

    @ManyToOne
    private GdiMasterDataIndex standardInputTemplate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountAttributeMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrecedence() {
        return this.precedence;
    }

    public AccountAttributeMetadata precedence(Integer precedence) {
        this.setPrecedence(precedence);
        return this;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public AccountAttributeMetadata columnName(String columnName) {
        this.setColumnName(columnName);
        return this;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public AccountAttributeMetadata shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDetailedDefinition() {
        return this.detailedDefinition;
    }

    public AccountAttributeMetadata detailedDefinition(String detailedDefinition) {
        this.setDetailedDefinition(detailedDefinition);
        return this;
    }

    public void setDetailedDefinition(String detailedDefinition) {
        this.detailedDefinition = detailedDefinition;
    }

    public String getDataType() {
        return this.dataType;
    }

    public AccountAttributeMetadata dataType(String dataType) {
        this.setDataType(dataType);
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getLength() {
        return this.length;
    }

    public AccountAttributeMetadata length(Integer length) {
        this.setLength(length);
        return this;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getColumnIndex() {
        return this.columnIndex;
    }

    public AccountAttributeMetadata columnIndex(String columnIndex) {
        this.setColumnIndex(columnIndex);
        return this;
    }

    public void setColumnIndex(String columnIndex) {
        this.columnIndex = columnIndex;
    }

    public MandatoryFieldFlagTypes getMandatoryFieldFlag() {
        return this.mandatoryFieldFlag;
    }

    public AccountAttributeMetadata mandatoryFieldFlag(MandatoryFieldFlagTypes mandatoryFieldFlag) {
        this.setMandatoryFieldFlag(mandatoryFieldFlag);
        return this;
    }

    public void setMandatoryFieldFlag(MandatoryFieldFlagTypes mandatoryFieldFlag) {
        this.mandatoryFieldFlag = mandatoryFieldFlag;
    }

    public String getBusinessValidation() {
        return this.businessValidation;
    }

    public AccountAttributeMetadata businessValidation(String businessValidation) {
        this.setBusinessValidation(businessValidation);
        return this;
    }

    public void setBusinessValidation(String businessValidation) {
        this.businessValidation = businessValidation;
    }

    public String getTechnicalValidation() {
        return this.technicalValidation;
    }

    public AccountAttributeMetadata technicalValidation(String technicalValidation) {
        this.setTechnicalValidation(technicalValidation);
        return this;
    }

    public void setTechnicalValidation(String technicalValidation) {
        this.technicalValidation = technicalValidation;
    }

    public String getDbColumnName() {
        return this.dbColumnName;
    }

    public AccountAttributeMetadata dbColumnName(String dbColumnName) {
        this.setDbColumnName(dbColumnName);
        return this;
    }

    public void setDbColumnName(String dbColumnName) {
        this.dbColumnName = dbColumnName;
    }

    public Integer getMetadataVersion() {
        return this.metadataVersion;
    }

    public AccountAttributeMetadata metadataVersion(Integer metadataVersion) {
        this.setMetadataVersion(metadataVersion);
        return this;
    }

    public void setMetadataVersion(Integer metadataVersion) {
        this.metadataVersion = metadataVersion;
    }

    public GdiMasterDataIndex getStandardInputTemplate() {
        return this.standardInputTemplate;
    }

    public void setStandardInputTemplate(GdiMasterDataIndex gdiMasterDataIndex) {
        this.standardInputTemplate = gdiMasterDataIndex;
    }

    public AccountAttributeMetadata standardInputTemplate(GdiMasterDataIndex gdiMasterDataIndex) {
        this.setStandardInputTemplate(gdiMasterDataIndex);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountAttributeMetadata)) {
            return false;
        }
        return id != null && id.equals(((AccountAttributeMetadata) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountAttributeMetadata{" +
            "id=" + getId() +
            ", precedence=" + getPrecedence() +
            ", columnName='" + getColumnName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", detailedDefinition='" + getDetailedDefinition() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", length=" + getLength() +
            ", columnIndex='" + getColumnIndex() + "'" +
            ", mandatoryFieldFlag='" + getMandatoryFieldFlag() + "'" +
            ", businessValidation='" + getBusinessValidation() + "'" +
            ", technicalValidation='" + getTechnicalValidation() + "'" +
            ", dbColumnName='" + getDbColumnName() + "'" +
            ", metadataVersion=" + getMetadataVersion() +
            "}";
    }
}
