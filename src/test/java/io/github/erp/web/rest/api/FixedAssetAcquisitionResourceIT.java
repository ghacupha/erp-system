package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 9 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.FixedAssetAcquisition;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.FixedAssetAcquisitionRepository;
import io.github.erp.repository.search.FixedAssetAcquisitionSearchRepository;
import io.github.erp.service.FixedAssetAcquisitionService;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
import io.github.erp.service.mapper.FixedAssetAcquisitionMapper;
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
 * Integration tests for the {@link FixedAssetAcquisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class FixedAssetAcquisitionResourceIT {

    private static final Long DEFAULT_ASSET_NUMBER = 1L;
    private static final Long UPDATED_ASSET_NUMBER = 2L;
    private static final Long SMALLER_ASSET_NUMBER = 1L - 1L;

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PURCHASE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PURCHASE_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PURCHASE_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/fixed-asset-acquisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/fixed-asset-acquisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository;

    @Mock
    private FixedAssetAcquisitionRepository fixedAssetAcquisitionRepositoryMock;

    @Autowired
    private FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper;

    @Mock
    private FixedAssetAcquisitionService fixedAssetAcquisitionServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FixedAssetAcquisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private FixedAssetAcquisitionSearchRepository mockFixedAssetAcquisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFixedAssetAcquisitionMockMvc;

    private FixedAssetAcquisition fixedAssetAcquisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetAcquisition createEntity(EntityManager em) {
        FixedAssetAcquisition fixedAssetAcquisition = new FixedAssetAcquisition()
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .assetTag(DEFAULT_ASSET_TAG)
            .assetDescription(DEFAULT_ASSET_DESCRIPTION)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .assetCategory(DEFAULT_ASSET_CATEGORY)
            .purchasePrice(DEFAULT_PURCHASE_PRICE)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN);
        return fixedAssetAcquisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetAcquisition createUpdatedEntity(EntityManager em) {
        FixedAssetAcquisition fixedAssetAcquisition = new FixedAssetAcquisition()
            .assetNumber(UPDATED_ASSET_NUMBER)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);
        return fixedAssetAcquisition;
    }

    @BeforeEach
    public void initTest() {
        fixedAssetAcquisition = createEntity(em);
    }

    @Test
    @Transactional
    void createFixedAssetAcquisition() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetAcquisitionRepository.findAll().size();
        // Create the FixedAssetAcquisition
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);
        restFixedAssetAcquisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeCreate + 1);
        FixedAssetAcquisition testFixedAssetAcquisition = fixedAssetAcquisitionList.get(fixedAssetAcquisitionList.size() - 1);
        assertThat(testFixedAssetAcquisition.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testFixedAssetAcquisition.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetAcquisition.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testFixedAssetAcquisition.getAssetDescription()).isEqualTo(DEFAULT_ASSET_DESCRIPTION);
        assertThat(testFixedAssetAcquisition.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testFixedAssetAcquisition.getAssetCategory()).isEqualTo(DEFAULT_ASSET_CATEGORY);
        assertThat(testFixedAssetAcquisition.getPurchasePrice()).isEqualByComparingTo(DEFAULT_PURCHASE_PRICE);
        assertThat(testFixedAssetAcquisition.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(1)).save(testFixedAssetAcquisition);
    }

    @Test
    @Transactional
    void createFixedAssetAcquisitionWithExistingId() throws Exception {
        // Create the FixedAssetAcquisition with an existing ID
        fixedAssetAcquisition.setId(1L);
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);

        int databaseSizeBeforeCreate = fixedAssetAcquisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFixedAssetAcquisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(0)).save(fixedAssetAcquisition);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitions() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList
        restFixedAssetAcquisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(sameNumber(DEFAULT_PURCHASE_PRICE))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFixedAssetAcquisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fixedAssetAcquisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFixedAssetAcquisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fixedAssetAcquisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFixedAssetAcquisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fixedAssetAcquisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFixedAssetAcquisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fixedAssetAcquisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFixedAssetAcquisition() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get the fixedAssetAcquisition
        restFixedAssetAcquisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, fixedAssetAcquisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fixedAssetAcquisition.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.assetDescription").value(DEFAULT_ASSET_DESCRIPTION))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.assetCategory").value(DEFAULT_ASSET_CATEGORY))
            .andExpect(jsonPath("$.purchasePrice").value(sameNumber(DEFAULT_PURCHASE_PRICE)))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN));
    }

    @Test
    @Transactional
    void getFixedAssetAcquisitionsByIdFiltering() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        Long id = fixedAssetAcquisition.getId();

        defaultFixedAssetAcquisitionShouldBeFound("id.equals=" + id);
        defaultFixedAssetAcquisitionShouldNotBeFound("id.notEquals=" + id);

        defaultFixedAssetAcquisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFixedAssetAcquisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultFixedAssetAcquisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFixedAssetAcquisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetAcquisitionList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetAcquisitionList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the fixedAssetAcquisitionList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber is not null
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.specified=true");

        // Get all the fixedAssetAcquisitionList where assetNumber is null
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber is greater than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.greaterThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetAcquisitionList where assetNumber is greater than or equal to UPDATED_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.greaterThanOrEqual=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber is less than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.lessThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetAcquisitionList where assetNumber is less than or equal to SMALLER_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.lessThanOrEqual=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber is less than DEFAULT_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.lessThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetAcquisitionList where assetNumber is less than UPDATED_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.lessThan=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetNumber is greater than DEFAULT_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldNotBeFound("assetNumber.greaterThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetAcquisitionList where assetNumber is greater than SMALLER_ASSET_NUMBER
        defaultFixedAssetAcquisitionShouldBeFound("assetNumber.greaterThan=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldBeFound(
            "serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE
        );

        // Get all the fixedAssetAcquisitionList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode is not null
        defaultFixedAssetAcquisitionShouldBeFound("serviceOutletCode.specified=true");

        // Get all the fixedAssetAcquisitionList where serviceOutletCode is null
        defaultFixedAssetAcquisitionShouldNotBeFound("serviceOutletCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetAcquisitionList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetAcquisitionShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetTag equals to DEFAULT_ASSET_TAG
        defaultFixedAssetAcquisitionShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetAcquisitionList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetAcquisitionShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultFixedAssetAcquisitionShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetAcquisitionList where assetTag not equals to UPDATED_ASSET_TAG
        defaultFixedAssetAcquisitionShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultFixedAssetAcquisitionShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the fixedAssetAcquisitionList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetAcquisitionShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetTag is not null
        defaultFixedAssetAcquisitionShouldBeFound("assetTag.specified=true");

        // Get all the fixedAssetAcquisitionList where assetTag is null
        defaultFixedAssetAcquisitionShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetTag contains DEFAULT_ASSET_TAG
        defaultFixedAssetAcquisitionShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetAcquisitionList where assetTag contains UPDATED_ASSET_TAG
        defaultFixedAssetAcquisitionShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultFixedAssetAcquisitionShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetAcquisitionList where assetTag does not contain UPDATED_ASSET_TAG
        defaultFixedAssetAcquisitionShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetDescription equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldBeFound("assetDescription.equals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetAcquisitionList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldNotBeFound("assetDescription.equals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetDescription not equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldNotBeFound("assetDescription.notEquals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetAcquisitionList where assetDescription not equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldBeFound("assetDescription.notEquals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetDescription in DEFAULT_ASSET_DESCRIPTION or UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldBeFound("assetDescription.in=" + DEFAULT_ASSET_DESCRIPTION + "," + UPDATED_ASSET_DESCRIPTION);

        // Get all the fixedAssetAcquisitionList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldNotBeFound("assetDescription.in=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetDescription is not null
        defaultFixedAssetAcquisitionShouldBeFound("assetDescription.specified=true");

        // Get all the fixedAssetAcquisitionList where assetDescription is null
        defaultFixedAssetAcquisitionShouldNotBeFound("assetDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetDescription contains DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldBeFound("assetDescription.contains=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetAcquisitionList where assetDescription contains UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldNotBeFound("assetDescription.contains=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetDescription does not contain DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldNotBeFound("assetDescription.doesNotContain=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetAcquisitionList where assetDescription does not contain UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetAcquisitionShouldBeFound("assetDescription.doesNotContain=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the fixedAssetAcquisitionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate not equals to DEFAULT_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.notEquals=" + DEFAULT_PURCHASE_DATE);

        // Get all the fixedAssetAcquisitionList where purchaseDate not equals to UPDATED_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.notEquals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the fixedAssetAcquisitionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate is not null
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.specified=true");

        // Get all the fixedAssetAcquisitionList where purchaseDate is null
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate is greater than or equal to DEFAULT_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the fixedAssetAcquisitionList where purchaseDate is greater than or equal to UPDATED_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate is less than or equal to DEFAULT_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the fixedAssetAcquisitionList where purchaseDate is less than or equal to SMALLER_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate is less than DEFAULT_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the fixedAssetAcquisitionList where purchaseDate is less than UPDATED_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchaseDate is greater than DEFAULT_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the fixedAssetAcquisitionList where purchaseDate is greater than SMALLER_PURCHASE_DATE
        defaultFixedAssetAcquisitionShouldBeFound("purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetCategory equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldBeFound("assetCategory.equals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetAcquisitionList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldNotBeFound("assetCategory.equals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetCategory not equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldNotBeFound("assetCategory.notEquals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetAcquisitionList where assetCategory not equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldBeFound("assetCategory.notEquals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetCategory in DEFAULT_ASSET_CATEGORY or UPDATED_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldBeFound("assetCategory.in=" + DEFAULT_ASSET_CATEGORY + "," + UPDATED_ASSET_CATEGORY);

        // Get all the fixedAssetAcquisitionList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldNotBeFound("assetCategory.in=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetCategory is not null
        defaultFixedAssetAcquisitionShouldBeFound("assetCategory.specified=true");

        // Get all the fixedAssetAcquisitionList where assetCategory is null
        defaultFixedAssetAcquisitionShouldNotBeFound("assetCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetCategoryContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetCategory contains DEFAULT_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldBeFound("assetCategory.contains=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetAcquisitionList where assetCategory contains UPDATED_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldNotBeFound("assetCategory.contains=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByAssetCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where assetCategory does not contain DEFAULT_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldNotBeFound("assetCategory.doesNotContain=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetAcquisitionList where assetCategory does not contain UPDATED_ASSET_CATEGORY
        defaultFixedAssetAcquisitionShouldBeFound("assetCategory.doesNotContain=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice equals to DEFAULT_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.equals=" + DEFAULT_PURCHASE_PRICE);

        // Get all the fixedAssetAcquisitionList where purchasePrice equals to UPDATED_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.equals=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice not equals to DEFAULT_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.notEquals=" + DEFAULT_PURCHASE_PRICE);

        // Get all the fixedAssetAcquisitionList where purchasePrice not equals to UPDATED_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.notEquals=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice in DEFAULT_PURCHASE_PRICE or UPDATED_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.in=" + DEFAULT_PURCHASE_PRICE + "," + UPDATED_PURCHASE_PRICE);

        // Get all the fixedAssetAcquisitionList where purchasePrice equals to UPDATED_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.in=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice is not null
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.specified=true");

        // Get all the fixedAssetAcquisitionList where purchasePrice is null
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice is greater than or equal to DEFAULT_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.greaterThanOrEqual=" + DEFAULT_PURCHASE_PRICE);

        // Get all the fixedAssetAcquisitionList where purchasePrice is greater than or equal to UPDATED_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.greaterThanOrEqual=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice is less than or equal to DEFAULT_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.lessThanOrEqual=" + DEFAULT_PURCHASE_PRICE);

        // Get all the fixedAssetAcquisitionList where purchasePrice is less than or equal to SMALLER_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.lessThanOrEqual=" + SMALLER_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice is less than DEFAULT_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.lessThan=" + DEFAULT_PURCHASE_PRICE);

        // Get all the fixedAssetAcquisitionList where purchasePrice is less than UPDATED_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.lessThan=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPurchasePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where purchasePrice is greater than DEFAULT_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldNotBeFound("purchasePrice.greaterThan=" + DEFAULT_PURCHASE_PRICE);

        // Get all the fixedAssetAcquisitionList where purchasePrice is greater than SMALLER_PURCHASE_PRICE
        defaultFixedAssetAcquisitionShouldBeFound("purchasePrice.greaterThan=" + SMALLER_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetAcquisitionList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetAcquisitionList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetAcquisitionList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where fileUploadToken is not null
        defaultFixedAssetAcquisitionShouldBeFound("fileUploadToken.specified=true");

        // Get all the fixedAssetAcquisitionList where fileUploadToken is null
        defaultFixedAssetAcquisitionShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetAcquisitionList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        // Get all the fixedAssetAcquisitionList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetAcquisitionList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetAcquisitionShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFixedAssetAcquisitionsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);
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
        fixedAssetAcquisition.addPlaceholder(placeholder);
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);
        Long placeholderId = placeholder.getId();

        // Get all the fixedAssetAcquisitionList where placeholder equals to placeholderId
        defaultFixedAssetAcquisitionShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fixedAssetAcquisitionList where placeholder equals to (placeholderId + 1)
        defaultFixedAssetAcquisitionShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFixedAssetAcquisitionShouldBeFound(String filter) throws Exception {
        restFixedAssetAcquisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(sameNumber(DEFAULT_PURCHASE_PRICE))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)));

        // Check, that the count call also returns 1
        restFixedAssetAcquisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFixedAssetAcquisitionShouldNotBeFound(String filter) throws Exception {
        restFixedAssetAcquisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFixedAssetAcquisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFixedAssetAcquisition() throws Exception {
        // Get the fixedAssetAcquisition
        restFixedAssetAcquisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFixedAssetAcquisition() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();

        // Update the fixedAssetAcquisition
        FixedAssetAcquisition updatedFixedAssetAcquisition = fixedAssetAcquisitionRepository.findById(fixedAssetAcquisition.getId()).get();
        // Disconnect from session so that the updates on updatedFixedAssetAcquisition are not directly saved in db
        em.detach(updatedFixedAssetAcquisition);
        updatedFixedAssetAcquisition
            .assetNumber(UPDATED_ASSET_NUMBER)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(updatedFixedAssetAcquisition);

        restFixedAssetAcquisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fixedAssetAcquisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetAcquisition testFixedAssetAcquisition = fixedAssetAcquisitionList.get(fixedAssetAcquisitionList.size() - 1);
        assertThat(testFixedAssetAcquisition.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testFixedAssetAcquisition.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetAcquisition.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testFixedAssetAcquisition.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testFixedAssetAcquisition.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testFixedAssetAcquisition.getAssetCategory()).isEqualTo(UPDATED_ASSET_CATEGORY);
        assertThat(testFixedAssetAcquisition.getPurchasePrice()).isEqualTo(UPDATED_PURCHASE_PRICE);
        assertThat(testFixedAssetAcquisition.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository).save(testFixedAssetAcquisition);
    }

    @Test
    @Transactional
    void putNonExistingFixedAssetAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();
        fixedAssetAcquisition.setId(count.incrementAndGet());

        // Create the FixedAssetAcquisition
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedAssetAcquisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fixedAssetAcquisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(0)).save(fixedAssetAcquisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchFixedAssetAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();
        fixedAssetAcquisition.setId(count.incrementAndGet());

        // Create the FixedAssetAcquisition
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetAcquisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(0)).save(fixedAssetAcquisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFixedAssetAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();
        fixedAssetAcquisition.setId(count.incrementAndGet());

        // Create the FixedAssetAcquisition
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetAcquisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(0)).save(fixedAssetAcquisition);
    }

    @Test
    @Transactional
    void partialUpdateFixedAssetAcquisitionWithPatch() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();

        // Update the fixedAssetAcquisition using partial update
        FixedAssetAcquisition partialUpdatedFixedAssetAcquisition = new FixedAssetAcquisition();
        partialUpdatedFixedAssetAcquisition.setId(fixedAssetAcquisition.getId());

        partialUpdatedFixedAssetAcquisition
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);

        restFixedAssetAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFixedAssetAcquisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFixedAssetAcquisition))
            )
            .andExpect(status().isOk());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetAcquisition testFixedAssetAcquisition = fixedAssetAcquisitionList.get(fixedAssetAcquisitionList.size() - 1);
        assertThat(testFixedAssetAcquisition.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testFixedAssetAcquisition.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetAcquisition.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testFixedAssetAcquisition.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testFixedAssetAcquisition.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testFixedAssetAcquisition.getAssetCategory()).isEqualTo(DEFAULT_ASSET_CATEGORY);
        assertThat(testFixedAssetAcquisition.getPurchasePrice()).isEqualByComparingTo(UPDATED_PURCHASE_PRICE);
        assertThat(testFixedAssetAcquisition.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateFixedAssetAcquisitionWithPatch() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();

        // Update the fixedAssetAcquisition using partial update
        FixedAssetAcquisition partialUpdatedFixedAssetAcquisition = new FixedAssetAcquisition();
        partialUpdatedFixedAssetAcquisition.setId(fixedAssetAcquisition.getId());

        partialUpdatedFixedAssetAcquisition
            .assetNumber(UPDATED_ASSET_NUMBER)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);

        restFixedAssetAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFixedAssetAcquisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFixedAssetAcquisition))
            )
            .andExpect(status().isOk());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetAcquisition testFixedAssetAcquisition = fixedAssetAcquisitionList.get(fixedAssetAcquisitionList.size() - 1);
        assertThat(testFixedAssetAcquisition.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testFixedAssetAcquisition.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetAcquisition.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testFixedAssetAcquisition.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testFixedAssetAcquisition.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testFixedAssetAcquisition.getAssetCategory()).isEqualTo(UPDATED_ASSET_CATEGORY);
        assertThat(testFixedAssetAcquisition.getPurchasePrice()).isEqualByComparingTo(UPDATED_PURCHASE_PRICE);
        assertThat(testFixedAssetAcquisition.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingFixedAssetAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();
        fixedAssetAcquisition.setId(count.incrementAndGet());

        // Create the FixedAssetAcquisition
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedAssetAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fixedAssetAcquisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(0)).save(fixedAssetAcquisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFixedAssetAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();
        fixedAssetAcquisition.setId(count.incrementAndGet());

        // Create the FixedAssetAcquisition
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(0)).save(fixedAssetAcquisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFixedAssetAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetAcquisitionRepository.findAll().size();
        fixedAssetAcquisition.setId(count.incrementAndGet());

        // Create the FixedAssetAcquisition
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFixedAssetAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fixedAssetAcquisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FixedAssetAcquisition in the database
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(0)).save(fixedAssetAcquisition);
    }

    @Test
    @Transactional
    void deleteFixedAssetAcquisition() throws Exception {
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);

        int databaseSizeBeforeDelete = fixedAssetAcquisitionRepository.findAll().size();

        // Delete the fixedAssetAcquisition
        restFixedAssetAcquisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, fixedAssetAcquisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FixedAssetAcquisition> fixedAssetAcquisitionList = fixedAssetAcquisitionRepository.findAll();
        assertThat(fixedAssetAcquisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FixedAssetAcquisition in Elasticsearch
        verify(mockFixedAssetAcquisitionSearchRepository, times(1)).deleteById(fixedAssetAcquisition.getId());
    }

    @Test
    @Transactional
    void searchFixedAssetAcquisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fixedAssetAcquisitionRepository.saveAndFlush(fixedAssetAcquisition);
        when(mockFixedAssetAcquisitionSearchRepository.search("id:" + fixedAssetAcquisition.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fixedAssetAcquisition), PageRequest.of(0, 1), 1));

        // Search the fixedAssetAcquisition
        restFixedAssetAcquisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fixedAssetAcquisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(sameNumber(DEFAULT_PURCHASE_PRICE))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)));
    }
}
