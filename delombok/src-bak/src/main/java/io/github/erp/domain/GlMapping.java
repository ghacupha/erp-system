package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

/**
 * A GlMapping.
 */
@Entity
@Table(name = "gl_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "glmapping")
public class GlMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sub_gl_code", nullable = false, unique = true)
    private String subGLCode;

    @Column(name = "sub_gl_description")
    private String subGLDescription;

    @NotNull
    @Column(name = "main_gl_code", nullable = false)
    private String mainGLCode;

    @Column(name = "main_gl_description")
    private String mainGLDescription;

    @NotNull
    @Column(name = "gl_type", nullable = false)
    private String glType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GlMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubGLCode() {
        return this.subGLCode;
    }

    public GlMapping subGLCode(String subGLCode) {
        this.setSubGLCode(subGLCode);
        return this;
    }

    public void setSubGLCode(String subGLCode) {
        this.subGLCode = subGLCode;
    }

    public String getSubGLDescription() {
        return this.subGLDescription;
    }

    public GlMapping subGLDescription(String subGLDescription) {
        this.setSubGLDescription(subGLDescription);
        return this;
    }

    public void setSubGLDescription(String subGLDescription) {
        this.subGLDescription = subGLDescription;
    }

    public String getMainGLCode() {
        return this.mainGLCode;
    }

    public GlMapping mainGLCode(String mainGLCode) {
        this.setMainGLCode(mainGLCode);
        return this;
    }

    public void setMainGLCode(String mainGLCode) {
        this.mainGLCode = mainGLCode;
    }

    public String getMainGLDescription() {
        return this.mainGLDescription;
    }

    public GlMapping mainGLDescription(String mainGLDescription) {
        this.setMainGLDescription(mainGLDescription);
        return this;
    }

    public void setMainGLDescription(String mainGLDescription) {
        this.mainGLDescription = mainGLDescription;
    }

    public String getGlType() {
        return this.glType;
    }

    public GlMapping glType(String glType) {
        this.setGlType(glType);
        return this;
    }

    public void setGlType(String glType) {
        this.glType = glType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlMapping)) {
            return false;
        }
        return id != null && id.equals(((GlMapping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlMapping{" +
            "id=" + getId() +
            ", subGLCode='" + getSubGLCode() + "'" +
            ", subGLDescription='" + getSubGLDescription() + "'" +
            ", mainGLCode='" + getMainGLCode() + "'" +
            ", mainGLDescription='" + getMainGLDescription() + "'" +
            ", glType='" + getGlType() + "'" +
            "}";
    }
}
