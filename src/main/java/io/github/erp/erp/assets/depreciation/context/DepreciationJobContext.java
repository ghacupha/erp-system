package io.github.erp.erp.assets.depreciation.context;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.math.BigDecimal;
import java.util.UUID;

public class DepreciationJobContext {
    private static final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    private static final DepreciationJobContext INSTANCE = new DepreciationJobContext();

    private static final String CONTEXT_ID = "depreciationProcessContext";

    private DepreciationJobContext() {
        // Private constructor to prevent instantiation
    }

    public static DepreciationJobContext getInstance() {
        return INSTANCE;
    }

    public UUID createContext(int numberOfProcessedItems) {
        UUID uuid = UUID.randomUUID();
        hazelcastInstance.getMap(CONTEXT_ID).put(uuid, numberOfProcessedItems);
        return uuid;
    }

    public int getNumberOfProcessedItems(UUID uuid) {
        return (int) hazelcastInstance.getMap(CONTEXT_ID).getOrDefault(uuid, 0);
    }

    public BigDecimal getAmount(UUID uuid) {
        return (BigDecimal) hazelcastInstance.getMap(CONTEXT_ID).getOrDefault(uuid, BigDecimal.ZERO);
    }

    public void updateNumberOfProcessedItems(UUID uuid, int incrementBy) {
        int currentCount = getNumberOfProcessedItems(uuid);
        hazelcastInstance.getMap(CONTEXT_ID).put(uuid, currentCount + incrementBy);
    }

    public void updateAmount(UUID uuid, BigDecimal incrementBy) {
        BigDecimal currentAmount = getAmount(uuid);
        hazelcastInstance.getMap(CONTEXT_ID).put(uuid, currentAmount.add(incrementBy));
    }

    public void removeContext(UUID uuid) {
        hazelcastInstance.getMap(CONTEXT_ID).remove(uuid);
    }

}

