package io.github.erp.service;
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
import io.github.erp.service.dto.LeaseTemplateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeaseTemplate}.
 */
public interface LeaseTemplateService {
    LeaseTemplateDTO save(LeaseTemplateDTO leaseTemplateDTO);

    Optional<LeaseTemplateDTO> partialUpdate(LeaseTemplateDTO leaseTemplateDTO);

    Page<LeaseTemplateDTO> findAll(Pageable pageable);

    Optional<LeaseTemplateDTO> findOne(Long id);

    void delete(Long id);

    Page<LeaseTemplateDTO> search(String query, Pageable pageable);
}
