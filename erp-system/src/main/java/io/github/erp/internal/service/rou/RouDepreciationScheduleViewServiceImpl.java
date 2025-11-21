package io.github.erp.internal.service.rou;

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
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RouDepreciationScheduleViewServiceImpl implements RouDepreciationScheduleViewService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationScheduleViewServiceImpl.class);

    private final InternalRouDepreciationEntryRepository rouDepreciationEntryRepository;

    public RouDepreciationScheduleViewServiceImpl(InternalRouDepreciationEntryRepository rouDepreciationEntryRepository) {
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
    }

    @Override
    public List<RouDepreciationScheduleViewInternal> getScheduleView(Long leaseContractId) {
        log.debug("Request to get ROU depreciation schedule view for lease contract: {}", leaseContractId);
        return rouDepreciationEntryRepository.getRouDepreciationScheduleView(leaseContractId);
    }
}
