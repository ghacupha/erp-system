package io.github.erp.internal.service.leases;

import java.util.UUID;

public interface LeaseRepaymentTransactionDetailsService {
    void createTransactionDetails(UUID requisitionId, Long postedById);
}
