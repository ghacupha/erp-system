package io.github.erp.erp.depreciation;

import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepreciationJobCompleteCallbackImpl implements DepreciationJobCompleteCallback {

    private final static Logger log = LoggerFactory.getLogger(DepreciationJobCompleteCallbackImpl.class);

    private final DepreciationJobService depreciationJobService;

    public DepreciationJobCompleteCallbackImpl(DepreciationJobService depreciationJobService) {
        this.depreciationJobService = depreciationJobService;
    }

    /**
     * Called when process is complete
     *
     * @param message
     */
    @Override
    public void onComplete(DepreciationBatchMessage message) {

        depreciationJobService.findOne(Long.valueOf(message.getJobId())).ifPresent(depreciationJob -> {
            if (depreciationJob.getDepreciationJobStatus() != DepreciationJobStatusType.COMPLETE) {
                depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
                depreciationJobService.save(depreciationJob);

                log.info("The depreciation job id: {} {}, has been completed", depreciationJob.getId(), depreciationJob.getDescription());
            }
        });
    }
}
