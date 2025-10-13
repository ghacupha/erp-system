package io.github.erp.erp.assets.depreciation;

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
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepreciationJobCompleteCallbackImpl implements DepreciationJobCompleteCallback {

    private final static Logger log = LoggerFactory.getLogger(DepreciationJobCompleteCallbackImpl.class);

    private final DepreciationJobService depreciationJobService;

    public DepreciationJobCompleteCallbackImpl(DepreciationJobService depreciationJobService) {
        this.depreciationJobService = depreciationJobService;
    }

    /**
     * Called when process is complete
     *
     * @param message
     */
    @Override
    public void onComplete(DepreciationBatchMessage message) {

        depreciationJobService.findOne(Long.valueOf(message.getJobId())).ifPresent(depreciationJob -> {
            if (depreciationJob.getDepreciationJobStatus() != DepreciationJobStatusType.COMPLETE) {
                depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
                depreciationJobService.save(depreciationJob);

                log.info("The depreciation job id: {} {}, has been completed", depreciationJob.getId(), depreciationJob.getDescription());
            }
        });
    }
}
