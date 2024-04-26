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
import io.github.erp.internal.repository.InternalDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalDepreciationJobNoticeRepository;
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
