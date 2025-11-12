package io.github.erp.erp.assets.nbv.queue;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import io.github.erp.erp.assets.nbv.BatchSequenceNBVCompilationService;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.internal.framework.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class NBVCompilationBatchExecutionListener {

    private final static Logger log = LoggerFactory.getLogger(NBVCompilationBatchExecutionListener.class);

    private final Lock depreciationLock = new ReentrantLock();

    private final BatchSequenceNBVCompilationService batchSequenceNBVCompilationService;
    private final Mapping<DepreciationBatchMessage, NBVBatchMessage> nbvBatchMapper;

    public NBVCompilationBatchExecutionListener(BatchSequenceNBVCompilationService batchSequenceNBVCompilationService, Mapping<DepreciationBatchMessage, NBVBatchMessage> nbvBatchMapper) {
        this.batchSequenceNBVCompilationService = batchSequenceNBVCompilationService;
        this.nbvBatchMapper = nbvBatchMapper;
    }

    /**
     *
     * @param message
     * @param acknowledgment
     */
    @KafkaListener(topics = "nbv_batch_topic", groupId = "erp-system-nbv", concurrency = "8")
    public void processJobMessages(DepreciationBatchMessage message, Acknowledgment acknowledgment) {

       /*
        * I know you are wondering but I too have no idea why the queue is constantly presenting a
        * depreciation-batch-message instance when the producer is actually sending an NBVBatchMessage
        * am just effing clueless. Even running the debugger at TRACE log levels I still haven't figured this
        * issue out; may be I should stop using kafka...
        * May be I should use my own factories instead of spring-kafka.
        * This listener is especially suspect. May be I should implement a listener manually. It will be painful and
        * full of errors, but that's how you effing grow, right? Rather than putting your head in the sand
        * and hope the problem will go away? Spring-kafka makes things easy with the annotation magic
        * but let's be honest, this thing of people doing things behind your back, behind the scenes and present
        * that everything is okay, gets old like really really fast. Even if you are not smart enough
        * you eventually figure something is going on and it will piss you off real bad. This is about
        * spring and this is not about spring. How about an interface that openly at least shows how
        * the messages are configured? It's a message queue for pete's sake, how did they ever think that
        * they can simplify things by hiding that interface? Just present the whole goddamn API and those
        * developers who cannot manage will know from day 1 to not ever touch kafka even if their lives
        * depended on it. Life can be Darwinian like that and it's just silly to pretend otherwise.
        */
        log.info("Message received by queue status update for processing....");
        try {

            depreciationLock.lock();


            // acknowledge the message to commit offset
            acknowledgment.acknowledge();

            long startingTime = System.currentTimeMillis();

            batchSequenceNBVCompilationService.compile(nbvBatchMapper.toValue2(message));


        } finally {

            depreciationLock.unlock();
        }
    }
}
