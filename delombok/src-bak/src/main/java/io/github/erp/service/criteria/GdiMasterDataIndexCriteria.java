package io.github.erp.service.criteria;

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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.GdiMasterDataIndex} entity. This class is used
 * in {@link io.github.erp.web.rest.GdiMasterDataIndexResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gdi-master-data-indices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GdiMasterDataIndexCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter entityName;

    private StringFilter databaseName;

    private StringFilter dataPath;

    private Boolean distinct;

    public GdiMasterDataIndexCriteria() {}

    public GdiMasterDataIndexCriteria(GdiMasterDataIndexCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entityName = other.entityName == null ? null : other.entityName.copy();
        this.databaseName = other.databaseName == null ? null : other.databaseName.copy();
        this.dataPath = other.dataPath == null ? null : other.dataPath.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GdiMasterDataIndexCriteria copy() {
        return new GdiMasterDataIndexCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEntityName() {
        return entityName;
    }

    public StringFilter entityName() {
        if (entityName == null) {
            entityName = new StringFilter();
        }
        return entityName;
    }

    public void setEntityName(StringFilter entityName) {
        this.entityName = entityName;
    }

    public StringFilter getDatabaseName() {
        return databaseName;
    }

    public StringFilter databaseName() {
        if (databaseName == null) {
            databaseName = new StringFilter();
        }
        return databaseName;
    }

    public void setDatabaseName(StringFilter databaseName) {
        this.databaseName = databaseName;
    }

    public StringFilter getDataPath() {
        return dataPath;
    }

    public StringFilter dataPath() {
        if (dataPath == null) {
            dataPath = new StringFilter();
        }
        return dataPath;
    }

    public void setDataPath(StringFilter dataPath) {
        this.dataPath = dataPath;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GdiMasterDataIndexCriteria that = (GdiMasterDataIndexCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entityName, that.entityName) &&
            Objects.equals(databaseName, that.databaseName) &&
            Objects.equals(dataPath, that.dataPath) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, databaseName, dataPath, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GdiMasterDataIndexCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entityName != null ? "entityName=" + entityName + ", " : "") +
            (databaseName != null ? "databaseName=" + databaseName + ", " : "") +
            (dataPath != null ? "dataPath=" + dataPath + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
