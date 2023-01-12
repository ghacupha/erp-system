package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 9 (Caleb Series) Server ver 0.4.0
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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.FixedAssetDepreciation;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.enumeration.DepreciationRegime;
import io.github.erp.repository.FixedAssetDepreciationRepository;
import io.github.erp.repository.search.FixedAssetDepreciationSearchRepository;
import io.github.erp.service.FixedAssetDepreciationService;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import io.github.erp.service.mapper.FixedAssetDepreciationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

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

/**
 * Integration tests for the {@link FixedAssetDepreciationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class FixedAssetDepreciationResourceIT {

    private static final Long DEFAULT_ASSET_NUMBER = 1L;
    private static final Long UPDATED_ASSET_NUMBER = 2L;
    private static final Long SMALLER_ASSET_NUMBER = 1L - 1L;

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEPRECIATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPRECIATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPRECIATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final DepreciationRegime DEFAULT_DEPRECIATION_REGIME = DepreciationRegime.STRAIGHT_LINE_BASIS;
    private static final DepreciationRegime UPDATED_DEPRECIATION_REGIME = DepreciationRegime.DECLINING_BALANCE_BASIS;

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fixed-asset/fixed-asset-depreciations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/fixed-asset-depreciations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FixedAssetDepreciationRepository fixedAssetDepreciationRepository;

    @Mock
    private FixedAssetDepreciationRepository fixedAssetDepreciationRepositoryMock;

    @Autowired
    private FixedAssetDepreciationMapper fixedAssetDepreciationMapper;

    @Mock
    private FixedAssetDepreciationService fixedAssetDepreciationServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FixedAssetDepreciationSearchRepositoryMockConfiguration
     */
    @Autowired
    private FixedAssetDepreciationSearchRepository mockFixedAssetDepreciationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFixedAssetDepreciationMockMvc;

    private FixedAssetDepreciation fixedAssetDepreciation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetDepreciation createEntity(EntityManager em) {
        FixedAssetDepreciation fixedAssetDepreciation = new FixedAssetDepreciation()
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .assetTag(DEFAULT_ASSET_TAG)
            .assetDescription(DEFAULT_ASSET_DESCRIPTION)
            .depreciationDate(DEFAULT_DEPRECIATION_DATE)
            .assetCategory(DEFAULT_ASSET_CATEGORY)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .depreciationRegime(DEFAULT_DEPRECIATION_REGIME)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        return fixedAssetDepreciation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetDepreciation createUpdatedEntity(EntityManager em) {
        FixedAssetDepreciation fixedAssetDepreciation = new FixedAssetDepreciation()
            .assetNumber(UPDATED_ASSET_NUMBER)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .depreciationDate(UPDATED_DEPRECIATION_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .depreciationRegime(UPDATED_DEPRECIATION_REGIME)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        return fixedAssetDepreciation;
    }

    @BeforeEach
    public void initTest() {
        fixedAssetDepreciation = createEntity(em);
    }

    @Test
    @Transactional
    void createFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetDepreciationRepository.findAll().size();
        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);
        restFixedAssetDepreciationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeCreate + 1);
        FixedAssetDepreciation testFixedAssetDepreciation = fixedAssetDepreciationList.get(fixedAssetDepreciationList.size() - 1);
        assertThat(testFixedAssetDepreciation.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testFixedAssetDepreciation.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetDepreciation.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testFixedAssetDepreciation.getAssetDescription()).isEqualTo(DEFAULT_ASSET_DESCRIPTION);
        assertThat(testFixedAssetDepreciation.getDepreciationDate()).isEqualTo(DEFAULT_DEPRECIATION_DATE);
        assertThat(testFixedAssetDepreciation.getAssetCategory()).isEqualTo(DEFAULT_ASSET_CATEGORY);
        assertThat(testFixedAssetDepreciation.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testFixedAssetDepreciation.getDepreciationRegime()).isEqualTo(DEFAULT_DEPRECIATION_REGIME);
        assertThat(testFixedAssetDepreciation.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetDepreciation.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(1)).save(testFixedAssetDepreciation);
    }

    @Test
    @Transactional
    void createFixedAssetDepreciationWithExistingId() throws Exception {
        // Create the FixedAssetDepreciation with an existing ID
        fixedAssetDepreciation.setId(1L);
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        int databaseSizeBeforeCreate = fixedAssetDepreciationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFixedAssetDepreciationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeCreate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciations() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList
        restFixedAssetDepreciationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetDepreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFixedAssetDepreciationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fixedAssetDepreciationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFixedAssetDepreciationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fixedAssetDepreciationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFixedAssetDepreciationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fixedAssetDepreciationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFixedAssetDepreciationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fixedAssetDepreciationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFixedAssetDepreciation() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc
            .perform(get(ENTITY_API_URL_ID, fixedAssetDepreciation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fixedAssetDepreciation.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.assetDescription").value(DEFAULT_ASSET_DESCRIPTION))
            .andExpect(jsonPath("$.depreciationDate").value(DEFAULT_DEPRECIATION_DATE.toString()))
            .andExpect(jsonPath("$.assetCategory").value(DEFAULT_ASSET_CATEGORY))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.depreciationRegime").value(DEFAULT_DEPRECIATION_REGIME.toString()))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }

    @Test
    @Transactional
    void getFixedAssetDepreciationsByIdFiltering() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        Long id = fixedAssetDepreciation.getId();

        defaultFixedAssetDepreciationShouldBeFound("id.equals=" + id);
        defaultFixedAssetDepreciationShouldNotBeFound("id.notEquals=" + id);

        defaultFixedAssetDepreciationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFixedAssetDepreciationShouldNotBeFound("id.greaterThan=" + id);

        defaultFixedAssetDepreciationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFixedAssetDepreciationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is not null
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.specified=true");

        // Get all the fixedAssetDepreciationList where assetNumber is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.greaterThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than or equal to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.greaterThanOrEqual=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is less than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.lessThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is less than or equal to SMALLER_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.lessThanOrEqual=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is less than DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.lessThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is less than UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.lessThan=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.greaterThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than SMALLER_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.greaterThan=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound(
            "serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE
        );

        // Get all the fixedAssetDepreciationList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode is not null
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.specified=true");

        // Get all the fixedAssetDepreciationList where serviceOutletCode is null
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag equals to DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag not equals to UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag is not null
        defaultFixedAssetDepreciationShouldBeFound("assetTag.specified=true");

        // Get all the fixedAssetDepreciationList where assetTag is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag contains DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag contains UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag does not contain UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.equals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.equals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription not equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.notEquals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription not equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.notEquals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription in DEFAULT_ASSET_DESCRIPTION or UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.in=" + DEFAULT_ASSET_DESCRIPTION + "," + UPDATED_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.in=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription is not null
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.specified=true");

        // Get all the fixedAssetDepreciationList where assetDescription is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription contains DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.contains=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription contains UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.contains=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription does not contain DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.doesNotContain=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription does not contain UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.doesNotContain=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate equals to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.equals=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate equals to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.equals=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate not equals to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.notEquals=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate not equals to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.notEquals=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate in DEFAULT_DEPRECIATION_DATE or UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.in=" + DEFAULT_DEPRECIATION_DATE + "," + UPDATED_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate equals to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.in=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is not null
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.specified=true");

        // Get all the fixedAssetDepreciationList where depreciationDate is null
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than or equal to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than or equal to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than or equal to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than or equal to SMALLER_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.lessThanOrEqual=" + SMALLER_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.lessThan=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.lessThan=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.greaterThan=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than SMALLER_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.greaterThan=" + SMALLER_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.equals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.equals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory not equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.notEquals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory not equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.notEquals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory in DEFAULT_ASSET_CATEGORY or UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.in=" + DEFAULT_ASSET_CATEGORY + "," + UPDATED_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.in=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory is not null
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.specified=true");

        // Get all the fixedAssetDepreciationList where assetCategory is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetCategoryContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory contains DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.contains=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory contains UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.contains=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByAssetCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory does not contain DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.doesNotContain=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory does not contain UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.doesNotContain=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound(
            "depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT
        );

        // Get all the fixedAssetDepreciationList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is not null
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.specified=true");

        // Get all the fixedAssetDepreciationList where depreciationAmount is null
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationRegimeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime equals to DEFAULT_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldBeFound("depreciationRegime.equals=" + DEFAULT_DEPRECIATION_REGIME);

        // Get all the fixedAssetDepreciationList where depreciationRegime equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.equals=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationRegimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime not equals to DEFAULT_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.notEquals=" + DEFAULT_DEPRECIATION_REGIME);

        // Get all the fixedAssetDepreciationList where depreciationRegime not equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldBeFound("depreciationRegime.notEquals=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationRegimeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime in DEFAULT_DEPRECIATION_REGIME or UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldBeFound(
            "depreciationRegime.in=" + DEFAULT_DEPRECIATION_REGIME + "," + UPDATED_DEPRECIATION_REGIME
        );

        // Get all the fixedAssetDepreciationList where depreciationRegime equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.in=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByDepreciationRegimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime is not null
        defaultFixedAssetDepreciationShouldBeFound("depreciationRegime.specified=true");

        // Get all the fixedAssetDepreciationList where depreciationRegime is null
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken is not null
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.specified=true");

        // Get all the fixedAssetDepreciationList where fileUploadToken is null
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken is not null
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.specified=true");

        // Get all the fixedAssetDepreciationList where compilationToken is null
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetDepreciationsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);
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
        fixedAssetDepreciation.addPlaceholder(placeholder);
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);
        Long placeholderId = placeholder.getId();

        // Get all the fixedAssetDepreciationList where placeholder equals to placeholderId
        defaultFixedAssetDepreciationShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fixedAssetDepreciationList where placeholder equals to (placeholderId + 1)
        defaultFixedAssetDepreciationShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFixedAssetDepreciationShouldBeFound(String filter) throws Exception {
        restFixedAssetDepreciationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetDepreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restFixedAssetDepreciationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFixedAssetDepreciationShouldNotBeFound(String filter) throws Exception {
        restFixedAssetDepreciationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFixedAssetDepreciationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFixedAssetDepreciation() throws Exception {
        // Get the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFixedAssetDepreciation() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();

        // Update the fixedAssetDepreciation
        FixedAssetDepreciation updatedFixedAssetDepreciation = fixedAssetDepreciationRepository
            .findById(fixedAssetDepreciation.getId())
            .get();
        // Disconnect from session so that the updates on updatedFixedAssetDepreciation are not directly saved in db
        em.detach(updatedFixedAssetDepreciation);
        updatedFixedAssetDepreciation
            .assetNumber(UPDATED_ASSET_NUMBER)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .depreciationDate(UPDATED_DEPRECIATION_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .depreciationRegime(UPDATED_DEPRECIATION_REGIME)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(updatedFixedAssetDepreciation);

        restFixedAssetDepreciationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fixedAssetDepreciationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isOk());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetDepreciation testFixedAssetDepreciation = fixedAssetDepreciationList.get(fixedAssetDepreciationList.size() - 1);
        assertThat(testFixedAssetDepreciation.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testFixedAssetDepreciation.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetDepreciation.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testFixedAssetDepreciation.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testFixedAssetDepreciation.getDepreciationDate()).isEqualTo(UPDATED_DEPRECIATION_DATE);
        assertThat(testFixedAssetDepreciation.getAssetCategory()).isEqualTo(UPDATED_ASSET_CATEGORY);
        assertThat(testFixedAssetDepreciation.getDepreciationAmount()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testFixedAssetDepreciation.getDepreciationRegime()).isEqualTo(UPDATED_DEPRECIATION_REGIME);
        assertThat(testFixedAssetDepreciation.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetDepreciation.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository).save(testFixedAssetDepreciation);
    }

    @Test
    @Transactional
    void putNonExistingFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();
        fixedAssetDepreciation.setId(count.incrementAndGet());

        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedAssetDepreciationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fixedAssetDepreciationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    void putWithIdMismatchFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();
        fixedAssetDepreciation.setId(count.incrementAndGet());

        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetDepreciationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();
        fixedAssetDepreciation.setId(count.incrementAndGet());

        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetDepreciationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    void partialUpdateFixedAssetDepreciationWithPatch() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();

        // Update the fixedAssetDepreciation using partial update
        FixedAssetDepreciation partialUpdatedFixedAssetDepreciation = new FixedAssetDepreciation();
        partialUpdatedFixedAssetDepreciation.setId(fixedAssetDepreciation.getId());

        partialUpdatedFixedAssetDepreciation
            .assetNumber(UPDATED_ASSET_NUMBER)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restFixedAssetDepreciationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFixedAssetDepreciation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFixedAssetDepreciation))
            )
            .andExpect(status().isOk());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetDepreciation testFixedAssetDepreciation = fixedAssetDepreciationList.get(fixedAssetDepreciationList.size() - 1);
        assertThat(testFixedAssetDepreciation.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testFixedAssetDepreciation.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetDepreciation.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testFixedAssetDepreciation.getAssetDescription()).isEqualTo(DEFAULT_ASSET_DESCRIPTION);
        assertThat(testFixedAssetDepreciation.getDepreciationDate()).isEqualTo(DEFAULT_DEPRECIATION_DATE);
        assertThat(testFixedAssetDepreciation.getAssetCategory()).isEqualTo(DEFAULT_ASSET_CATEGORY);
        assertThat(testFixedAssetDepreciation.getDepreciationAmount()).isEqualByComparingTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testFixedAssetDepreciation.getDepreciationRegime()).isEqualTo(DEFAULT_DEPRECIATION_REGIME);
        assertThat(testFixedAssetDepreciation.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetDepreciation.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateFixedAssetDepreciationWithPatch() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();

        // Update the fixedAssetDepreciation using partial update
        FixedAssetDepreciation partialUpdatedFixedAssetDepreciation = new FixedAssetDepreciation();
        partialUpdatedFixedAssetDepreciation.setId(fixedAssetDepreciation.getId());

        partialUpdatedFixedAssetDepreciation
            .assetNumber(UPDATED_ASSET_NUMBER)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .depreciationDate(UPDATED_DEPRECIATION_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .depreciationRegime(UPDATED_DEPRECIATION_REGIME)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restFixedAssetDepreciationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFixedAssetDepreciation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFixedAssetDepreciation))
            )
            .andExpect(status().isOk());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetDepreciation testFixedAssetDepreciation = fixedAssetDepreciationList.get(fixedAssetDepreciationList.size() - 1);
        assertThat(testFixedAssetDepreciation.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testFixedAssetDepreciation.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetDepreciation.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testFixedAssetDepreciation.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testFixedAssetDepreciation.getDepreciationDate()).isEqualTo(UPDATED_DEPRECIATION_DATE);
        assertThat(testFixedAssetDepreciation.getAssetCategory()).isEqualTo(UPDATED_ASSET_CATEGORY);
        assertThat(testFixedAssetDepreciation.getDepreciationAmount()).isEqualByComparingTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testFixedAssetDepreciation.getDepreciationRegime()).isEqualTo(UPDATED_DEPRECIATION_REGIME);
        assertThat(testFixedAssetDepreciation.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetDepreciation.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();
        fixedAssetDepreciation.setId(count.incrementAndGet());

        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedAssetDepreciationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fixedAssetDepreciationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();
        fixedAssetDepreciation.setId(count.incrementAndGet());

        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetDepreciationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();
        fixedAssetDepreciation.setId(count.incrementAndGet());

        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetDepreciationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    void deleteFixedAssetDepreciation() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        int databaseSizeBeforeDelete = fixedAssetDepreciationRepository.findAll().size();

        // Delete the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc
            .perform(delete(ENTITY_API_URL_ID, fixedAssetDepreciation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(1)).deleteById(fixedAssetDepreciation.getId());
    }

    @Test
    @Transactional
    void searchFixedAssetDepreciation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);
        when(mockFixedAssetDepreciationSearchRepository.search("id:" + fixedAssetDepreciation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fixedAssetDepreciation), PageRequest.of(0, 1), 1));

        // Search the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fixedAssetDepreciation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetDepreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
