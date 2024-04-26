package io.github.erp.internal.report.assemblies;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.internal.report.templates.TemplatePresentation;
import io.github.erp.service.ReportContentTypeService;
import io.github.erp.service.ReportTemplateService;
import io.github.erp.service.SystemContentTypeService;
import io.github.erp.service.dto.ReportRequisitionDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * This service is usable but with warnings: after the creation was complete, it was seen that the
 * reports generated were somehow corrupted and that means that we'll need to create a couple of very
 * serious checksum algorithms and routines, and until then this API is to be considered an untested
 * prototype
 */
@Service
public class ReportRequisitionAssemblyService implements ReportAssemblyService<ReportRequisitionDTO>{

    private final ReportTemplateService reportTemplateService;
    private final TemplatePresentation templatePresentation;
    private final GenericReportService genericReportService;
    private final SystemContentTypeService systemContentTypeService;
    private final ReportContentTypeService reportContentTypeService;

    public ReportRequisitionAssemblyService(ReportTemplateService reportTemplateService, TemplatePresentation templatePresentation, GenericReportService genericReportService, SystemContentTypeService systemContentTypeService, ReportContentTypeService reportContentTypeService) {
        this.reportTemplateService = reportTemplateService;
        this.templatePresentation = templatePresentation;
        this.genericReportService = genericReportService;
        this.systemContentTypeService = systemContentTypeService;
        this.reportContentTypeService = reportContentTypeService;
    }

    @Override
    public String createReport(ReportRequisitionDTO dto, String fileExtension) {
        Map<String, Object> parameters = new HashMap<>();
        final String[] fileName = {""};
        final String[] categoryTypeName = {""};

        /*
         * So this is very interesting because one would think that simply because the object
         * contains a report template object, "why don't I just get a report template byte stream from there?"
         * Right? But you would be mistaken. Apparently that would just lead you to null pointer exceptions
         * that you would be looking for, for the rest of your life. Hibernate needs to have a way
         * of explicitly showing that to poor devs like me, though am certain they have said that
         * somewhere in the specification as they explain, "the complex nature of many to one relationships".
         * So anyway, that's why against better advise I've had to inject the service instance
         * for report-template, (WHICH I DID IN THE CALLER AS WELL); and you can see why this excites our puzzlement especially if there
         * was a time you religiously signed up and followed Bob's DRY design pattern. So am not picking
         * up the pitch forks yet, but am starting to take stock of where I keep the sharp tools. Of course
         * if am to take this to stackoverflow the folks there would have a field day on how people should get
         * into the habit of actually reading their documentation and so on and so forth. But this is Java
         * for heaven's sake and people who use Java are people who like things expressed in explicit terms: like
         * you don't just say that a particular method could burn your hands in the comments, we want to see
         * it in the code. Is that too much to ask? Huh? Am I rambling too much and would I know if I did? I guess not
         * *sigh* But why in the SEVEN HELLS am I injecting the reporting-template-service here if I'd already
         * done it the caller and fetched a fresh instance of the report-template from the repository? As in it's
         * right there IN THE ARGUMENTS!!!!!! Anyway, I was just venting, so I will delete this comment, and no one
         * will have to see it...
         */
        reportTemplateService.findOne(dto.getReportTemplate().getId()).ifPresent(template -> {

            fileName[0] = templatePresentation.presentTemplate(template);

            parameters.put("title", dto.getReportName());
            parameters.put("description", dto.getReportTemplate().getDescription());

            dto.getParameters().forEach(p -> {
                parameters.put(p.getUniversalKey(), p.getMappedValue());
            });
        });

        reportContentTypeService.findOne(dto.getReportContentType().getId()).flatMap(cType -> systemContentTypeService.findOne(cType.getSystemContentType().getId())).ifPresent(type -> {
            categoryTypeName[0] = type.getContentTypeName();
        });

        return genericReportService.generateReport(
            categoryTypeName[0],
            fileName[0],
            dto.getReportId().toString().concat(fileExtension),
            dto.getReportPassword(),
            dto.getReportPassword(),
            parameters
        );
    }
}
