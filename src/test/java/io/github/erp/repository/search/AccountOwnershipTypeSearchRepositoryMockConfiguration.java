package io.github.erp.repository.search;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AccountOwnershipTypeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AccountOwnershipTypeSearchRepositoryMockConfiguration {

    @MockBean
    private AccountOwnershipTypeSearchRepository mockAccountOwnershipTypeSearchRepository;
}
