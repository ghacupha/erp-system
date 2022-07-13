package io.github.erp.internal.model.mapping;

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
