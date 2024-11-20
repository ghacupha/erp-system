package io.github.erp.internal.service.ledgers;

import java.util.List;

public interface TransactionAccountAdjacent {

    /**
     * Finds all transactionAccount instances in a relationship with the
     * current root entity
     *
     * @return List of related ids
     */
    List<Long> findAdjacentIds();
}
