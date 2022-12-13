package io.github.erp.web.rest.api;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ReportContentType;
import io.github.erp.domain.SystemContentType;
import io.github.erp.repository.ReportContentTypeRepository;
import io.github.erp.repository.search.ReportContentTypeSearchRepository;
import io.github.erp.service.ReportContentTypeService;
import io.github.erp.service.dto.ReportContentTypeDTO;
import io.github.erp.service.mapper.ReportContentTypeMapper;
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
 * Integration tests for the {@link ReportContentTypeResourceDev} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class ReportContentTypeResourceIT {

    private static final String DEFAULT_REPORT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_FILE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_FILE_EXTENSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev-test/report-content-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev-test/_search/report-content-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportContentTypeRepository reportContentTypeRepository;

    @Mock
    private ReportContentTypeRepository reportContentTypeRepositoryMock;

    @Autowired
    private ReportContentTypeMapper reportContentTypeMapper;

    @Mock
    private ReportContentTypeService reportContentTypeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReportContentTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportContentTypeSearchRepository mockReportContentTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportContentTypeMockMvc;

    private ReportContentType reportContentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportContentType createEntity(EntityManager em) {
        ReportContentType reportContentType = new ReportContentType()
            .reportTypeName(DEFAULT_REPORT_TYPE_NAME)
            .reportFileExtension(DEFAULT_REPORT_FILE_EXTENSION);
        // Add required entity
        SystemContentType systemContentType;
        if (TestUtil.findAll(em, SystemContentType.class).isEmpty()) {
            systemContentType = SystemContentTypeResourceIT.createEntity(em);
            em.persist(systemContentType);
            em.flush();
        } else {
            systemContentType = TestUtil.findAll(em, SystemContentType.class).get(0);
        }
        reportContentType.setSystemContentType(systemContentType);
        return reportContentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportContentType createUpdatedEntity(EntityManager em) {
        ReportContentType reportContentType = new ReportContentType()
            .reportTypeName(UPDATED_REPORT_TYPE_NAME)
            .reportFileExtension(UPDATED_REPORT_FILE_EXTENSION);
        // Add required entity
        SystemContentType systemContentType;
        if (TestUtil.findAll(em, SystemContentType.class).isEmpty()) {
            systemContentType = SystemContentTypeResourceIT.createUpdatedEntity(em);
            em.persist(systemContentType);
            em.flush();
        } else {
            systemContentType = TestUtil.findAll(em, SystemContentType.class).get(0);
        }
        reportContentType.setSystemContentType(systemContentType);
        return reportContentType;
    }

    @BeforeEach
    public void initTest() {
        reportContentType = createEntity(em);
    }

    @Test
    @Transactional
    void createReportContentType() throws Exception {
        int databaseSizeBeforeCreate = reportContentTypeRepository.findAll().size();
        // Create the ReportContentType
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);
        restReportContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ReportContentType testReportContentType = reportContentTypeList.get(reportContentTypeList.size() - 1);
        assertThat(testReportContentType.getReportTypeName()).isEqualTo(DEFAULT_REPORT_TYPE_NAME);
        assertThat(testReportContentType.getReportFileExtension()).isEqualTo(DEFAULT_REPORT_FILE_EXTENSION);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(1)).save(testReportContentType);
    }

    @Test
    @Transactional
    void createReportContentTypeWithExistingId() throws Exception {
        // Create the ReportContentType with an existing ID
        reportContentType.setId(1L);
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        int databaseSizeBeforeCreate = reportContentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(0)).save(reportContentType);
    }

    @Test
    @Transactional
    void checkReportTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportContentTypeRepository.findAll().size();
        // set the field null
        reportContentType.setReportTypeName(null);

        // Create the ReportContentType, which fails.
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        restReportContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportFileExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportContentTypeRepository.findAll().size();
        // set the field null
        reportContentType.setReportFileExtension(null);

        // Create the ReportContentType, which fails.
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        restReportContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportContentTypes() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList
        restReportContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportContentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportTypeName").value(hasItem(DEFAULT_REPORT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].reportFileExtension").value(hasItem(DEFAULT_REPORT_FILE_EXTENSION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportContentTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportContentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportContentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportContentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportContentTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportContentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportContentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportContentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReportContentType() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get the reportContentType
        restReportContentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, reportContentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportContentType.getId().intValue()))
            .andExpect(jsonPath("$.reportTypeName").value(DEFAULT_REPORT_TYPE_NAME))
            .andExpect(jsonPath("$.reportFileExtension").value(DEFAULT_REPORT_FILE_EXTENSION));
    }

    @Test
    @Transactional
    void getReportContentTypesByIdFiltering() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        Long id = reportContentType.getId();

        defaultReportContentTypeShouldBeFound("id.equals=" + id);
        defaultReportContentTypeShouldNotBeFound("id.notEquals=" + id);

        defaultReportContentTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportContentTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultReportContentTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportContentTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportTypeName equals to DEFAULT_REPORT_TYPE_NAME
        defaultReportContentTypeShouldBeFound("reportTypeName.equals=" + DEFAULT_REPORT_TYPE_NAME);

        // Get all the reportContentTypeList where reportTypeName equals to UPDATED_REPORT_TYPE_NAME
        defaultReportContentTypeShouldNotBeFound("reportTypeName.equals=" + UPDATED_REPORT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportTypeName not equals to DEFAULT_REPORT_TYPE_NAME
        defaultReportContentTypeShouldNotBeFound("reportTypeName.notEquals=" + DEFAULT_REPORT_TYPE_NAME);

        // Get all the reportContentTypeList where reportTypeName not equals to UPDATED_REPORT_TYPE_NAME
        defaultReportContentTypeShouldBeFound("reportTypeName.notEquals=" + UPDATED_REPORT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportTypeName in DEFAULT_REPORT_TYPE_NAME or UPDATED_REPORT_TYPE_NAME
        defaultReportContentTypeShouldBeFound("reportTypeName.in=" + DEFAULT_REPORT_TYPE_NAME + "," + UPDATED_REPORT_TYPE_NAME);

        // Get all the reportContentTypeList where reportTypeName equals to UPDATED_REPORT_TYPE_NAME
        defaultReportContentTypeShouldNotBeFound("reportTypeName.in=" + UPDATED_REPORT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportTypeName is not null
        defaultReportContentTypeShouldBeFound("reportTypeName.specified=true");

        // Get all the reportContentTypeList where reportTypeName is null
        defaultReportContentTypeShouldNotBeFound("reportTypeName.specified=false");
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportTypeNameContainsSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportTypeName contains DEFAULT_REPORT_TYPE_NAME
        defaultReportContentTypeShouldBeFound("reportTypeName.contains=" + DEFAULT_REPORT_TYPE_NAME);

        // Get all the reportContentTypeList where reportTypeName contains UPDATED_REPORT_TYPE_NAME
        defaultReportContentTypeShouldNotBeFound("reportTypeName.contains=" + UPDATED_REPORT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportTypeName does not contain DEFAULT_REPORT_TYPE_NAME
        defaultReportContentTypeShouldNotBeFound("reportTypeName.doesNotContain=" + DEFAULT_REPORT_TYPE_NAME);

        // Get all the reportContentTypeList where reportTypeName does not contain UPDATED_REPORT_TYPE_NAME
        defaultReportContentTypeShouldBeFound("reportTypeName.doesNotContain=" + UPDATED_REPORT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportFileExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportFileExtension equals to DEFAULT_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldBeFound("reportFileExtension.equals=" + DEFAULT_REPORT_FILE_EXTENSION);

        // Get all the reportContentTypeList where reportFileExtension equals to UPDATED_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldNotBeFound("reportFileExtension.equals=" + UPDATED_REPORT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportFileExtensionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportFileExtension not equals to DEFAULT_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldNotBeFound("reportFileExtension.notEquals=" + DEFAULT_REPORT_FILE_EXTENSION);

        // Get all the reportContentTypeList where reportFileExtension not equals to UPDATED_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldBeFound("reportFileExtension.notEquals=" + UPDATED_REPORT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportFileExtensionIsInShouldWork() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportFileExtension in DEFAULT_REPORT_FILE_EXTENSION or UPDATED_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldBeFound(
            "reportFileExtension.in=" + DEFAULT_REPORT_FILE_EXTENSION + "," + UPDATED_REPORT_FILE_EXTENSION
        );

        // Get all the reportContentTypeList where reportFileExtension equals to UPDATED_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldNotBeFound("reportFileExtension.in=" + UPDATED_REPORT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportFileExtensionIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportFileExtension is not null
        defaultReportContentTypeShouldBeFound("reportFileExtension.specified=true");

        // Get all the reportContentTypeList where reportFileExtension is null
        defaultReportContentTypeShouldNotBeFound("reportFileExtension.specified=false");
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportFileExtensionContainsSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportFileExtension contains DEFAULT_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldBeFound("reportFileExtension.contains=" + DEFAULT_REPORT_FILE_EXTENSION);

        // Get all the reportContentTypeList where reportFileExtension contains UPDATED_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldNotBeFound("reportFileExtension.contains=" + UPDATED_REPORT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void getAllReportContentTypesByReportFileExtensionNotContainsSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        // Get all the reportContentTypeList where reportFileExtension does not contain DEFAULT_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldNotBeFound("reportFileExtension.doesNotContain=" + DEFAULT_REPORT_FILE_EXTENSION);

        // Get all the reportContentTypeList where reportFileExtension does not contain UPDATED_REPORT_FILE_EXTENSION
        defaultReportContentTypeShouldBeFound("reportFileExtension.doesNotContain=" + UPDATED_REPORT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void getAllReportContentTypesBySystemContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);
        SystemContentType systemContentType;
        if (TestUtil.findAll(em, SystemContentType.class).isEmpty()) {
            systemContentType = SystemContentTypeResourceIT.createEntity(em);
            em.persist(systemContentType);
            em.flush();
        } else {
            systemContentType = TestUtil.findAll(em, SystemContentType.class).get(0);
        }
        em.persist(systemContentType);
        em.flush();
        reportContentType.setSystemContentType(systemContentType);
        reportContentTypeRepository.saveAndFlush(reportContentType);
        Long systemContentTypeId = systemContentType.getId();

        // Get all the reportContentTypeList where systemContentType equals to systemContentTypeId
        defaultReportContentTypeShouldBeFound("systemContentTypeId.equals=" + systemContentTypeId);

        // Get all the reportContentTypeList where systemContentType equals to (systemContentTypeId + 1)
        defaultReportContentTypeShouldNotBeFound("systemContentTypeId.equals=" + (systemContentTypeId + 1));
    }

    @Test
    @Transactional
    void getAllReportContentTypesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);
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
        reportContentType.addPlaceholder(placeholder);
        reportContentTypeRepository.saveAndFlush(reportContentType);
        Long placeholderId = placeholder.getId();

        // Get all the reportContentTypeList where placeholder equals to placeholderId
        defaultReportContentTypeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the reportContentTypeList where placeholder equals to (placeholderId + 1)
        defaultReportContentTypeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportContentTypeShouldBeFound(String filter) throws Exception {
        restReportContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportContentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportTypeName").value(hasItem(DEFAULT_REPORT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].reportFileExtension").value(hasItem(DEFAULT_REPORT_FILE_EXTENSION)));

        // Check, that the count call also returns 1
        restReportContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportContentTypeShouldNotBeFound(String filter) throws Exception {
        restReportContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportContentType() throws Exception {
        // Get the reportContentType
        restReportContentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportContentType() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();

        // Update the reportContentType
        ReportContentType updatedReportContentType = reportContentTypeRepository.findById(reportContentType.getId()).get();
        // Disconnect from session so that the updates on updatedReportContentType are not directly saved in db
        em.detach(updatedReportContentType);
        updatedReportContentType.reportTypeName(UPDATED_REPORT_TYPE_NAME).reportFileExtension(UPDATED_REPORT_FILE_EXTENSION);
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(updatedReportContentType);

        restReportContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportContentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);
        ReportContentType testReportContentType = reportContentTypeList.get(reportContentTypeList.size() - 1);
        assertThat(testReportContentType.getReportTypeName()).isEqualTo(UPDATED_REPORT_TYPE_NAME);
        assertThat(testReportContentType.getReportFileExtension()).isEqualTo(UPDATED_REPORT_FILE_EXTENSION);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository).save(testReportContentType);
    }

    @Test
    @Transactional
    void putNonExistingReportContentType() throws Exception {
        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();
        reportContentType.setId(count.incrementAndGet());

        // Create the ReportContentType
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportContentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(0)).save(reportContentType);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportContentType() throws Exception {
        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();
        reportContentType.setId(count.incrementAndGet());

        // Create the ReportContentType
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(0)).save(reportContentType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportContentType() throws Exception {
        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();
        reportContentType.setId(count.incrementAndGet());

        // Create the ReportContentType
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(0)).save(reportContentType);
    }

    @Test
    @Transactional
    void partialUpdateReportContentTypeWithPatch() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();

        // Update the reportContentType using partial update
        ReportContentType partialUpdatedReportContentType = new ReportContentType();
        partialUpdatedReportContentType.setId(reportContentType.getId());

        partialUpdatedReportContentType.reportTypeName(UPDATED_REPORT_TYPE_NAME).reportFileExtension(UPDATED_REPORT_FILE_EXTENSION);

        restReportContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportContentType))
            )
            .andExpect(status().isOk());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);
        ReportContentType testReportContentType = reportContentTypeList.get(reportContentTypeList.size() - 1);
        assertThat(testReportContentType.getReportTypeName()).isEqualTo(UPDATED_REPORT_TYPE_NAME);
        assertThat(testReportContentType.getReportFileExtension()).isEqualTo(UPDATED_REPORT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void fullUpdateReportContentTypeWithPatch() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();

        // Update the reportContentType using partial update
        ReportContentType partialUpdatedReportContentType = new ReportContentType();
        partialUpdatedReportContentType.setId(reportContentType.getId());

        partialUpdatedReportContentType.reportTypeName(UPDATED_REPORT_TYPE_NAME).reportFileExtension(UPDATED_REPORT_FILE_EXTENSION);

        restReportContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportContentType))
            )
            .andExpect(status().isOk());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);
        ReportContentType testReportContentType = reportContentTypeList.get(reportContentTypeList.size() - 1);
        assertThat(testReportContentType.getReportTypeName()).isEqualTo(UPDATED_REPORT_TYPE_NAME);
        assertThat(testReportContentType.getReportFileExtension()).isEqualTo(UPDATED_REPORT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void patchNonExistingReportContentType() throws Exception {
        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();
        reportContentType.setId(count.incrementAndGet());

        // Create the ReportContentType
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportContentTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(0)).save(reportContentType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportContentType() throws Exception {
        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();
        reportContentType.setId(count.incrementAndGet());

        // Create the ReportContentType
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(0)).save(reportContentType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportContentType() throws Exception {
        int databaseSizeBeforeUpdate = reportContentTypeRepository.findAll().size();
        reportContentType.setId(count.incrementAndGet());

        // Create the ReportContentType
        ReportContentTypeDTO reportContentTypeDTO = reportContentTypeMapper.toDto(reportContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportContentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportContentType in the database
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(0)).save(reportContentType);
    }

    @Test
    @Transactional
    void deleteReportContentType() throws Exception {
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);

        int databaseSizeBeforeDelete = reportContentTypeRepository.findAll().size();

        // Delete the reportContentType
        restReportContentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportContentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportContentType> reportContentTypeList = reportContentTypeRepository.findAll();
        assertThat(reportContentTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReportContentType in Elasticsearch
        verify(mockReportContentTypeSearchRepository, times(1)).deleteById(reportContentType.getId());
    }

    @Test
    @Transactional
    void searchReportContentType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reportContentTypeRepository.saveAndFlush(reportContentType);
        when(mockReportContentTypeSearchRepository.search("id:" + reportContentType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reportContentType), PageRequest.of(0, 1), 1));

        // Search the reportContentType
        restReportContentTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportContentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportContentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportTypeName").value(hasItem(DEFAULT_REPORT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].reportFileExtension").value(hasItem(DEFAULT_REPORT_FILE_EXTENSION)));
    }
}
