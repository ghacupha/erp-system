package io.github.erp.internal.service;

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
import io.github.erp.internal.repository.InternalDepreciationBatchSequenceRepository;
import io.github.erp.internal.repository.InternalDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalDepreciationJobNoticeRepository;
import io.github.erp.service.DepreciationBatchSequenceService;
import io.github.erp.service.DepreciationEntryService;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.dto.DepreciationJobDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepreciationJobCascadeDeletionSequenceImpl implements CascadeDeletionSequence<DepreciationJobDTO> {

    private final DepreciationEntryService depreciationEntryService;
    private final DepreciationJobNoticeService depreciationJobNoticeService;
    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    private final InternalDepreciationBatchSequenceRepository internalDepreciationBatchSequenceRepository;
    private final InternalDepreciationJobNoticeRepository internalDepreciationJobNoticeRepository;
    private final InternalDepreciationEntryRepository internalDepreciationEntryRepository;

    public DepreciationJobCascadeDeletionSequenceImpl(
        DepreciationEntryService depreciationEntryService,
        DepreciationJobNoticeService depreciationJobNoticeService,
        DepreciationBatchSequenceService depreciationBatchSequenceService,
        InternalDepreciationBatchSequenceRepository internalDepreciationBatchSequenceRepository,
        InternalDepreciationJobNoticeRepository internalDepreciationJobNoticeRepository,
        InternalDepreciationEntryRepository internalDepreciationEntryRepository) {
        this.depreciationEntryService = depreciationEntryService;
        this.depreciationJobNoticeService = depreciationJobNoticeService;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.internalDepreciationBatchSequenceRepository = internalDepreciationBatchSequenceRepository;
        this.internalDepreciationJobNoticeRepository = internalDepreciationJobNoticeRepository;
        this.internalDepreciationEntryRepository = internalDepreciationEntryRepository;
    }

    public DepreciationJobDTO deleteCascade(DepreciationJobDTO job) {

        internalDepreciationEntryRepository.findDepreciationEntryByDepreciationJobId(job.getId(), Pageable.ofSize(Integer.MAX_VALUE))
            .forEach(depreciationEntry -> {
                depreciationEntryService.delete(depreciationEntry.getId());
            });
        internalDepreciationJobNoticeRepository.findDepreciationJobNoticeByDepreciationJobId(job.getId(), Pageable.ofSize(Integer.MAX_VALUE))
            .forEach(depreciationJobNotice -> {
                depreciationJobNoticeService.delete(depreciationJobNotice.getId());
            });
        internalDepreciationBatchSequenceRepository.findByDepreciationJobId(job.getId(), Pageable.ofSize(Integer.MAX_VALUE))
            .forEach(depreciationBatchSequence -> {
                depreciationBatchSequenceService.delete(depreciationBatchSequence.getId());
            });

        return job;
    }
}
