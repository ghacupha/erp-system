package io.github.erp.erp.reports;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.erp.domain.ReportMetadata;
import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.service.ReportMetadataQueryService;
import io.github.erp.service.criteria.ReportMetadataCriteria;
import io.github.erp.service.dto.ReportMetadataDTO;
import io.github.erp.service.mapper.ReportMetadataMapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReportMetadataResourceExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportMetadataResourceExtensionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportMetadataRepository reportMetadataRepository;

    @MockBean
    private ReportMetadataMapper reportMetadataMapper;

    @MockBean
    private ReportMetadataQueryService reportMetadataQueryService;

    @Test
    void searchActiveReportMetadataUsesRepositoryWhenTermProvided() throws Exception {
        ReportMetadata entity = buildEntity(1L);
        ReportMetadataDTO dto = buildDto(1L);

        Page<ReportMetadata> entityPage = new PageImpl<>(List.of(entity), PageRequest.of(0, 20), 1);
        when(reportMetadataRepository.searchActiveByTerm(eq("lease"), any(Pageable.class))).thenReturn(entityPage);
        when(reportMetadataMapper.toDto(entity)).thenReturn(dto);

        mockMvc
            .perform(get("/api/app/report-metadata/search").param("term", "lease").param("page", "0").param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(header().string("X-Total-Count", "1"))
            .andExpect(header().string(HttpHeaders.LINK, containsString("page=0")))
            .andExpect(jsonPath("$[0].id").value(dto.getId()))
            .andExpect(jsonPath("$[0].reportTitle").value(dto.getReportTitle()))
            .andExpect(jsonPath("$[0].module").value(dto.getModule()))
            .andExpect(jsonPath("$[0].pagePath").value(dto.getPagePath()))
            .andExpect(jsonPath("$[0].backendApi").value(dto.getBackendApi()))
            .andExpect(jsonPath("$[0].active").value(dto.getActive()));

        verify(reportMetadataRepository).searchActiveByTerm(eq("lease"), any(Pageable.class));
        verify(reportMetadataQueryService, never()).findByCriteria(any(ReportMetadataCriteria.class), any(Pageable.class));
    }

    @Test
    void searchActiveReportMetadataFallsBackToCriteriaWhenTermBlank() throws Exception {
        ReportMetadataDTO dto = buildDto(2L);
        Page<ReportMetadataDTO> dtoPage = new PageImpl<>(List.of(dto), PageRequest.of(0, 5), 1);
        when(reportMetadataQueryService.findByCriteria(any(ReportMetadataCriteria.class), any(Pageable.class))).thenReturn(dtoPage);

        mockMvc
            .perform(get("/api/app/report-metadata/search").param("term", " ").param("page", "0").param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(header().string("X-Total-Count", "1"))
            .andExpect(header().string(HttpHeaders.LINK, containsString("page=0")))
            .andExpect(jsonPath("$[0].id").value(dto.getId()))
            .andExpect(jsonPath("$[0].reportTitle").value(dto.getReportTitle()));

        verify(reportMetadataRepository, never()).searchActiveByTerm(anyString(), any(Pageable.class));
        verify(reportMetadataQueryService).findByCriteria(any(ReportMetadataCriteria.class), any(Pageable.class));
    }

    @Test
    void getByPagePathReturnsMetadataWhenPresent() throws Exception {
        ReportMetadata entity = buildEntity(5L);
        ReportMetadataDTO dto = buildDto(5L);
        when(reportMetadataRepository.findOneByPagePath("reports/lease")).thenReturn(Optional.of(entity));
        when(reportMetadataMapper.toDto(entity)).thenReturn(dto);

        mockMvc
            .perform(get("/api/app/report-metadata/by-path").param("path", "reports/lease"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(dto.getId()))
            .andExpect(jsonPath("$.reportTitle").value(dto.getReportTitle()))
            .andExpect(jsonPath("$.module").value(dto.getModule()));
    }

    @Test
    void getByPagePathReturnsNotFoundWhenAbsent() throws Exception {
        when(reportMetadataRepository.findOneByPagePath("unknown")).thenReturn(Optional.empty());

        mockMvc
            .perform(get("/api/app/report-metadata/by-path").param("path", "unknown"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getActiveReportMetadataReturnsActivePage() throws Exception {
        ReportMetadataDTO dto = buildDto(7L);
        Page<ReportMetadataDTO> dtoPage = new PageImpl<>(List.of(dto), PageRequest.of(1, 10), 12);
        when(reportMetadataQueryService.findByCriteria(any(ReportMetadataCriteria.class), any(Pageable.class))).thenReturn(dtoPage);

        mockMvc
            .perform(get("/api/app/report-metadata/active").param("page", "1").param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(header().string("X-Total-Count", "12"))
            .andExpect(header().string(HttpHeaders.LINK, containsString("page=1")))
            .andExpect(jsonPath("$[0].id").value(dto.getId()))
            .andExpect(jsonPath("$[0].reportTitle").value(dto.getReportTitle()))
            .andExpect(jsonPath("$[0].module").value(dto.getModule()));
    }

    private ReportMetadata buildEntity(Long id) {
        return new ReportMetadata()
            .id(id)
            .reportTitle("Lease Summary")
            .description("A lease report")
            .module("Leasing")
            .pagePath("reports/lease")
            .backendApi("/api/report/lease")
            .active(true);
    }

    private ReportMetadataDTO buildDto(Long id) {
        ReportMetadataDTO dto = new ReportMetadataDTO();
        dto.setId(id);
        dto.setReportTitle("Lease Summary");
        dto.setDescription("A lease report");
        dto.setModule("Leasing");
        dto.setPagePath("reports/lease");
        dto.setBackendApi("/api/report/lease");
        dto.setActive(true);
        dto.setFilters(Collections.emptyList());
        return dto;
    }
}
