package io.github.erp.internal.service.rou;

import io.github.erp.service.dto.RouModelMetadataDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class compile the depreciation entries for each ROU model metadata
 * provided in the following way:
 * 1. We calculate the initial lease-period using the commencementDate of the
 *    model-metadata provided
 * 2. Fetch a list of x lease-periods starting with the initial period computed
 *    above and going through sequential increments until the leaseTerm specified
 *    in the model metadata is attained
 * 3. For each period compute the depreciation amount using the straight line basis
 *    and save a corresponding rou-depreciation-entry
 *
 * After these steps the compilation for a single rouModelMetadata is complete
 */
@Transactional
@Service
public class ROUDepreciationEntryCompilationServiceImpl {

    private final InternalLeasePeriodService internalLeasePeriodService;

    public ROUDepreciationEntryCompilationServiceImpl(InternalLeasePeriodService internalLeasePeriodService) {
        this.internalLeasePeriodService = internalLeasePeriodService;
    }

    public void compileDepreciation(RouModelMetadataDTO modelMetadataDTO) {

        // get sequence of leasePeriods for the term of the rou lease
        // Optional<List<LeasePeriod>> periods  = Optional.empty();
        // internalLeasePeriodService.findLeaseTermPeriods(modelMetadataDTO.getId()).ifPresent(period - > {
        //   TODO create depreciation-entry for each period
        //    TODO save each period using buffer
        // })
    }
}
