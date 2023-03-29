
/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.1-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
/*-
 *  Server - Leases and assets management platform
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
/**
 * Package contains services called in a typical batch process workflow
 *
 * For instance if we have a service for a SchemeTable entity it might look like so :
 *
 * {@code
 *
        package io.github.erp.internal.service;

        import io.github.deposits.repository.SchemeTableRepository;
        import io.github.deposits.repository.search.SchemeTableSearchRepository;
        import io.github.deposits.service.dto.SchemeTableDTO;
        import io.github.deposits.service.mapper.SchemeTableMapper;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;

        @Transactional
        @Service
        public class SchemeTableBatchService implements BatchService<SchemeTableDTO> {

            private final SchemeTableMapper schemeTableMapper;
            private final SchemeTableRepository schemeTableRepository;
            private final SchemeTableSearchRepository schemeTableSearchRepository;

            public SchemeTableBatchService(final SchemeTableMapper schemeTableMapper, final SchemeTableRepository schemeTableRepository, final SchemeTableSearchRepository schemeTableSearchRepository) {
                this.schemeTableMapper = schemeTableMapper;
                this.schemeTableRepository = schemeTableRepository;
                this.schemeTableSearchRepository = schemeTableSearchRepository;
            }

            @Override
            public List<SchemeTableDTO> save(final List<SchemeTableDTO> entities) {
                return schemeTableMapper.toDto(schemeTableRepository.saveAll(schemeTableMapper.toEntity(entities)));
            }

            @Override
            public void index(final List<SchemeTableDTO> entities) {

                this.schemeTableSearchRepository.saveAll(schemeTableMapper.toEntity(entities));
            }
        }
 * }
 *
 * Of course you have to have the typical jhipster-like repositories and service setup
 */
package io.github.erp.internal.service;
