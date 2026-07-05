package io.github.erp.repository.search.mapper;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.repository.search.document.LeaseLiabilityCompilationSearchDocument;
import io.github.erp.repository.search.document.LeaseLiabilityCompilationSearchDocument.RequestedBySummary;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Converts {@link LeaseLiabilityCompilation} aggregates into their search documents and back into DTOs.
 */
@Component
public class LeaseLiabilityCompilationSearchMapper {

    public LeaseLiabilityCompilationSearchDocument toDocument(LeaseLiabilityCompilation source) {
        if (source == null) {
            return null;
        }
        LeaseLiabilityCompilationSearchDocument document = new LeaseLiabilityCompilationSearchDocument();
        document.setId(source.getId());
        document.setRequestId(source.getRequestId());
        document.setTimeOfRequest(source.getTimeOfRequest());
        document.setActive(source.getActive());
        document.setRequestedBy(toRequestedBySummary(source.getRequestedBy()));
        return document;
    }

    public LeaseLiabilityCompilationDTO toDto(LeaseLiabilityCompilationSearchDocument document) {
        if (document == null) {
            return null;
        }
        LeaseLiabilityCompilationDTO dto = new LeaseLiabilityCompilationDTO();
        dto.setId(document.getId());
        dto.setRequestId(document.getRequestId());
        dto.setTimeOfRequest(document.getTimeOfRequest());
        dto.setActive(Optional.ofNullable(document.getActive()).orElse(Boolean.FALSE));
        dto.setRequestedBy(toRequestedByDto(document.getRequestedBy()));
        return dto;
    }

    private RequestedBySummary toRequestedBySummary(ApplicationUser requestedBy) {
        if (requestedBy == null) {
            return null;
        }
        RequestedBySummary summary = new RequestedBySummary();
        summary.setId(requestedBy.getId());
        summary.setDesignation(requestedBy.getDesignation());
        summary.setApplicationIdentity(requestedBy.getApplicationIdentity());
        return summary;
    }

    private ApplicationUserDTO toRequestedByDto(RequestedBySummary summary) {
        if (summary == null) {
            return null;
        }
        ApplicationUserDTO dto = new ApplicationUserDTO();
        dto.setId(summary.getId());
        dto.setDesignation(summary.getDesignation());
        dto.setApplicationIdentity(summary.getApplicationIdentity());
        return dto;
    }
}
