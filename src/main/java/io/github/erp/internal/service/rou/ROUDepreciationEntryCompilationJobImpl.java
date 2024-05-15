package io.github.erp.internal.service.rou;

import io.github.erp.service.dto.RouDepreciationRequestDTO;
import liquibase.pro.packaged.A;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static io.github.erp.internal.service.rou.batch.ROUDepreciationEntryBatchConfigs.PERSISTENCE_JOB_NAME;

@Service
public class ROUDepreciationEntryCompilationJobImpl implements ROUDepreciationEntryCompilationJob {

    @Autowired
    @Qualifier(PERSISTENCE_JOB_NAME)
    private Job rouDepreciationEntryPersistenceJob;

    /**
     * Initiate a rou depreciation job
     *
     * @param reportDTO
     */
    @Override
    public void compileROUDepreciationEntries(RouDepreciationRequestDTO reportDTO) {

        JobParameters jobParameters = new JobParameters();
        // jobParameters


        JobExecution jobExecution =
            new JobExecution(rouDepreciationEntryPersistenceJob, );




        rouDepreciationEntryPersistenceJob.execute();
    }
}
