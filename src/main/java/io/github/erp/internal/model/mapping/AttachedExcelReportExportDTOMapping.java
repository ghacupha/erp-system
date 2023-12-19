package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.AttachedExcelReportExportDTO;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class AttachedExcelReportExportDTOMapping implements Mapping<ExcelReportExportDTO, AttachedExcelReportExportDTO> {

    @Override
    public ExcelReportExportDTO toValue1(AttachedExcelReportExportDTO arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ExcelReportExportDTO excelReportExportDTO = new ExcelReportExportDTO();

        excelReportExportDTO.setId( arg0.getId() );
        excelReportExportDTO.setReportName( arg0.getReportName() );
        excelReportExportDTO.setReportPassword( arg0.getReportPassword() );
        byte[] reportNotes = arg0.getReportNotes();
        if ( reportNotes != null ) {
            excelReportExportDTO.setReportNotes( Arrays.copyOf( reportNotes, reportNotes.length ) );
        }
        excelReportExportDTO.setReportNotesContentType( arg0.getReportNotesContentType() );
        byte[] reportFile = arg0.getReportFile();
        if ( reportFile != null ) {
            excelReportExportDTO.setReportFile( Arrays.copyOf( reportFile, reportFile.length ) );
        }
        excelReportExportDTO.setReportFileContentType( arg0.getReportFileContentType() );
        excelReportExportDTO.setReportTimeStamp( arg0.getReportTimeStamp() );
        excelReportExportDTO.setReportId( arg0.getReportId() );
        Set<PlaceholderDTO> set = arg0.getPlaceholders();
        if ( set != null ) {
            excelReportExportDTO.setPlaceholders( new HashSet<PlaceholderDTO>( set ) );
        }
        Set<UniversallyUniqueMappingDTO> set1 = arg0.getParameters();
        if ( set1 != null ) {
            excelReportExportDTO.setParameters( new HashSet<UniversallyUniqueMappingDTO>( set1 ) );
        }
        excelReportExportDTO.setReportStatus( arg0.getReportStatus() );
        excelReportExportDTO.setSecurityClearance( arg0.getSecurityClearance() );
        excelReportExportDTO.setReportCreator( arg0.getReportCreator() );
        excelReportExportDTO.setOrganization( arg0.getOrganization() );
        excelReportExportDTO.setDepartment( arg0.getDepartment() );
        excelReportExportDTO.setSystemModule( arg0.getSystemModule() );
        excelReportExportDTO.setReportDesign( arg0.getReportDesign() );
        excelReportExportDTO.setFileCheckSum(arg0.getFileChecksum());
        excelReportExportDTO.setFileCheckSumAlgorithm( arg0.getFileCheckSumAlgorithm() );

        return excelReportExportDTO;
    }

    @Override
    public AttachedExcelReportExportDTO toValue2(ExcelReportExportDTO arg0) {
        if ( arg0 == null ) {
            return null;
        }

        AttachedExcelReportExportDTO.AttachedExcelReportExportDTOBuilder attachedExcelReportExportDTO = AttachedExcelReportExportDTO.builder();

        attachedExcelReportExportDTO.id( arg0.getId() );
        attachedExcelReportExportDTO.reportName( arg0.getReportName() );
        attachedExcelReportExportDTO.reportPassword( arg0.getReportPassword() );
        byte[] reportNotes = arg0.getReportNotes();
        if ( reportNotes != null ) {
            attachedExcelReportExportDTO.reportNotes( Arrays.copyOf( reportNotes, reportNotes.length ) );
        }
        attachedExcelReportExportDTO.reportNotesContentType( arg0.getReportNotesContentType() );
        attachedExcelReportExportDTO.fileCheckSum( arg0.getFileCheckSum() );
        byte[] reportFile = arg0.getReportFile();
        if ( reportFile != null ) {
            attachedExcelReportExportDTO.reportFile( Arrays.copyOf( reportFile, reportFile.length ) );
        }
        attachedExcelReportExportDTO.reportFileContentType( arg0.getReportFileContentType() );
        attachedExcelReportExportDTO.reportTimeStamp( arg0.getReportTimeStamp() );
        attachedExcelReportExportDTO.reportId( arg0.getReportId() );
        Set<PlaceholderDTO> set = arg0.getPlaceholders();
        if ( set != null ) {
            attachedExcelReportExportDTO.placeholders( new HashSet<PlaceholderDTO>( set ) );
        }
        Set<UniversallyUniqueMappingDTO> set1 = arg0.getParameters();
        if ( set1 != null ) {
            attachedExcelReportExportDTO.parameters( new HashSet<UniversallyUniqueMappingDTO>( set1 ) );
        }
        attachedExcelReportExportDTO.reportStatus( arg0.getReportStatus() );
        attachedExcelReportExportDTO.securityClearance( arg0.getSecurityClearance() );
        attachedExcelReportExportDTO.reportCreator( arg0.getReportCreator() );
        attachedExcelReportExportDTO.organization( arg0.getOrganization() );
        attachedExcelReportExportDTO.department( arg0.getDepartment() );
        attachedExcelReportExportDTO.systemModule( arg0.getSystemModule() );
        attachedExcelReportExportDTO.reportDesign( arg0.getReportDesign() );
        attachedExcelReportExportDTO.fileCheckSumAlgorithm( arg0.getFileCheckSumAlgorithm() );

        return attachedExcelReportExportDTO.build();
    }

}
