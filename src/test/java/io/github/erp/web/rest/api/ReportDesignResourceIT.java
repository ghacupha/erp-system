package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0
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
import io.github.erp.domain.*;
import io.github.erp.repository.ReportDesignRepository;
import io.github.erp.repository.search.ReportDesignSearchRepository;
import io.github.erp.service.ReportDesignService;
import io.github.erp.service.dto.ReportDesignDTO;
import io.github.erp.service.mapper.ReportDesignMapper;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the ReportDesignResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class ReportDesignResourceIT {

    private static final UUID DEFAULT_CATALOGUE_NUMBER = UUID.randomUUID();
    private static final UUID UPDATED_CATALOGUE_NUMBER = UUID.randomUUID();

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

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

    private static final String DEFAULT_REPORT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/report-designs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/report-designs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportDesignRepository reportDesignRepository;

    @Mock
    private ReportDesignRepository reportDesignRepositoryMock;

    @Autowired
    private ReportDesignMapper reportDesignMapper;

    @Mock
    private ReportDesignService reportDesignServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReportDesignSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportDesignSearchRepository mockReportDesignSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportDesignMockMvc;

    private ReportDesign reportDesign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDesign createEntity(EntityManager em) {
        ReportDesign reportDesign = new ReportDesign()
            .catalogueNumber(DEFAULT_CATALOGUE_NUMBER)
            .designation(DEFAULT_DESIGNATION)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .notesContentType(DEFAULT_NOTES_CONTENT_TYPE)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE)
            .reportFileChecksum(DEFAULT_REPORT_FILE_CHECKSUM);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        reportDesign.setSecurityClearance(securityClearance);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        reportDesign.setReportDesigner(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        reportDesign.setOrganization(dealer);
        // Add required entity
        reportDesign.setDepartment(dealer);
        // Add required entity
        SystemModule systemModule;
        if (TestUtil.findAll(em, SystemModule.class).isEmpty()) {
            systemModule = SystemModuleResourceIT.createEntity(em);
            em.persist(systemModule);
            em.flush();
        } else {
            systemModule = TestUtil.findAll(em, SystemModule.class).get(0);
        }
        reportDesign.setSystemModule(systemModule);
        // Add required entity
        Algorithm algorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            algorithm = AlgorithmResourceIT.createEntity(em);
            em.persist(algorithm);
            em.flush();
        } else {
            algorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        reportDesign.setFileCheckSumAlgorithm(algorithm);
        return reportDesign;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDesign createUpdatedEntity(EntityManager em) {
        ReportDesign reportDesign = new ReportDesign()
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createUpdatedEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        reportDesign.setSecurityClearance(securityClearance);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createUpdatedEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        reportDesign.setReportDesigner(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        reportDesign.setOrganization(dealer);
        // Add required entity
        reportDesign.setDepartment(dealer);
        // Add required entity
        SystemModule systemModule;
        if (TestUtil.findAll(em, SystemModule.class).isEmpty()) {
            systemModule = SystemModuleResourceIT.createUpdatedEntity(em);
            em.persist(systemModule);
            em.flush();
        } else {
            systemModule = TestUtil.findAll(em, SystemModule.class).get(0);
        }
        reportDesign.setSystemModule(systemModule);
        // Add required entity
        Algorithm algorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            algorithm = AlgorithmResourceIT.createUpdatedEntity(em);
            em.persist(algorithm);
            em.flush();
        } else {
            algorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        reportDesign.setFileCheckSumAlgorithm(algorithm);
        return reportDesign;
    }

    @BeforeEach
    public void initTest() {
        reportDesign = createEntity(em);
    }

    @Test
    @Transactional
    void createReportDesign() throws Exception {
        int databaseSizeBeforeCreate = reportDesignRepository.findAll().size();
        // Create the ReportDesign
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);
        restReportDesignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeCreate + 1);
        ReportDesign testReportDesign = reportDesignList.get(reportDesignList.size() - 1);
        assertThat(testReportDesign.getCatalogueNumber()).isEqualTo(DEFAULT_CATALOGUE_NUMBER);
        assertThat(testReportDesign.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testReportDesign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReportDesign.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testReportDesign.getNotesContentType()).isEqualTo(DEFAULT_NOTES_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testReportDesign.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFileChecksum()).isEqualTo(DEFAULT_REPORT_FILE_CHECKSUM);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(1)).save(testReportDesign);
    }

    @Test
    @Transactional
    void createReportDesignWithExistingId() throws Exception {
        // Create the ReportDesign with an existing ID
        reportDesign.setId(1L);
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        int databaseSizeBeforeCreate = reportDesignRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportDesignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(0)).save(reportDesign);
    }

    @Test
    @Transactional
    void checkCatalogueNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportDesignRepository.findAll().size();
        // set the field null
        reportDesign.setCatalogueNumber(null);

        // Create the ReportDesign, which fails.
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        restReportDesignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportDesignRepository.findAll().size();
        // set the field null
        reportDesign.setDesignation(null);

        // Create the ReportDesign, which fails.
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        restReportDesignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportDesigns() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList
        restReportDesignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportDesign.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportFileChecksum").value(hasItem(DEFAULT_REPORT_FILE_CHECKSUM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportDesignsWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportDesignServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportDesignMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportDesignServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportDesignsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportDesignServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportDesignMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportDesignServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReportDesign() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get the reportDesign
        restReportDesignMockMvc
            .perform(get(ENTITY_API_URL_ID, reportDesign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportDesign.getId().intValue()))
            .andExpect(jsonPath("$.catalogueNumber").value(DEFAULT_CATALOGUE_NUMBER.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.notesContentType").value(DEFAULT_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.notes").value(Base64Utils.encodeToString(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)))
            .andExpect(jsonPath("$.reportFileChecksum").value(DEFAULT_REPORT_FILE_CHECKSUM));
    }

    @Test
    @Transactional
    void getReportDesignsByIdFiltering() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        Long id = reportDesign.getId();

        defaultReportDesignShouldBeFound("id.equals=" + id);
        defaultReportDesignShouldNotBeFound("id.notEquals=" + id);

        defaultReportDesignShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportDesignShouldNotBeFound("id.greaterThan=" + id);

        defaultReportDesignShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportDesignShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportDesignsByCatalogueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where catalogueNumber equals to DEFAULT_CATALOGUE_NUMBER
        defaultReportDesignShouldBeFound("catalogueNumber.equals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the reportDesignList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultReportDesignShouldNotBeFound("catalogueNumber.equals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportDesignsByCatalogueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where catalogueNumber not equals to DEFAULT_CATALOGUE_NUMBER
        defaultReportDesignShouldNotBeFound("catalogueNumber.notEquals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the reportDesignList where catalogueNumber not equals to UPDATED_CATALOGUE_NUMBER
        defaultReportDesignShouldBeFound("catalogueNumber.notEquals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportDesignsByCatalogueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where catalogueNumber in DEFAULT_CATALOGUE_NUMBER or UPDATED_CATALOGUE_NUMBER
        defaultReportDesignShouldBeFound("catalogueNumber.in=" + DEFAULT_CATALOGUE_NUMBER + "," + UPDATED_CATALOGUE_NUMBER);

        // Get all the reportDesignList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultReportDesignShouldNotBeFound("catalogueNumber.in=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportDesignsByCatalogueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where catalogueNumber is not null
        defaultReportDesignShouldBeFound("catalogueNumber.specified=true");

        // Get all the reportDesignList where catalogueNumber is null
        defaultReportDesignShouldNotBeFound("catalogueNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllReportDesignsByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where designation equals to DEFAULT_DESIGNATION
        defaultReportDesignShouldBeFound("designation.equals=" + DEFAULT_DESIGNATION);

        // Get all the reportDesignList where designation equals to UPDATED_DESIGNATION
        defaultReportDesignShouldNotBeFound("designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllReportDesignsByDesignationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where designation not equals to DEFAULT_DESIGNATION
        defaultReportDesignShouldNotBeFound("designation.notEquals=" + DEFAULT_DESIGNATION);

        // Get all the reportDesignList where designation not equals to UPDATED_DESIGNATION
        defaultReportDesignShouldBeFound("designation.notEquals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllReportDesignsByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where designation in DEFAULT_DESIGNATION or UPDATED_DESIGNATION
        defaultReportDesignShouldBeFound("designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION);

        // Get all the reportDesignList where designation equals to UPDATED_DESIGNATION
        defaultReportDesignShouldNotBeFound("designation.in=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllReportDesignsByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where designation is not null
        defaultReportDesignShouldBeFound("designation.specified=true");

        // Get all the reportDesignList where designation is null
        defaultReportDesignShouldNotBeFound("designation.specified=false");
    }

    @Test
    @Transactional
    void getAllReportDesignsByDesignationContainsSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where designation contains DEFAULT_DESIGNATION
        defaultReportDesignShouldBeFound("designation.contains=" + DEFAULT_DESIGNATION);

        // Get all the reportDesignList where designation contains UPDATED_DESIGNATION
        defaultReportDesignShouldNotBeFound("designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllReportDesignsByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where designation does not contain DEFAULT_DESIGNATION
        defaultReportDesignShouldNotBeFound("designation.doesNotContain=" + DEFAULT_DESIGNATION);

        // Get all the reportDesignList where designation does not contain UPDATED_DESIGNATION
        defaultReportDesignShouldBeFound("designation.doesNotContain=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllReportDesignsByReportFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where reportFileChecksum equals to DEFAULT_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldBeFound("reportFileChecksum.equals=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the reportDesignList where reportFileChecksum equals to UPDATED_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldNotBeFound("reportFileChecksum.equals=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllReportDesignsByReportFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where reportFileChecksum not equals to DEFAULT_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldNotBeFound("reportFileChecksum.notEquals=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the reportDesignList where reportFileChecksum not equals to UPDATED_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldBeFound("reportFileChecksum.notEquals=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllReportDesignsByReportFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where reportFileChecksum in DEFAULT_REPORT_FILE_CHECKSUM or UPDATED_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldBeFound("reportFileChecksum.in=" + DEFAULT_REPORT_FILE_CHECKSUM + "," + UPDATED_REPORT_FILE_CHECKSUM);

        // Get all the reportDesignList where reportFileChecksum equals to UPDATED_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldNotBeFound("reportFileChecksum.in=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllReportDesignsByReportFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where reportFileChecksum is not null
        defaultReportDesignShouldBeFound("reportFileChecksum.specified=true");

        // Get all the reportDesignList where reportFileChecksum is null
        defaultReportDesignShouldNotBeFound("reportFileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllReportDesignsByReportFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where reportFileChecksum contains DEFAULT_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldBeFound("reportFileChecksum.contains=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the reportDesignList where reportFileChecksum contains UPDATED_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldNotBeFound("reportFileChecksum.contains=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllReportDesignsByReportFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        // Get all the reportDesignList where reportFileChecksum does not contain DEFAULT_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldNotBeFound("reportFileChecksum.doesNotContain=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the reportDesignList where reportFileChecksum does not contain UPDATED_REPORT_FILE_CHECKSUM
        defaultReportDesignShouldBeFound("reportFileChecksum.doesNotContain=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllReportDesignsByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        UniversallyUniqueMapping parameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            parameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(parameters);
            em.flush();
        } else {
            parameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(parameters);
        em.flush();
        reportDesign.addParameters(parameters);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long parametersId = parameters.getId();

        // Get all the reportDesignList where parameters equals to parametersId
        defaultReportDesignShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the reportDesignList where parameters equals to (parametersId + 1)
        defaultReportDesignShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    @Test
    @Transactional
    void getAllReportDesignsBySecurityClearanceIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        em.persist(securityClearance);
        em.flush();
        reportDesign.setSecurityClearance(securityClearance);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long securityClearanceId = securityClearance.getId();

        // Get all the reportDesignList where securityClearance equals to securityClearanceId
        defaultReportDesignShouldBeFound("securityClearanceId.equals=" + securityClearanceId);

        // Get all the reportDesignList where securityClearance equals to (securityClearanceId + 1)
        defaultReportDesignShouldNotBeFound("securityClearanceId.equals=" + (securityClearanceId + 1));
    }

    @Test
    @Transactional
    void getAllReportDesignsByReportDesignerIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        ApplicationUser reportDesigner;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            reportDesigner = ApplicationUserResourceIT.createEntity(em);
            em.persist(reportDesigner);
            em.flush();
        } else {
            reportDesigner = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(reportDesigner);
        em.flush();
        reportDesign.setReportDesigner(reportDesigner);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long reportDesignerId = reportDesigner.getId();

        // Get all the reportDesignList where reportDesigner equals to reportDesignerId
        defaultReportDesignShouldBeFound("reportDesignerId.equals=" + reportDesignerId);

        // Get all the reportDesignList where reportDesigner equals to (reportDesignerId + 1)
        defaultReportDesignShouldNotBeFound("reportDesignerId.equals=" + (reportDesignerId + 1));
    }

    @Test
    @Transactional
    void getAllReportDesignsByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        Dealer organization;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            organization = DealerResourceIT.createEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(organization);
        em.flush();
        reportDesign.setOrganization(organization);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long organizationId = organization.getId();

        // Get all the reportDesignList where organization equals to organizationId
        defaultReportDesignShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the reportDesignList where organization equals to (organizationId + 1)
        defaultReportDesignShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    @Test
    @Transactional
    void getAllReportDesignsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        Dealer department;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            department = DealerResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(department);
        em.flush();
        reportDesign.setDepartment(department);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long departmentId = department.getId();

        // Get all the reportDesignList where department equals to departmentId
        defaultReportDesignShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the reportDesignList where department equals to (departmentId + 1)
        defaultReportDesignShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllReportDesignsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
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
        reportDesign.addPlaceholder(placeholder);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long placeholderId = placeholder.getId();

        // Get all the reportDesignList where placeholder equals to placeholderId
        defaultReportDesignShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the reportDesignList where placeholder equals to (placeholderId + 1)
        defaultReportDesignShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllReportDesignsBySystemModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        SystemModule systemModule;
        if (TestUtil.findAll(em, SystemModule.class).isEmpty()) {
            systemModule = SystemModuleResourceIT.createEntity(em);
            em.persist(systemModule);
            em.flush();
        } else {
            systemModule = TestUtil.findAll(em, SystemModule.class).get(0);
        }
        em.persist(systemModule);
        em.flush();
        reportDesign.setSystemModule(systemModule);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long systemModuleId = systemModule.getId();

        // Get all the reportDesignList where systemModule equals to systemModuleId
        defaultReportDesignShouldBeFound("systemModuleId.equals=" + systemModuleId);

        // Get all the reportDesignList where systemModule equals to (systemModuleId + 1)
        defaultReportDesignShouldNotBeFound("systemModuleId.equals=" + (systemModuleId + 1));
    }

    @Test
    @Transactional
    void getAllReportDesignsByFileCheckSumAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        Algorithm fileCheckSumAlgorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            fileCheckSumAlgorithm = AlgorithmResourceIT.createEntity(em);
            em.persist(fileCheckSumAlgorithm);
            em.flush();
        } else {
            fileCheckSumAlgorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        em.persist(fileCheckSumAlgorithm);
        em.flush();
        reportDesign.setFileCheckSumAlgorithm(fileCheckSumAlgorithm);
        reportDesignRepository.saveAndFlush(reportDesign);
        Long fileCheckSumAlgorithmId = fileCheckSumAlgorithm.getId();

        // Get all the reportDesignList where fileCheckSumAlgorithm equals to fileCheckSumAlgorithmId
        defaultReportDesignShouldBeFound("fileCheckSumAlgorithmId.equals=" + fileCheckSumAlgorithmId);

        // Get all the reportDesignList where fileCheckSumAlgorithm equals to (fileCheckSumAlgorithmId + 1)
        defaultReportDesignShouldNotBeFound("fileCheckSumAlgorithmId.equals=" + (fileCheckSumAlgorithmId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportDesignShouldBeFound(String filter) throws Exception {
        restReportDesignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportDesign.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportFileChecksum").value(hasItem(DEFAULT_REPORT_FILE_CHECKSUM)));

        // Check, that the count call also returns 1
        restReportDesignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportDesignShouldNotBeFound(String filter) throws Exception {
        restReportDesignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportDesignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportDesign() throws Exception {
        // Get the reportDesign
        restReportDesignMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportDesign() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();

        // Update the reportDesign
        ReportDesign updatedReportDesign = reportDesignRepository.findById(reportDesign.getId()).get();
        // Disconnect from session so that the updates on updatedReportDesign are not directly saved in db
        em.detach(updatedReportDesign);
        updatedReportDesign
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM);
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(updatedReportDesign);

        restReportDesignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDesignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);
        ReportDesign testReportDesign = reportDesignList.get(reportDesignList.size() - 1);
        assertThat(testReportDesign.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testReportDesign.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testReportDesign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportDesign.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testReportDesign.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testReportDesign.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFileChecksum()).isEqualTo(UPDATED_REPORT_FILE_CHECKSUM);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository).save(testReportDesign);
    }

    @Test
    @Transactional
    void putNonExistingReportDesign() throws Exception {
        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();
        reportDesign.setId(count.incrementAndGet());

        // Create the ReportDesign
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDesignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDesignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(0)).save(reportDesign);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportDesign() throws Exception {
        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();
        reportDesign.setId(count.incrementAndGet());

        // Create the ReportDesign
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDesignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(0)).save(reportDesign);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportDesign() throws Exception {
        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();
        reportDesign.setId(count.incrementAndGet());

        // Create the ReportDesign
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDesignMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(0)).save(reportDesign);
    }

    @Test
    @Transactional
    void partialUpdateReportDesignWithPatch() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();

        // Update the reportDesign using partial update
        ReportDesign partialUpdatedReportDesign = new ReportDesign();
        partialUpdatedReportDesign.setId(reportDesign.getId());

        partialUpdatedReportDesign
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM);

        restReportDesignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDesign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportDesign))
            )
            .andExpect(status().isOk());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);
        ReportDesign testReportDesign = reportDesignList.get(reportDesignList.size() - 1);
        assertThat(testReportDesign.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testReportDesign.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testReportDesign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportDesign.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testReportDesign.getNotesContentType()).isEqualTo(DEFAULT_NOTES_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testReportDesign.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFileChecksum()).isEqualTo(UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void fullUpdateReportDesignWithPatch() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();

        // Update the reportDesign using partial update
        ReportDesign partialUpdatedReportDesign = new ReportDesign();
        partialUpdatedReportDesign.setId(reportDesign.getId());

        partialUpdatedReportDesign
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM);

        restReportDesignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDesign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportDesign))
            )
            .andExpect(status().isOk());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);
        ReportDesign testReportDesign = reportDesignList.get(reportDesignList.size() - 1);
        assertThat(testReportDesign.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testReportDesign.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testReportDesign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportDesign.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testReportDesign.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testReportDesign.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testReportDesign.getReportFileChecksum()).isEqualTo(UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void patchNonExistingReportDesign() throws Exception {
        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();
        reportDesign.setId(count.incrementAndGet());

        // Create the ReportDesign
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDesignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportDesignDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(0)).save(reportDesign);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportDesign() throws Exception {
        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();
        reportDesign.setId(count.incrementAndGet());

        // Create the ReportDesign
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDesignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(0)).save(reportDesign);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportDesign() throws Exception {
        int databaseSizeBeforeUpdate = reportDesignRepository.findAll().size();
        reportDesign.setId(count.incrementAndGet());

        // Create the ReportDesign
        ReportDesignDTO reportDesignDTO = reportDesignMapper.toDto(reportDesign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDesignMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDesignDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDesign in the database
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(0)).save(reportDesign);
    }

    @Test
    @Transactional
    void deleteReportDesign() throws Exception {
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);

        int databaseSizeBeforeDelete = reportDesignRepository.findAll().size();

        // Delete the reportDesign
        restReportDesignMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportDesign.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportDesign> reportDesignList = reportDesignRepository.findAll();
        assertThat(reportDesignList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReportDesign in Elasticsearch
        verify(mockReportDesignSearchRepository, times(1)).deleteById(reportDesign.getId());
    }

    @Test
    @Transactional
    void searchReportDesign() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reportDesignRepository.saveAndFlush(reportDesign);
        when(mockReportDesignSearchRepository.search("id:" + reportDesign.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reportDesign), PageRequest.of(0, 1), 1));

        // Search the reportDesign
        restReportDesignMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportDesign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportDesign.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportFileChecksum").value(hasItem(DEFAULT_REPORT_FILE_CHECKSUM)));
    }
}
