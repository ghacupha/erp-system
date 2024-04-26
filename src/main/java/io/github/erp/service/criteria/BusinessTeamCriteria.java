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

/**
 * Criteria class for the {@link io.github.erp.domain.BusinessTeam} entity. This class is used
 * in {@link io.github.erp.web.rest.BusinessTeamResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /business-teams?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusinessTeamCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter businessTeam;

    private LongFilter teamMembersId;

    private Boolean distinct;

    public BusinessTeamCriteria() {}

    public BusinessTeamCriteria(BusinessTeamCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.businessTeam = other.businessTeam == null ? null : other.businessTeam.copy();
        this.teamMembersId = other.teamMembersId == null ? null : other.teamMembersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusinessTeamCriteria copy() {
        return new BusinessTeamCriteria(this);
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

    public StringFilter getBusinessTeam() {
        return businessTeam;
    }

    public StringFilter businessTeam() {
        if (businessTeam == null) {
            businessTeam = new StringFilter();
        }
        return businessTeam;
    }

    public void setBusinessTeam(StringFilter businessTeam) {
        this.businessTeam = businessTeam;
    }

    public LongFilter getTeamMembersId() {
        return teamMembersId;
    }

    public LongFilter teamMembersId() {
        if (teamMembersId == null) {
            teamMembersId = new LongFilter();
        }
        return teamMembersId;
    }

    public void setTeamMembersId(LongFilter teamMembersId) {
        this.teamMembersId = teamMembersId;
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
        final BusinessTeamCriteria that = (BusinessTeamCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(businessTeam, that.businessTeam) &&
            Objects.equals(teamMembersId, that.teamMembersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, businessTeam, teamMembersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessTeamCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (businessTeam != null ? "businessTeam=" + businessTeam + ", " : "") +
            (teamMembersId != null ? "teamMembersId=" + teamMembersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
