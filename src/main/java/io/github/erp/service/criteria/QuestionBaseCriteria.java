package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.enumeration.ControlTypes;
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
 * Criteria class for the {@link io.github.erp.domain.QuestionBase} entity. This class is used
 * in {@link io.github.erp.web.rest.QuestionBaseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /question-bases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionBaseCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ControlTypes
     */
    public static class ControlTypesFilter extends Filter<ControlTypes> {

        public ControlTypesFilter() {}

        public ControlTypesFilter(ControlTypesFilter filter) {
            super(filter);
        }

        @Override
        public ControlTypesFilter copy() {
            return new ControlTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter context;

    private UUIDFilter serial;

    private StringFilter questionBaseValue;

    private StringFilter questionBaseKey;

    private StringFilter questionBaseLabel;

    private BooleanFilter required;

    private IntegerFilter order;

    private ControlTypesFilter controlType;

    private StringFilter placeholder;

    private BooleanFilter iterable;

    private LongFilter parametersId;

    private LongFilter placeholderItemId;

    private Boolean distinct;

    public QuestionBaseCriteria() {}

    public QuestionBaseCriteria(QuestionBaseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.context = other.context == null ? null : other.context.copy();
        this.serial = other.serial == null ? null : other.serial.copy();
        this.questionBaseValue = other.questionBaseValue == null ? null : other.questionBaseValue.copy();
        this.questionBaseKey = other.questionBaseKey == null ? null : other.questionBaseKey.copy();
        this.questionBaseLabel = other.questionBaseLabel == null ? null : other.questionBaseLabel.copy();
        this.required = other.required == null ? null : other.required.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.controlType = other.controlType == null ? null : other.controlType.copy();
        this.placeholder = other.placeholder == null ? null : other.placeholder.copy();
        this.iterable = other.iterable == null ? null : other.iterable.copy();
        this.parametersId = other.parametersId == null ? null : other.parametersId.copy();
        this.placeholderItemId = other.placeholderItemId == null ? null : other.placeholderItemId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionBaseCriteria copy() {
        return new QuestionBaseCriteria(this);
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

    public StringFilter getContext() {
        return context;
    }

    public StringFilter context() {
        if (context == null) {
            context = new StringFilter();
        }
        return context;
    }

    public void setContext(StringFilter context) {
        this.context = context;
    }

    public UUIDFilter getSerial() {
        return serial;
    }

    public UUIDFilter serial() {
        if (serial == null) {
            serial = new UUIDFilter();
        }
        return serial;
    }

    public void setSerial(UUIDFilter serial) {
        this.serial = serial;
    }

    public StringFilter getQuestionBaseValue() {
        return questionBaseValue;
    }

    public StringFilter questionBaseValue() {
        if (questionBaseValue == null) {
            questionBaseValue = new StringFilter();
        }
        return questionBaseValue;
    }

    public void setQuestionBaseValue(StringFilter questionBaseValue) {
        this.questionBaseValue = questionBaseValue;
    }

    public StringFilter getQuestionBaseKey() {
        return questionBaseKey;
    }

    public StringFilter questionBaseKey() {
        if (questionBaseKey == null) {
            questionBaseKey = new StringFilter();
        }
        return questionBaseKey;
    }

    public void setQuestionBaseKey(StringFilter questionBaseKey) {
        this.questionBaseKey = questionBaseKey;
    }

    public StringFilter getQuestionBaseLabel() {
        return questionBaseLabel;
    }

    public StringFilter questionBaseLabel() {
        if (questionBaseLabel == null) {
            questionBaseLabel = new StringFilter();
        }
        return questionBaseLabel;
    }

    public void setQuestionBaseLabel(StringFilter questionBaseLabel) {
        this.questionBaseLabel = questionBaseLabel;
    }

    public BooleanFilter getRequired() {
        return required;
    }

    public BooleanFilter required() {
        if (required == null) {
            required = new BooleanFilter();
        }
        return required;
    }

    public void setRequired(BooleanFilter required) {
        this.required = required;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public ControlTypesFilter getControlType() {
        return controlType;
    }

    public ControlTypesFilter controlType() {
        if (controlType == null) {
            controlType = new ControlTypesFilter();
        }
        return controlType;
    }

    public void setControlType(ControlTypesFilter controlType) {
        this.controlType = controlType;
    }

    public StringFilter getPlaceholder() {
        return placeholder;
    }

    public StringFilter placeholder() {
        if (placeholder == null) {
            placeholder = new StringFilter();
        }
        return placeholder;
    }

    public void setPlaceholder(StringFilter placeholder) {
        this.placeholder = placeholder;
    }

    public BooleanFilter getIterable() {
        return iterable;
    }

    public BooleanFilter iterable() {
        if (iterable == null) {
            iterable = new BooleanFilter();
        }
        return iterable;
    }

    public void setIterable(BooleanFilter iterable) {
        this.iterable = iterable;
    }

    public LongFilter getParametersId() {
        return parametersId;
    }

    public LongFilter parametersId() {
        if (parametersId == null) {
            parametersId = new LongFilter();
        }
        return parametersId;
    }

    public void setParametersId(LongFilter parametersId) {
        this.parametersId = parametersId;
    }

    public LongFilter getPlaceholderItemId() {
        return placeholderItemId;
    }

    public LongFilter placeholderItemId() {
        if (placeholderItemId == null) {
            placeholderItemId = new LongFilter();
        }
        return placeholderItemId;
    }

    public void setPlaceholderItemId(LongFilter placeholderItemId) {
        this.placeholderItemId = placeholderItemId;
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
        final QuestionBaseCriteria that = (QuestionBaseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(context, that.context) &&
            Objects.equals(serial, that.serial) &&
            Objects.equals(questionBaseValue, that.questionBaseValue) &&
            Objects.equals(questionBaseKey, that.questionBaseKey) &&
            Objects.equals(questionBaseLabel, that.questionBaseLabel) &&
            Objects.equals(required, that.required) &&
            Objects.equals(order, that.order) &&
            Objects.equals(controlType, that.controlType) &&
            Objects.equals(placeholder, that.placeholder) &&
            Objects.equals(iterable, that.iterable) &&
            Objects.equals(parametersId, that.parametersId) &&
            Objects.equals(placeholderItemId, that.placeholderItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            context,
            serial,
            questionBaseValue,
            questionBaseKey,
            questionBaseLabel,
            required,
            order,
            controlType,
            placeholder,
            iterable,
            parametersId,
            placeholderItemId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionBaseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (context != null ? "context=" + context + ", " : "") +
            (serial != null ? "serial=" + serial + ", " : "") +
            (questionBaseValue != null ? "questionBaseValue=" + questionBaseValue + ", " : "") +
            (questionBaseKey != null ? "questionBaseKey=" + questionBaseKey + ", " : "") +
            (questionBaseLabel != null ? "questionBaseLabel=" + questionBaseLabel + ", " : "") +
            (required != null ? "required=" + required + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (controlType != null ? "controlType=" + controlType + ", " : "") +
            (placeholder != null ? "placeholder=" + placeholder + ", " : "") +
            (iterable != null ? "iterable=" + iterable + ", " : "") +
            (parametersId != null ? "parametersId=" + parametersId + ", " : "") +
            (placeholderItemId != null ? "placeholderItemId=" + placeholderItemId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
