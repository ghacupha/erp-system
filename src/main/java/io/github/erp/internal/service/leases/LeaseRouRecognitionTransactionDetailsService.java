package io.github.erp.internal.service.leases;

import java.util.UUID;

public interface LeaseRouRecognitionTransactionDetailsService {
    void createTransactionDetails(UUID requisitionId, Long postedById);
}
