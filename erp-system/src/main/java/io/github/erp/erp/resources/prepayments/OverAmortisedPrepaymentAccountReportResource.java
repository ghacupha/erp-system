package io.github.erp.erp.resources.prepayments;

import io.github.erp.erp.repository.prepayments.OverAmortisedPrepaymentAccountReportRepository;
import io.github.erp.erp.reports.prepayments.OverAmortisedPrepaymentAccountReportItem;
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
public class OverAmortisedPrepaymentAccountReportResource {

    private final Logger log = LoggerFactory.getLogger(OverAmortisedPrepaymentAccountReportResource.class);

    private final OverAmortisedPrepaymentAccountReportRepository repository;

    public OverAmortisedPrepaymentAccountReportResource(OverAmortisedPrepaymentAccountReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/over-amortised-prepayment-accounts")
    public ResponseEntity<List<OverAmortisedPrepaymentAccountReportItem>> getOverAmortisedPrepaymentAccounts(Pageable pageable) {
        log.debug("REST request for over-amortised prepayment accounts");
        Page<OverAmortisedPrepaymentAccountReportItem> page = repository.findOverAmortised(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
