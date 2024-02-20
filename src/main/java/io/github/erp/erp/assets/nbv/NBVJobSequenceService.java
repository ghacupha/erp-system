package io.github.erp.erp.assets.nbv;

public interface NBVJobSequenceService<Job> {

    void  triggerJobStart(Job job);
}
