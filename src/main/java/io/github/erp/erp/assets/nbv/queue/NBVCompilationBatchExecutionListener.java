package io.github.erp.erp.assets.nbv.queue;

import io.github.erp.erp.assets.nbv.BatchSequenceNBVCompilationService;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NBVCompilationBatchExecutionListener {

    private final Lock depreciationLock = new ReentrantLock();

    private final BatchSequenceNBVCompilationService batchSequenceNBVCompilationService;

    public NBVCompilationBatchExecutionListener(BatchSequenceNBVCompilationService batchSequenceNBVCompilationService) {
        this.batchSequenceNBVCompilationService = batchSequenceNBVCompilationService;
    }

    @KafkaListener(topics = "nbv_batch_topic", groupId = "erp-system", concurrency = "8")
    public void processJobMessages(NBVBatchMessage message, Acknowledgment acknowledgment) {

        try {

            depreciationLock.lock();


            // acknowledge the message to commit offset
            acknowledgment.acknowledge();

            long startingTime = System.currentTimeMillis();

            batchSequenceNBVCompilationService.compile(message);


        } finally {

            depreciationLock.unlock();
        }
    }
}
