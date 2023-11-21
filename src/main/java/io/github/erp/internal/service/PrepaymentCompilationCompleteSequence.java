package io.github.erp.internal.service;

import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;

/**
 * Callback for completion of compilation
 */
public interface PrepaymentCompilationCompleteSequence {
    void compilationComplete(PrepaymentCompilationRequestDTO compilationRequest);
}
