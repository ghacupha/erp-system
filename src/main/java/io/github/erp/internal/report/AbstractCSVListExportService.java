package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class AbstractCSVListExportService<T> {


    private final ReportsProperties reportsProperties;
    private final FileStorageService fileStorageService;

    public AbstractCSVListExportService (ReportsProperties reportsProperties, FileStorageService fileStorageService) {
        this.reportsProperties = reportsProperties;
        this.fileStorageService = fileStorageService;
    }

    /**
     * @param reportList List items to be exported
     * @param fileName   filename to be used on the file system
     * @throws IOException can happen
     */
    public String executeReport(List<T> reportList, String fileName) throws IOException {

        ByteArrayOutputStream csvByteArray = CSVDynamicConverterService.convertToCSV(reportList);

        String reportPath = reportsProperties.getReportsDirectory().concat("/").concat(fileName).concat(".csv");

        try (FileOutputStream fileOutputStream = new FileOutputStream(reportPath)) {
            csvByteArray.writeTo(fileOutputStream);
        }

        return fileStorageService.calculateSha512CheckSum(fileName + ".csv");
    }
}
