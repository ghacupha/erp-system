package io.github.erp.erp.resources.prepayments;

import io.github.erp.erp.repository.prepayments.UnallocatedPrepaymentAccountReportRepository;
import io.github.erp.erp.reports.prepayments.UnallocatedPrepaymentAccountReportItem;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/prepayments")
public class UnallocatedPrepaymentAccountReportResource {

    private static final BigDecimal DEFAULT_MINIMUM_OUTSTANDING_AMOUNT = BigDecimal.ONE;

    private final Logger log = LoggerFactory.getLogger(UnallocatedPrepaymentAccountReportResource.class);

    private final UnallocatedPrepaymentAccountReportRepository repository;

    public UnallocatedPrepaymentAccountReportResource(UnallocatedPrepaymentAccountReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/unallocated-prepayment-accounts")
    public ResponseEntity<List<UnallocatedPrepaymentAccountReportItem>> getUnallocatedPrepaymentAccounts(
        @RequestParam(defaultValue = "1.00") BigDecimal minimumOutstandingAmount,
        Pageable pageable
    ) {
        BigDecimal threshold = minimumOutstandingAmount == null ? DEFAULT_MINIMUM_OUTSTANDING_AMOUNT : minimumOutstandingAmount.abs();
        log.debug("REST request for unallocated prepayment accounts with threshold {}", threshold);
        Page<UnallocatedPrepaymentAccountReportItem> page = repository.findUnallocated(threshold, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
