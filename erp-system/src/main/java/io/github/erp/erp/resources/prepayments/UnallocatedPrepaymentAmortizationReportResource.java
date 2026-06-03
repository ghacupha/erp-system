package io.github.erp.erp.resources.prepayments;

import io.github.erp.erp.repository.prepayments.UnallocatedPrepaymentAmortizationReportRepository;
import io.github.erp.erp.reports.prepayments.UnallocatedPrepaymentAmortizationReportItem;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/prepayments")
public class UnallocatedPrepaymentAmortizationReportResource {

    private final Logger log = LoggerFactory.getLogger(UnallocatedPrepaymentAmortizationReportResource.class);

    private final UnallocatedPrepaymentAmortizationReportRepository repository;

    public UnallocatedPrepaymentAmortizationReportResource(UnallocatedPrepaymentAmortizationReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/unallocated-prepayment-amortizations")
    public ResponseEntity<List<UnallocatedPrepaymentAmortizationReportItem>> getUnallocatedPrepaymentAmortizations(Pageable pageable) {
        log.debug("REST request for unallocated prepayment amortizations");
        Page<UnallocatedPrepaymentAmortizationReportItem> page = repository.findUnallocated(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
