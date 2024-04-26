package io.github.erp.service.criteria;

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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PrepaymentMapping} entity. This class is used
 * in {@link io.github.erp.web.rest.PrepaymentMappingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prepayment-mappings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentMappingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter parameterKey;

    private UUIDFilter parameterGuid;

    private StringFilter parameter;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PrepaymentMappingCriteria() {}

    public PrepaymentMappingCriteria(PrepaymentMappingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.parameterKey = other.parameterKey == null ? null : other.parameterKey.copy();
        this.parameterGuid = other.parameterGuid == null ? null : other.parameterGuid.copy();
        this.parameter = other.parameter == null ? null : other.parameter.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrepaymentMappingCriteria copy() {
        return new PrepaymentMappingCriteria(this);
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

    public StringFilter getParameterKey() {
        return parameterKey;
    }

    public StringFilter parameterKey() {
        if (parameterKey == null) {
            parameterKey = new StringFilter();
        }
        return parameterKey;
    }

    public void setParameterKey(StringFilter parameterKey) {
        this.parameterKey = parameterKey;
    }

    public UUIDFilter getParameterGuid() {
        return parameterGuid;
    }

    public UUIDFilter parameterGuid() {
        if (parameterGuid == null) {
            parameterGuid = new UUIDFilter();
        }
        return parameterGuid;
    }

    public void setParameterGuid(UUIDFilter parameterGuid) {
        this.parameterGuid = parameterGuid;
    }

    public StringFilter getParameter() {
        return parameter;
    }

    public StringFilter parameter() {
        if (parameter == null) {
            parameter = new StringFilter();
        }
        return parameter;
    }

    public void setParameter(StringFilter parameter) {
        this.parameter = parameter;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
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
        final PrepaymentMappingCriteria that = (PrepaymentMappingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(parameterKey, that.parameterKey) &&
            Objects.equals(parameterGuid, that.parameterGuid) &&
            Objects.equals(parameter, that.parameter) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parameterKey, parameterGuid, parameter, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMappingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (parameterKey != null ? "parameterKey=" + parameterKey + ", " : "") +
            (parameterGuid != null ? "parameterGuid=" + parameterGuid + ", " : "") +
            (parameter != null ? "parameter=" + parameter + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
