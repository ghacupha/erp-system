package io.github.erp.internal.files;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FilesSystemInit implements ApplicationListener<ApplicationReadyEvent> {

    private final FileStorageService storageService;

    public FilesSystemInit(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        storageService.deleteAll();
        storageService.init();
    }
}
