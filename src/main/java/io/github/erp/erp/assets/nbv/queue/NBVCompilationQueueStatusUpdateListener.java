package io.github.erp.erp.assets.nbv.queue;

import io.github.erp.domain.enumeration.CompilationBatchStatusTypes;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.NbvCompilationBatchService;
import io.github.erp.service.dto.NbvCompilationBatchDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NBVCompilationQueueStatusUpdateListener {

    private final Lock depreciationLock = new ReentrantLock();

    private final NbvCompilationBatchService nbvCompilationBatchService;

    public NBVCompilationQueueStatusUpdateListener(NbvCompilationBatchService nbvCompilationBatchService) {
        this.nbvCompilationBatchService = nbvCompilationBatchService;
    }

    @KafkaListener(topics = "nbv_batch_topic", groupId = "erp-system", concurrency = "8")
    public void processJobMessages(NBVBatchMessage message, Acknowledgment acknowledgment) {

        try {

            depreciationLock.lock();

            nbvCompilationBatchService.save(updateCompilationBatch(message));


            // acknowledge the message to commit offset
            acknowledgment.acknowledge();

        } finally {

            depreciationLock.unlock();
        }
    }

    private NbvCompilationBatchDTO updateCompilationBatch(NBVBatchMessage message) {
        final NbvCompilationBatchDTO[] dto = {null};

        nbvCompilationBatchService.findOne(message.getBatchId()).ifPresent(batch ->{

            dto[0] = batch;
        });

        dto[0].setCompilationBatchStatus(CompilationBatchStatusTypes.RUNNING);


        return dto[0];
    }
}
