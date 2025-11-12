package io.github.erp.web.rest;

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
import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.LeaseLiabilityByAccountReport;
import io.github.erp.domain.LeasePeriod;
import io.github.erp.repository.LeaseLiabilityByAccountReportRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityByAccountReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityByAccountReportMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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

/**
 * Integration tests for the {@link LeaseLiabilityByAccountReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseLiabilityByAccountReportResourceIT {

    private static final UUID DEFAULT_REPORT_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TAMPERED = false;
    private static final Boolean UPDATED_TAMPERED = true;

    private static final UUID DEFAULT_FILENAME = UUID.randomUUID();
    private static final UUID UPDATED_FILENAME = UUID.randomUUID();

    private static final String DEFAULT_REPORT_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PARAMETERS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/lease-liability-by-account-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-liability-by-account-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityByAccountReportRepository leaseLiabilityByAccountReportRepository;

    @Autowired
    private LeaseLiabilityByAccountReportMapper leaseLiabilityByAccountReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityByAccountReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityByAccountReportSearchRepository mockLeaseLiabilityByAccountReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityByAccountReportMockMvc;

    private LeaseLiabilityByAccountReport leaseLiabilityByAccountReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityByAccountReport createEntity(EntityManager em) {
        LeaseLiabilityByAccountReport leaseLiabilityByAccountReport = new LeaseLiabilityByAccountReport()
            .reportId(DEFAULT_REPORT_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        leaseLiabilityByAccountReport.setLeasePeriod(leasePeriod);
        return leaseLiabilityByAccountReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityByAccountReport createUpdatedEntity(EntityManager em) {
        LeaseLiabilityByAccountReport leaseLiabilityByAccountReport = new LeaseLiabilityByAccountReport()
            .reportId(UPDATED_REPORT_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createUpdatedEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        leaseLiabilityByAccountReport.setLeasePeriod(leasePeriod);
        return leaseLiabilityByAccountReport;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityByAccountReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseLiabilityByAccountReport() throws Exception {
        int databaseSizeBeforeCreate = leaseLiabilityByAccountReportRepository.findAll().size();
        // Create the LeaseLiabilityByAccountReport
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseLiabilityByAccountReport testLeaseLiabilityByAccountReport = leaseLiabilityByAccountReportList.get(
            leaseLiabilityByAccountReportList.size() - 1
        );
        assertThat(testLeaseLiabilityByAccountReport.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
        assertThat(testLeaseLiabilityByAccountReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityByAccountReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityByAccountReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testLeaseLiabilityByAccountReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testLeaseLiabilityByAccountReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityByAccountReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testLeaseLiabilityByAccountReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(1)).save(testLeaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void createLeaseLiabilityByAccountReportWithExistingId() throws Exception {
        // Create the LeaseLiabilityByAccountReport with an existing ID
        leaseLiabilityByAccountReport.setId(1L);
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        int databaseSizeBeforeCreate = leaseLiabilityByAccountReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(0)).save(leaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void checkReportIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityByAccountReportRepository.findAll().size();
        // set the field null
        leaseLiabilityByAccountReport.setReportId(null);

        // Create the LeaseLiabilityByAccountReport, which fails.
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReports() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList
        restLeaseLiabilityByAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityByAccountReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getLeaseLiabilityByAccountReport() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get the leaseLiabilityByAccountReport
        restLeaseLiabilityByAccountReportMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityByAccountReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityByAccountReport.getId().intValue()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getLeaseLiabilityByAccountReportsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        Long id = leaseLiabilityByAccountReport.getId();

        defaultLeaseLiabilityByAccountReportShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityByAccountReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityByAccountReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportId equals to DEFAULT_REPORT_ID
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportId.equals=" + DEFAULT_REPORT_ID);

        // Get all the leaseLiabilityByAccountReportList where reportId equals to UPDATED_REPORT_ID
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportId.equals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportId not equals to DEFAULT_REPORT_ID
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportId.notEquals=" + DEFAULT_REPORT_ID);

        // Get all the leaseLiabilityByAccountReportList where reportId not equals to UPDATED_REPORT_ID
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportId.notEquals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportId in DEFAULT_REPORT_ID or UPDATED_REPORT_ID
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportId.in=" + DEFAULT_REPORT_ID + "," + UPDATED_REPORT_ID);

        // Get all the leaseLiabilityByAccountReportList where reportId equals to UPDATED_REPORT_ID
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportId.in=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportId is not null
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportId.specified=true");

        // Get all the leaseLiabilityByAccountReportList where reportId is null
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is not null
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is null
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityByAccountReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityByAccountReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum is not null
        defaultLeaseLiabilityByAccountReportShouldBeFound("fileChecksum.specified=true");

        // Get all the leaseLiabilityByAccountReportList where fileChecksum is null
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityByAccountReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityByAccountReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where tampered equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityByAccountReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityByAccountReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where tampered not equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityByAccountReportList where tampered not equals to UPDATED_TAMPERED
        defaultLeaseLiabilityByAccountReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultLeaseLiabilityByAccountReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the leaseLiabilityByAccountReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where tampered is not null
        defaultLeaseLiabilityByAccountReportShouldBeFound("tampered.specified=true");

        // Get all the leaseLiabilityByAccountReportList where tampered is null
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where filename equals to DEFAULT_FILENAME
        defaultLeaseLiabilityByAccountReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityByAccountReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where filename not equals to DEFAULT_FILENAME
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityByAccountReportList where filename not equals to UPDATED_FILENAME
        defaultLeaseLiabilityByAccountReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultLeaseLiabilityByAccountReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the leaseLiabilityByAccountReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where filename is not null
        defaultLeaseLiabilityByAccountReportShouldBeFound("filename.specified=true");

        // Get all the leaseLiabilityByAccountReportList where filename is null
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityByAccountReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityByAccountReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the leaseLiabilityByAccountReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportParameters is not null
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportParameters.specified=true");

        // Get all the leaseLiabilityByAccountReportList where reportParameters is null
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityByAccountReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        // Get all the leaseLiabilityByAccountReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityByAccountReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityByAccountReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);
        ApplicationUser requestedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            requestedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(requestedBy);
            em.flush();
        } else {
            requestedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(requestedBy);
        em.flush();
        leaseLiabilityByAccountReport.setRequestedBy(requestedBy);
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);
        Long requestedById = requestedBy.getId();

        // Get all the leaseLiabilityByAccountReportList where requestedBy equals to requestedById
        defaultLeaseLiabilityByAccountReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the leaseLiabilityByAccountReportList where requestedBy equals to (requestedById + 1)
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportsByLeasePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        em.persist(leasePeriod);
        em.flush();
        leaseLiabilityByAccountReport.setLeasePeriod(leasePeriod);
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);
        Long leasePeriodId = leasePeriod.getId();

        // Get all the leaseLiabilityByAccountReportList where leasePeriod equals to leasePeriodId
        defaultLeaseLiabilityByAccountReportShouldBeFound("leasePeriodId.equals=" + leasePeriodId);

        // Get all the leaseLiabilityByAccountReportList where leasePeriod equals to (leasePeriodId + 1)
        defaultLeaseLiabilityByAccountReportShouldNotBeFound("leasePeriodId.equals=" + (leasePeriodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityByAccountReportShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityByAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityByAccountReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restLeaseLiabilityByAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityByAccountReportShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityByAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityByAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityByAccountReport() throws Exception {
        // Get the leaseLiabilityByAccountReport
        restLeaseLiabilityByAccountReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseLiabilityByAccountReport() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();

        // Update the leaseLiabilityByAccountReport
        LeaseLiabilityByAccountReport updatedLeaseLiabilityByAccountReport = leaseLiabilityByAccountReportRepository
            .findById(leaseLiabilityByAccountReport.getId())
            .get();
        // Disconnect from session so that the updates on updatedLeaseLiabilityByAccountReport are not directly saved in db
        em.detach(updatedLeaseLiabilityByAccountReport);
        updatedLeaseLiabilityByAccountReport
            .reportId(UPDATED_REPORT_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            updatedLeaseLiabilityByAccountReport
        );

        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityByAccountReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityByAccountReport testLeaseLiabilityByAccountReport = leaseLiabilityByAccountReportList.get(
            leaseLiabilityByAccountReportList.size() - 1
        );
        assertThat(testLeaseLiabilityByAccountReport.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testLeaseLiabilityByAccountReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityByAccountReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityByAccountReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityByAccountReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityByAccountReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityByAccountReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityByAccountReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository).save(testLeaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void putNonExistingLeaseLiabilityByAccountReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();
        leaseLiabilityByAccountReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityByAccountReport
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityByAccountReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(0)).save(leaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseLiabilityByAccountReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();
        leaseLiabilityByAccountReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityByAccountReport
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(0)).save(leaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseLiabilityByAccountReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();
        leaseLiabilityByAccountReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityByAccountReport
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(0)).save(leaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void partialUpdateLeaseLiabilityByAccountReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();

        // Update the leaseLiabilityByAccountReport using partial update
        LeaseLiabilityByAccountReport partialUpdatedLeaseLiabilityByAccountReport = new LeaseLiabilityByAccountReport();
        partialUpdatedLeaseLiabilityByAccountReport.setId(leaseLiabilityByAccountReport.getId());

        partialUpdatedLeaseLiabilityByAccountReport
            .reportId(UPDATED_REPORT_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS);

        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityByAccountReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityByAccountReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityByAccountReport testLeaseLiabilityByAccountReport = leaseLiabilityByAccountReportList.get(
            leaseLiabilityByAccountReportList.size() - 1
        );
        assertThat(testLeaseLiabilityByAccountReport.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testLeaseLiabilityByAccountReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityByAccountReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityByAccountReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testLeaseLiabilityByAccountReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityByAccountReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityByAccountReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testLeaseLiabilityByAccountReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseLiabilityByAccountReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();

        // Update the leaseLiabilityByAccountReport using partial update
        LeaseLiabilityByAccountReport partialUpdatedLeaseLiabilityByAccountReport = new LeaseLiabilityByAccountReport();
        partialUpdatedLeaseLiabilityByAccountReport.setId(leaseLiabilityByAccountReport.getId());

        partialUpdatedLeaseLiabilityByAccountReport
            .reportId(UPDATED_REPORT_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityByAccountReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityByAccountReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityByAccountReport testLeaseLiabilityByAccountReport = leaseLiabilityByAccountReportList.get(
            leaseLiabilityByAccountReportList.size() - 1
        );
        assertThat(testLeaseLiabilityByAccountReport.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testLeaseLiabilityByAccountReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityByAccountReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityByAccountReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityByAccountReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityByAccountReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityByAccountReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityByAccountReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseLiabilityByAccountReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();
        leaseLiabilityByAccountReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityByAccountReport
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseLiabilityByAccountReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(0)).save(leaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseLiabilityByAccountReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();
        leaseLiabilityByAccountReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityByAccountReport
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(0)).save(leaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseLiabilityByAccountReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityByAccountReportRepository.findAll().size();
        leaseLiabilityByAccountReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityByAccountReport
        LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportMapper.toDto(
            leaseLiabilityByAccountReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityByAccountReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityByAccountReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityByAccountReport in the database
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(0)).save(leaseLiabilityByAccountReport);
    }

    @Test
    @Transactional
    void deleteLeaseLiabilityByAccountReport() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);

        int databaseSizeBeforeDelete = leaseLiabilityByAccountReportRepository.findAll().size();

        // Delete the leaseLiabilityByAccountReport
        restLeaseLiabilityByAccountReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseLiabilityByAccountReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseLiabilityByAccountReport> leaseLiabilityByAccountReportList = leaseLiabilityByAccountReportRepository.findAll();
        assertThat(leaseLiabilityByAccountReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseLiabilityByAccountReport in Elasticsearch
        verify(mockLeaseLiabilityByAccountReportSearchRepository, times(1)).deleteById(leaseLiabilityByAccountReport.getId());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityByAccountReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityByAccountReportRepository.saveAndFlush(leaseLiabilityByAccountReport);
        when(mockLeaseLiabilityByAccountReportSearchRepository.search("id:" + leaseLiabilityByAccountReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityByAccountReport), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityByAccountReport
        restLeaseLiabilityByAccountReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityByAccountReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityByAccountReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
