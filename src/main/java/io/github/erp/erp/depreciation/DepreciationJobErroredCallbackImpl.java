package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationBatchSequenceService;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.DepreciationJobService;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class DepreciationJobErroredCallbackImpl implements DepreciationJobErroredCallback {

    private final static Logger log = LoggerFactory.getLogger(DepreciationJobErroredCallbackImpl.class);

    private final DepreciationJobService depreciationJobService;
    private final DepreciationJobNoticeService depreciationJobNoticeService;
    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    public DepreciationJobErroredCallbackImpl(DepreciationJobService depreciationJobService, DepreciationJobNoticeService depreciationJobNoticeService, DepreciationBatchSequenceService depreciationBatchSequenceService) {
        this.depreciationJobService = depreciationJobService;
        this.depreciationJobNoticeService = depreciationJobNoticeService;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
    }

    /**
     * Called when process has errored
     *
     * @param message
     */
    @Override
    public void onError(DepreciationBatchMessage message, Exception e) {

        log.error("Error processing batch-id id {}", message.getBatchId(), e);
        // Handle errors here, such as sending notifications or retries

        DepreciationJobNoticeDTO jobNotice = new DepreciationJobNoticeDTO();
        jobNotice.setEventTimeStamp(ZonedDateTime.now());
        jobNotice.setEventNarrative("Error encountered processing batch-id id ,"+ message.getBatchId());
        jobNotice.setErrorMessage(e.getMessage());
        jobNotice.setSourceModule("DepreciationBatchConsumer");

        depreciationJobNoticeService.save(jobNotice);

        depreciationJobService.findOne(Long.valueOf(message.getJobId())).ifPresent(depreciationJob -> {
            if (depreciationJob.getDepreciationJobStatus() != DepreciationJobStatusType.ERRORED) {
                depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.ERRORED);
                depreciationJobService.save(depreciationJob);

                log.info("The depreciation job id: {} {}, has errored at batch id: {}", depreciationJob.getId(), depreciationJob.getDescription(), message.getBatchId());
            }
        });

        updateBatchSequenceStatus(message.getBatchId());
    }

    private void updateBatchSequenceStatus(String batchId) {
        depreciationBatchSequenceService.findOne(Long.valueOf(batchId)).ifPresent(batchSequence -> {
            batchSequence.setDepreciationBatchStatus(DepreciationBatchStatusType.ERRORED);
            depreciationBatchSequenceService.save(batchSequence);
        });
    }
}
