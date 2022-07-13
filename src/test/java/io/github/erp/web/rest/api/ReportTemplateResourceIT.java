package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ReportTemplate;
import io.github.erp.repository.ReportTemplateRepository;
import io.github.erp.repository.search.ReportTemplateSearchRepository;
import io.github.erp.service.ReportTemplateService;
import io.github.erp.service.dto.ReportTemplateDTO;
import io.github.erp.service.mapper.ReportTemplateMapper;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReportTemplateResourceDev} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class ReportTemplateResourceIT {

    private static final String DEFAULT_CATALOGUE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CATALOGUE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_NOTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NOTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NOTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NOTES_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_COMPILE_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COMPILE_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COMPILE_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/dev/report-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/report-templates";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportTemplateRepository reportTemplateRepository;

    @Mock
    private ReportTemplateRepository reportTemplateRepositoryMock;

    @Autowired
    private ReportTemplateMapper reportTemplateMapper;

    @Mock
    private ReportTemplateService reportTemplateServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReportTemplateSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportTemplateSearchRepository mockReportTemplateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportTemplateMockMvc;

    private ReportTemplate reportTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportTemplate createEntity(EntityManager em) {
        ReportTemplate reportTemplate = new ReportTemplate()
            .catalogueNumber(DEFAULT_CATALOGUE_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .notesContentType(DEFAULT_NOTES_CONTENT_TYPE)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE)
            .compileReportFile(DEFAULT_COMPILE_REPORT_FILE)
            .compileReportFileContentType(DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE);
        return reportTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportTemplate createUpdatedEntity(EntityManager em) {
        ReportTemplate reportTemplate = new ReportTemplate()
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .compileReportFile(UPDATED_COMPILE_REPORT_FILE)
            .compileReportFileContentType(UPDATED_COMPILE_REPORT_FILE_CONTENT_TYPE);
        return reportTemplate;
    }

    @BeforeEach
    public void initTest() {
        reportTemplate = createEntity(em);
    }

    @Test
    @Transactional
    void createReportTemplate() throws Exception {
        int databaseSizeBeforeCreate = reportTemplateRepository.findAll().size();
        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);
        restReportTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        ReportTemplate testReportTemplate = reportTemplateList.get(reportTemplateList.size() - 1);
        assertThat(testReportTemplate.getCatalogueNumber()).isEqualTo(DEFAULT_CATALOGUE_NUMBER);
        assertThat(testReportTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReportTemplate.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testReportTemplate.getNotesContentType()).isEqualTo(DEFAULT_NOTES_CONTENT_TYPE);
        assertThat(testReportTemplate.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testReportTemplate.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportTemplate.getCompileReportFile()).isEqualTo(DEFAULT_COMPILE_REPORT_FILE);
        assertThat(testReportTemplate.getCompileReportFileContentType()).isEqualTo(DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(1)).save(testReportTemplate);
    }

    @Test
    @Transactional
    void createReportTemplateWithExistingId() throws Exception {
        // Create the ReportTemplate with an existing ID
        reportTemplate.setId(1L);
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        int databaseSizeBeforeCreate = reportTemplateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(0)).save(reportTemplate);
    }

    @Test
    @Transactional
    void checkCatalogueNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportTemplateRepository.findAll().size();
        // set the field null
        reportTemplate.setCatalogueNumber(null);

        // Create the ReportTemplate, which fails.
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        restReportTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportTemplates() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].compileReportFileContentType").value(hasItem(DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].compileReportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMPILE_REPORT_FILE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportTemplatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportTemplateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportTemplateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportTemplatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportTemplateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportTemplateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReportTemplate() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get the reportTemplate
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, reportTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportTemplate.getId().intValue()))
            .andExpect(jsonPath("$.catalogueNumber").value(DEFAULT_CATALOGUE_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.notesContentType").value(DEFAULT_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.notes").value(Base64Utils.encodeToString(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)))
            .andExpect(jsonPath("$.compileReportFileContentType").value(DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.compileReportFile").value(Base64Utils.encodeToString(DEFAULT_COMPILE_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getReportTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        Long id = reportTemplate.getId();

        defaultReportTemplateShouldBeFound("id.equals=" + id);
        defaultReportTemplateShouldNotBeFound("id.notEquals=" + id);

        defaultReportTemplateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportTemplateShouldNotBeFound("id.greaterThan=" + id);

        defaultReportTemplateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportTemplateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCatalogueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where catalogueNumber equals to DEFAULT_CATALOGUE_NUMBER
        defaultReportTemplateShouldBeFound("catalogueNumber.equals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the reportTemplateList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultReportTemplateShouldNotBeFound("catalogueNumber.equals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCatalogueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where catalogueNumber not equals to DEFAULT_CATALOGUE_NUMBER
        defaultReportTemplateShouldNotBeFound("catalogueNumber.notEquals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the reportTemplateList where catalogueNumber not equals to UPDATED_CATALOGUE_NUMBER
        defaultReportTemplateShouldBeFound("catalogueNumber.notEquals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCatalogueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where catalogueNumber in DEFAULT_CATALOGUE_NUMBER or UPDATED_CATALOGUE_NUMBER
        defaultReportTemplateShouldBeFound("catalogueNumber.in=" + DEFAULT_CATALOGUE_NUMBER + "," + UPDATED_CATALOGUE_NUMBER);

        // Get all the reportTemplateList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultReportTemplateShouldNotBeFound("catalogueNumber.in=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCatalogueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where catalogueNumber is not null
        defaultReportTemplateShouldBeFound("catalogueNumber.specified=true");

        // Get all the reportTemplateList where catalogueNumber is null
        defaultReportTemplateShouldNotBeFound("catalogueNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCatalogueNumberContainsSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where catalogueNumber contains DEFAULT_CATALOGUE_NUMBER
        defaultReportTemplateShouldBeFound("catalogueNumber.contains=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the reportTemplateList where catalogueNumber contains UPDATED_CATALOGUE_NUMBER
        defaultReportTemplateShouldNotBeFound("catalogueNumber.contains=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCatalogueNumberNotContainsSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where catalogueNumber does not contain DEFAULT_CATALOGUE_NUMBER
        defaultReportTemplateShouldNotBeFound("catalogueNumber.doesNotContain=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the reportTemplateList where catalogueNumber does not contain UPDATED_CATALOGUE_NUMBER
        defaultReportTemplateShouldBeFound("catalogueNumber.doesNotContain=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        reportTemplate.addPlaceholder(placeholder);
        reportTemplateRepository.saveAndFlush(reportTemplate);
        Long placeholderId = placeholder.getId();

        // Get all the reportTemplateList where placeholder equals to placeholderId
        defaultReportTemplateShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the reportTemplateList where placeholder equals to (placeholderId + 1)
        defaultReportTemplateShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportTemplateShouldBeFound(String filter) throws Exception {
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].compileReportFileContentType").value(hasItem(DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].compileReportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMPILE_REPORT_FILE))));

        // Check, that the count call also returns 1
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportTemplateShouldNotBeFound(String filter) throws Exception {
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportTemplate() throws Exception {
        // Get the reportTemplate
        restReportTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportTemplate() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();

        // Update the reportTemplate
        ReportTemplate updatedReportTemplate = reportTemplateRepository.findById(reportTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedReportTemplate are not directly saved in db
        em.detach(updatedReportTemplate);
        updatedReportTemplate
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .compileReportFile(UPDATED_COMPILE_REPORT_FILE)
            .compileReportFileContentType(UPDATED_COMPILE_REPORT_FILE_CONTENT_TYPE);
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(updatedReportTemplate);

        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);
        ReportTemplate testReportTemplate = reportTemplateList.get(reportTemplateList.size() - 1);
        assertThat(testReportTemplate.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testReportTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportTemplate.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testReportTemplate.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testReportTemplate.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testReportTemplate.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportTemplate.getCompileReportFile()).isEqualTo(UPDATED_COMPILE_REPORT_FILE);
        assertThat(testReportTemplate.getCompileReportFileContentType()).isEqualTo(UPDATED_COMPILE_REPORT_FILE_CONTENT_TYPE);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository).save(testReportTemplate);
    }

    @Test
    @Transactional
    void putNonExistingReportTemplate() throws Exception {
        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();
        reportTemplate.setId(count.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(0)).save(reportTemplate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportTemplate() throws Exception {
        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();
        reportTemplate.setId(count.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(0)).save(reportTemplate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportTemplate() throws Exception {
        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();
        reportTemplate.setId(count.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(0)).save(reportTemplate);
    }

    @Test
    @Transactional
    void partialUpdateReportTemplateWithPatch() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();

        // Update the reportTemplate using partial update
        ReportTemplate partialUpdatedReportTemplate = new ReportTemplate();
        partialUpdatedReportTemplate.setId(reportTemplate.getId());

        partialUpdatedReportTemplate
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE);

        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);
        ReportTemplate testReportTemplate = reportTemplateList.get(reportTemplateList.size() - 1);
        assertThat(testReportTemplate.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testReportTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReportTemplate.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testReportTemplate.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testReportTemplate.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testReportTemplate.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportTemplate.getCompileReportFile()).isEqualTo(DEFAULT_COMPILE_REPORT_FILE);
        assertThat(testReportTemplate.getCompileReportFileContentType()).isEqualTo(DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateReportTemplateWithPatch() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();

        // Update the reportTemplate using partial update
        ReportTemplate partialUpdatedReportTemplate = new ReportTemplate();
        partialUpdatedReportTemplate.setId(reportTemplate.getId());

        partialUpdatedReportTemplate
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .compileReportFile(UPDATED_COMPILE_REPORT_FILE)
            .compileReportFileContentType(UPDATED_COMPILE_REPORT_FILE_CONTENT_TYPE);

        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);
        ReportTemplate testReportTemplate = reportTemplateList.get(reportTemplateList.size() - 1);
        assertThat(testReportTemplate.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testReportTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportTemplate.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testReportTemplate.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testReportTemplate.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testReportTemplate.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportTemplate.getCompileReportFile()).isEqualTo(UPDATED_COMPILE_REPORT_FILE);
        assertThat(testReportTemplate.getCompileReportFileContentType()).isEqualTo(UPDATED_COMPILE_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingReportTemplate() throws Exception {
        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();
        reportTemplate.setId(count.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(0)).save(reportTemplate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportTemplate() throws Exception {
        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();
        reportTemplate.setId(count.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(0)).save(reportTemplate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportTemplate() throws Exception {
        int databaseSizeBeforeUpdate = reportTemplateRepository.findAll().size();
        reportTemplate.setId(count.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportTemplate in the database
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(0)).save(reportTemplate);
    }

    @Test
    @Transactional
    void deleteReportTemplate() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        int databaseSizeBeforeDelete = reportTemplateRepository.findAll().size();

        // Delete the reportTemplate
        restReportTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportTemplate> reportTemplateList = reportTemplateRepository.findAll();
        assertThat(reportTemplateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReportTemplate in Elasticsearch
        verify(mockReportTemplateSearchRepository, times(1)).deleteById(reportTemplate.getId());
    }

    @Test
    @Transactional
    void searchReportTemplate() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);
        when(mockReportTemplateSearchRepository.search("id:" + reportTemplate.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reportTemplate), PageRequest.of(0, 1), 1));

        // Search the reportTemplate
        restReportTemplateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].compileReportFileContentType").value(hasItem(DEFAULT_COMPILE_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].compileReportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMPILE_REPORT_FILE))));
    }
}
