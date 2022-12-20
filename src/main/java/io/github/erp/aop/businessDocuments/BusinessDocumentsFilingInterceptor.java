
/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.8-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.erp.aop.businessDocuments;

import com.google.common.collect.HashBiMap;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.model.BusinessDocumentFSO;
import io.github.erp.internal.model.mapping.BusinessDocumentFSOMapping;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.*;

/**
 * This advise intercepts the BusinessDocuments#createBusinessDocument method processes and stores
 * the information and returns the required response.
 * The idea is while storing the information just like the advised resource, the method also stores the
 * file on the file-system folders asynchronously
 */
@Aspect
public class BusinessDocumentsFilingInterceptor {

    private static final String ENTITY_NAME = "businessDocumentFiling";

    private final BusinessDocumentService businessDocumentService;

    private final BusinessDocumentFSOMapping businessDocumentFSOMapping;

    private final FileStorageService fileStorageService;

    public BusinessDocumentsFilingInterceptor(BusinessDocumentService businessDocumentService, BusinessDocumentFSOMapping businessDocumentFSOMapping, FileStorageService fileStorageService) {
        this.businessDocumentService = businessDocumentService;
        this.businessDocumentFSOMapping = businessDocumentFSOMapping;
        this.fileStorageService = fileStorageService;
    }

    @Around(value = "filingResponsePointcut()")
    public ResponseEntity<BusinessDocumentFSO> documentFilingResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        String message = "";
        MultipartFile file;
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        BusinessDocumentFSO bDoc = (BusinessDocumentFSO) joinPoint.getArgs()[0];
        BusinessDocumentDTO result = businessDocumentService.save(businessDocumentFSOMapping.toValue2(bDoc));
        try {

            log.debug("REST request intercepted to save BusinessDocument serial: {}", bDoc.getDocumentSerial());

            if (bDoc.getId() != null) {
                throw new BadRequestAlertException("A new businessDocument cannot already have an ID", ENTITY_NAME, "idexists");
            }

            file = compileMultipart(bDoc);

            fileStorageService.save(file);

            // message = "Uploaded the file successfully: " + file.getOriginalFilename();

            log.info("{} uploaded successfully", file.getOriginalFilename());


            if (log.isDebugEnabled()) {
                log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);
            }

            // ResponseEntity<BusinessDocumentFSO> result = (ResponseEntity<BusinessDocumentFSO>)joinPoint.proceed();

            log.debug("Business Doc id: {} intercept complete with uploads to both DB & FS", result.getId());

            return ResponseEntity.status(HttpStatus.OK).body(businessDocumentFSOMapping.toValue1(result));
            // return businessDocumentFSOMapping.toValue1(result);

            // return result;

        } catch (Exception e) {
            log.error("Exception encountered: {} in {}() caused by {}", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName(), e.getMessage());

            // assert file != null;
            // message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(businessDocumentFSOMapping.toValue1(result));
        }
    }

    private MultipartFile compileMultipart(BusinessDocumentFSO bDoc) {
        return  new DecodedMultipartFile(bDoc);
    }

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Pointcut for report-requisition file attachment
     */
    @Pointcut("execution(* io.github.erp.erp.resources.BusinessDocumentResource.createBusinessDocument(..))")
    public void filingResponsePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    private static class DecodedMultipartFile implements MultipartFile {

        private static final Map<String, String> fileExtensionMap;

        static {
            fileExtensionMap = new HashMap<>();
            // MS Office
            fileExtensionMap.put("doc", "application/msword");
            fileExtensionMap.put("dot", "application/msword");
            fileExtensionMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            fileExtensionMap.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
            fileExtensionMap.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
            fileExtensionMap.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
            fileExtensionMap.put("xls", "application/vnd.ms-excel");
            fileExtensionMap.put("xlt", "application/vnd.ms-excel");
            fileExtensionMap.put("xla", "application/vnd.ms-excel");
            fileExtensionMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            fileExtensionMap.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
            fileExtensionMap.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
            fileExtensionMap.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
            fileExtensionMap.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
            fileExtensionMap.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
            fileExtensionMap.put("ppt", "application/vnd.ms-powerpoint");
            fileExtensionMap.put("pot", "application/vnd.ms-powerpoint");
            fileExtensionMap.put("pps", "application/vnd.ms-powerpoint");
            fileExtensionMap.put("ppa", "application/vnd.ms-powerpoint");
            fileExtensionMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            fileExtensionMap.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
            fileExtensionMap.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
            fileExtensionMap.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
            fileExtensionMap.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
            fileExtensionMap.put("potm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
            fileExtensionMap.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
            // Open Office
            fileExtensionMap.put("odt", "application/vnd.oasis.opendocument.text");
            fileExtensionMap.put("ott", "application/vnd.oasis.opendocument.text-template");
            fileExtensionMap.put("oth", "application/vnd.oasis.opendocument.text-web");
            fileExtensionMap.put("odm", "application/vnd.oasis.opendocument.text-master");
            fileExtensionMap.put("odg", "application/vnd.oasis.opendocument.graphics");
            fileExtensionMap.put("otg", "application/vnd.oasis.opendocument.graphics-template");
            fileExtensionMap.put("odp", "application/vnd.oasis.opendocument.presentation");
            fileExtensionMap.put("otp", "application/vnd.oasis.opendocument.presentation-template");
            fileExtensionMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
            fileExtensionMap.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
            fileExtensionMap.put("odc", "application/vnd.oasis.opendocument.chart");
            fileExtensionMap.put("odf", "application/vnd.oasis.opendocument.formula");
            fileExtensionMap.put("odb", "application/vnd.oasis.opendocument.database");
            fileExtensionMap.put("odi", "application/vnd.oasis.opendocument.image");
            fileExtensionMap.put("oxt", "application/vnd.openofficeorg.extension");
            // Other
            fileExtensionMap.put("txt", "text/plain");
            fileExtensionMap.put("rtf", "application/rtf");
            fileExtensionMap.put("pdf", "application/pdf");
        }

        private final byte[] fileContent;
        private final String fileContentType;
        private final String fileName;
        private final String ext;

        public DecodedMultipartFile(BusinessDocumentFSO businessDocument) {
            fileContent = businessDocument.getDocumentFile();
            fileName = businessDocument.getDocumentSerial().toString();
            fileContentType = businessDocument.getDocumentFileContentType();
            ext = getKeys(fileExtensionMap, businessDocument.getDocumentFileContentType()).stream().findFirst().orElse("erp");
        }

        public String getExt() {
            return "."+ext;
        }

        @Override
        public @NotNull String getName() {
            return fileName + getExt();
        }

        @Override
        public String getOriginalFilename() {
            return fileName + getExt();
        }

        @Override
        public String getContentType() {
            return fileContentType;
        }

        @Override
        public boolean isEmpty() {
            return fileContent == null || fileContent.length == 0;
        }

        @Override
        public long getSize() {
            return fileContent.length;
        }

        @Override
        public byte @NotNull [] getBytes() throws IOException {
            return fileContent;
        }

        @Override
        public @NotNull InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(fileContent);
        }

        @Override
        public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
            new FileOutputStream(dest).write(fileContent);
        }

        private <K, V> Set<K> getKeys(Map<K, V> map, V value) {
            Set<K> keys = new HashSet<>();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (entry.getValue().equals(value)) {
                    keys.add(entry.getKey());
                }
            }
            return keys;
        }
    }
}
