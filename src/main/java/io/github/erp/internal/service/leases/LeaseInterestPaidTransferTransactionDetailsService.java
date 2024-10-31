package io.github.erp.internal.service.leases;

import java.util.UUID;

public interface LeaseInterestPaidTransferTransactionDetailsService {
    void createTransactionDetails(UUID requisitionId, Long postedById);
}
