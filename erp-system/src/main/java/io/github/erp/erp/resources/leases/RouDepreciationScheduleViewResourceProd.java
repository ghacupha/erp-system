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

import io.github.erp.internal.model.RouDepreciationScheduleViewInternal;
import io.github.erp.internal.service.rou.RouDepreciationScheduleViewService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("rouDepreciationScheduleViewResourceProd")
@RequestMapping("/api/leases")
public class RouDepreciationScheduleViewResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationScheduleViewResourceProd.class);

    private final RouDepreciationScheduleViewService rouDepreciationScheduleViewService;

    public RouDepreciationScheduleViewResourceProd(RouDepreciationScheduleViewService rouDepreciationScheduleViewService) {
        this.rouDepreciationScheduleViewService = rouDepreciationScheduleViewService;
    }

    @GetMapping("/rou-depreciation-schedule-view/{leaseContractId}")
    public ResponseEntity<List<RouDepreciationScheduleViewInternal>> getRouDepreciationScheduleView(
        @PathVariable Long leaseContractId
    ) {
        log.debug("REST request to get ROU depreciation schedule view for lease contract id: {}", leaseContractId);
        List<RouDepreciationScheduleViewInternal> schedule = rouDepreciationScheduleViewService.getScheduleView(leaseContractId);
        return ResponseEntity.ok(schedule);
    }
}
