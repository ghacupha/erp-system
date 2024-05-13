package io.github.erp.internal.service.assets;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.internal.repository.InternalDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalDepreciationJobNoticeRepository;
import io.github.erp.internal.service.CascadeDeletionSequence;
import io.github.erp.service.DepreciationEntryService;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepreciationBatchCascadeDeletionSequence implements CascadeDeletionSequence<DepreciationBatchSequenceDTO> {

    private final DepreciationEntryService depreciationEntryService;
    private final DepreciationJobNoticeService depreciationJobNoticeService;

    private final InternalDepreciationJobNoticeRepository internalDepreciationJobNoticeRepository;
    private final InternalDepreciationEntryRepository internalDepreciationEntryRepository;

    public DepreciationBatchCascadeDeletionSequence (
        DepreciationEntryService depreciationEntryService,
        DepreciationJobNoticeService depreciationJobNoticeService,
        InternalDepreciationJobNoticeRepository internalDepreciationJobNoticeRepository,
        InternalDepreciationEntryRepository internalDepreciationEntryRepository) {
        this.depreciationEntryService = depreciationEntryService;
        this.depreciationJobNoticeService = depreciationJobNoticeService;
        this.internalDepreciationJobNoticeRepository = internalDepreciationJobNoticeRepository;
        this.internalDepreciationEntryRepository = internalDepreciationEntryRepository;
    }

    public DepreciationBatchSequenceDTO deleteCascade(DepreciationBatchSequenceDTO dto) {

        internalDepreciationEntryRepository.findDepreciationEntryByDepreciationBatchSequenceId(dto.getId(), Pageable.ofSize(Integer.MAX_VALUE))
            .forEach(depreciationEntry -> depreciationEntryService.delete(depreciationEntry.getId()));

        internalDepreciationJobNoticeRepository.findDepreciationJobNoticeByDepreciationBatchSequenceId(dto.getId(), Pageable.ofSize(Integer.MAX_VALUE))
            .forEach(depreciationJobNotice -> depreciationJobNoticeService.delete(depreciationJobNotice.getId()));

        return dto;
    }
}
