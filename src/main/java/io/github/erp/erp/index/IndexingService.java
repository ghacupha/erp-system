package io.github.erp.erp.index;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile("!no-index")
public @interface IndexingService {
}
