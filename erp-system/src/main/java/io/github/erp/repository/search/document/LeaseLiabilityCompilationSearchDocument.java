package io.github.erp.repository.search.document;

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
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A flattened Elasticsearch representation of the LeaseLiabilityCompilation aggregate.
 */
@Document(indexName = "leaseliabilitycompilation-search")
public class LeaseLiabilityCompilationSearchDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private UUID requestId;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private ZonedDateTime timeOfRequest;

    @Field(type = FieldType.Boolean)
    private Boolean active;

    @Field(type = FieldType.Object)
    private RequestedBySummary requestedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public ZonedDateTime getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RequestedBySummary getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(RequestedBySummary requestedBy) {
        this.requestedBy = requestedBy;
    }

    public static class RequestedBySummary implements Serializable {

        private static final long serialVersionUID = 1L;

        @Field(type = FieldType.Long)
        private Long id;

        @Field(type = FieldType.Keyword)
        private UUID designation;

        @Field(type = FieldType.Keyword)
        private String applicationIdentity;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public UUID getDesignation() {
            return designation;
        }

        public void setDesignation(UUID designation) {
            this.designation = designation;
        }

        public String getApplicationIdentity() {
            return applicationIdentity;
        }

        public void setApplicationIdentity(String applicationIdentity) {
            this.applicationIdentity = applicationIdentity;
        }
    }
}
