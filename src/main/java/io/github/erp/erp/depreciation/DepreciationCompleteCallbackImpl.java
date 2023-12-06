package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationBatchSequenceService;
import org.springframework.stereotype.Service;

@Service
public class DepreciationCompleteCallbackImpl implements DepreciationCompleteCallback {

    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    public DepreciationCompleteCallbackImpl(DepreciationBatchSequenceService depreciationBatchSequenceService) {
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
    }

    @Override
    public void onComplete(DepreciationBatchMessage message) {
        // This method will be called after each depreciation batch is completed
        depreciationBatchSequenceService.findOne(Long.valueOf(message.getBatchId())).ifPresent(depreciationBatch -> {
            if (depreciationBatch.getDepreciationBatchStatus() != DepreciationBatchStatusType.COMPLETED) {
                depreciationBatch.setDepreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);
                depreciationBatchSequenceService.save(depreciationBatch);
            }
        });

    }
}

