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
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationProcessor;
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationRequest;
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("liabilityEnumerationResourceProd")
@RequestMapping("/api/leases")
public class LiabilityEnumerationResourceProd {

    private static final Logger log = LoggerFactory.getLogger(LiabilityEnumerationResourceProd.class);

    private final LiabilityEnumerationProcessor liabilityEnumerationProcessor;

    public LiabilityEnumerationResourceProd(LiabilityEnumerationProcessor liabilityEnumerationProcessor) {
        this.liabilityEnumerationProcessor = liabilityEnumerationProcessor;
    }

    @PostMapping("/liability-enumerations")
    public ResponseEntity<LiabilityEnumerationResponse> enumerate(@Valid @RequestBody LiabilityEnumerationRequest request) {
        log.debug("REST request to enumerate present values for lease contract {}", request.getLeaseContractId());
        LiabilityEnumerationResponse response = liabilityEnumerationProcessor.enumerate(request);
        return ResponseEntity.ok(response);
    }
}
