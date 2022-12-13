package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.7-SNAPSHOT
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
import io.github.erp.internal.report.templates.TemplatePresentation;
import io.github.erp.service.ReportTemplateService;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class XLSXAssemblyService implements ReportAssemblyService<XlsxReportRequisitionDTO> {

    private final TemplatePresentation templatePresentation;
    private final XLSXReportsService reportsService;
    private final ReportTemplateService reportTemplateService;

    public XLSXAssemblyService(TemplatePresentation templatePresentation, XLSXReportsService reportsService, ReportTemplateService reportTemplateService) {
        this.templatePresentation = templatePresentation;
        this.reportsService = reportsService;
        this.reportTemplateService = reportTemplateService;
    }

    @Override
    public String createReport(XlsxReportRequisitionDTO dto, String fileExtension) {
        Map<String, Object> parameters = new HashMap<>();
        final String[] fileName = {""};

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
         * right there IN THE ARGUMENTS!!!!!! Anyway, I was just venting, so I will delete the comment, and no one
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

        return reportsService.generateReport(
            fileName[0],
            dto.getReportId().toString().concat(fileExtension),
            dto.getUserPassword(),
            dto.getUserPassword(),
            parameters
        );
    }
}
