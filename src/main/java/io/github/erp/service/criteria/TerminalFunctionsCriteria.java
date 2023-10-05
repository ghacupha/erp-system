package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.TerminalFunctions} entity. This class is used
 * in {@link io.github.erp.web.rest.TerminalFunctionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /terminal-functions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TerminalFunctionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter functionCode;

    private StringFilter terminalFunctionality;

    private Boolean distinct;

    public TerminalFunctionsCriteria() {}

    public TerminalFunctionsCriteria(TerminalFunctionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.functionCode = other.functionCode == null ? null : other.functionCode.copy();
        this.terminalFunctionality = other.terminalFunctionality == null ? null : other.terminalFunctionality.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TerminalFunctionsCriteria copy() {
        return new TerminalFunctionsCriteria(this);
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

    public StringFilter getFunctionCode() {
        return functionCode;
    }

    public StringFilter functionCode() {
        if (functionCode == null) {
            functionCode = new StringFilter();
        }
        return functionCode;
    }

    public void setFunctionCode(StringFilter functionCode) {
        this.functionCode = functionCode;
    }

    public StringFilter getTerminalFunctionality() {
        return terminalFunctionality;
    }

    public StringFilter terminalFunctionality() {
        if (terminalFunctionality == null) {
            terminalFunctionality = new StringFilter();
        }
        return terminalFunctionality;
    }

    public void setTerminalFunctionality(StringFilter terminalFunctionality) {
        this.terminalFunctionality = terminalFunctionality;
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
        final TerminalFunctionsCriteria that = (TerminalFunctionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(functionCode, that.functionCode) &&
            Objects.equals(terminalFunctionality, that.terminalFunctionality) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, functionCode, terminalFunctionality, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalFunctionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (functionCode != null ? "functionCode=" + functionCode + ", " : "") +
            (terminalFunctionality != null ? "terminalFunctionality=" + terminalFunctionality + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
