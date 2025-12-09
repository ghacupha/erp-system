package io.github.erp.erp.resources.leases;

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
import io.github.erp.domain.PresentValueEnumeration;
import io.github.erp.repository.PresentValueEnumerationRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/leases")
public class PresentValueEnumerationResourceProd {

    private static final Logger log = LoggerFactory.getLogger(PresentValueEnumerationResourceProd.class);

    private final PresentValueEnumerationRepository presentValueEnumerationRepository;

    public PresentValueEnumerationResourceProd(PresentValueEnumerationRepository presentValueEnumerationRepository) {
        this.presentValueEnumerationRepository = presentValueEnumerationRepository;
    }

    @GetMapping("/present-value-enumerations")
    public ResponseEntity<List<PresentValueEnumeration>> getPresentValueEnumerations(
        @PageableDefault(size = 200, sort = "sequenceNumber", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(value = "liabilityEnumerationId", required = false) Long liabilityEnumerationId
    ) {
        log.debug("REST request to get PresentValueEnumerations for liability {}", liabilityEnumerationId);
        Page<PresentValueEnumeration> page =
            liabilityEnumerationId == null
                ? presentValueEnumerationRepository.findAll(pageable)
                : presentValueEnumerationRepository.findAllByLiabilityEnumerationId(liabilityEnumerationId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
