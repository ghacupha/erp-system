package io.github.erp.internal.service.leases.trxAccounts;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright  2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import java.math.BigInteger;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseTransactionEntryIdGenerator implements TransactionEntryIdGenerator {

    private final EntityManager entityManager;

    public DatabaseTransactionEntryIdGenerator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public long nextEntryId() {
        Object result = entityManager.createNativeQuery("select nextval('transaction_entry_id_sequence')").getSingleResult();
        if (result instanceof BigInteger) {
            return ((BigInteger) result).longValue();
        }
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        throw new IllegalStateException("Unexpected sequence value type: " + result.getClass());
    }
}
