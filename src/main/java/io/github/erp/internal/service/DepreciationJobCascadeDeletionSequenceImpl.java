package io.github.erp.internal.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
