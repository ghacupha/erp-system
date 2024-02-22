package io.github.erp.erp.assets.nbv;

import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;


public interface BatchSequenceNBVCompilationService {

    NBVBatchMessage compile(NBVBatchMessage nbvBatchMessage);
}
