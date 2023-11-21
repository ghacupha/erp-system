package io.github.erp.internal.service;

import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import org.springframework.scheduling.annotation.Async;

public interface PrepaymentCompilationService {
    @Async
    void compile(PrepaymentCompilationRequestDTO compilationRequest, PrepaymentCompilationCompleteSequence completeSequence);
}
