package io.github.erp.erp.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class SearchIndexingService {

    public <T, ID> void saveAfterCommit(ElasticsearchRepository<T, ID> repository, T document) {
        runAfterCommit(() -> repository.save(document));
    }

    public <T, ID> void deleteAfterCommit(ElasticsearchRepository<T, ID> repository, ID id) {
        runAfterCommit(() -> repository.deleteById(id));
    }

    private void runAfterCommit(Runnable action) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
            return;
        }

        action.run();
    }
}
