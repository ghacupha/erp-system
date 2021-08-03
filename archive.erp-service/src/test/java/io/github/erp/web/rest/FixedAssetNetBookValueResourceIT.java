package io.github.erp.web.rest;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.github.erp.ErpServiceApp;
import io.github.erp.config.SecurityBeanOverrideConfiguration;
import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.repository.FixedAssetNetBookValueRepository;
import io.github.erp.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.erp.service.FixedAssetNetBookValueService;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.service.mapper.FixedAssetNetBookValueMapper;
import io.github.erp.service.dto.FixedAssetNetBookValueCriteria;
import io.github.erp.service.FixedAssetNetBookValueQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.domain.enumeration.DepreciationRegime;
/**
 * Integration tests for the {@link FixedAssetNetBookValueResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ErpServiceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FixedAssetNetBookValueResourceIT {

    private static final Long DEFAULT_ASSET_NUMBER = 1L;
    private static final Long UPDATED_ASSET_NUMBER = 2L;
    private static final Long SMALLER_ASSET_NUMBER = 1L - 1L;

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NET_BOOK_VALUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NET_BOOK_VALUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NET_BOOK_VALUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE = new BigDecimal(1 - 1);

    private static final DepreciationRegime DEFAULT_DEPRECIATION_REGIME = DepreciationRegime.STRAIGHT_LINE_BASIS;
    private static final DepreciationRegime UPDATED_DEPRECIATION_REGIME = DepreciationRegime.DECLINING_BALANCE_BASIS;

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    @Autowired
    private FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository;

    @Autowired
    private FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper;

    @Autowired
    private FixedAssetNetBookValueService fixedAssetNetBookValueService;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FixedAssetNetBookValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private FixedAssetNetBookValueSearchRepository mockFixedAssetNetBookValueSearchRepository;

    @Autowired
    private FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFixedAssetNetBookValueMockMvc;

    private FixedAssetNetBookValue fixedAssetNetBookValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetNetBookValue createEntity(EntityManager em) {
        FixedAssetNetBookValue fixedAssetNetBookValue = new FixedAssetNetBookValue();
        fixedAssetNetBookValue.setAssetNumber(DEFAULT_ASSET_NUMBER);
        fixedAssetNetBookValue.setServiceOutletCode(DEFAULT_SERVICE_OUTLET_CODE);
        fixedAssetNetBookValue.setAssetTag(DEFAULT_ASSET_TAG);
        fixedAssetNetBookValue.setAssetDescription(DEFAULT_ASSET_DESCRIPTION);
        fixedAssetNetBookValue.setNetBookValueDate(DEFAULT_NET_BOOK_VALUE_DATE);
        fixedAssetNetBookValue.setAssetCategory(DEFAULT_ASSET_CATEGORY);
        fixedAssetNetBookValue.setNetBookValue(DEFAULT_NET_BOOK_VALUE);
        fixedAssetNetBookValue.setDepreciationRegime(DEFAULT_DEPRECIATION_REGIME);
        fixedAssetNetBookValue.setFileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN);
        fixedAssetNetBookValue.setCompilationToken(DEFAULT_COMPILATION_TOKEN);
        return fixedAssetNetBookValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetNetBookValue createUpdatedEntity(EntityManager em) {
        FixedAssetNetBookValue fixedAssetNetBookValue = new FixedAssetNetBookValue();
        fixedAssetNetBookValue.setAssetNumber(UPDATED_ASSET_NUMBER);
        fixedAssetNetBookValue.setServiceOutletCode(UPDATED_SERVICE_OUTLET_CODE);
        fixedAssetNetBookValue.setAssetTag(UPDATED_ASSET_TAG);
        fixedAssetNetBookValue.setAssetDescription(UPDATED_ASSET_DESCRIPTION);
        fixedAssetNetBookValue.setNetBookValueDate(UPDATED_NET_BOOK_VALUE_DATE);
        fixedAssetNetBookValue.setAssetCategory(UPDATED_ASSET_CATEGORY);
        fixedAssetNetBookValue.setNetBookValue(UPDATED_NET_BOOK_VALUE);
        fixedAssetNetBookValue.setDepreciationRegime(UPDATED_DEPRECIATION_REGIME);
        fixedAssetNetBookValue.setFileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);
        fixedAssetNetBookValue.setCompilationToken(UPDATED_COMPILATION_TOKEN);
        return fixedAssetNetBookValue;
    }

    @BeforeEach
    public void initTest() {
        fixedAssetNetBookValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createFixedAssetNetBookValue() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetNetBookValueRepository.findAll().size();
        // Create the FixedAssetNetBookValue
        FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO = fixedAssetNetBookValueMapper.toDto(fixedAssetNetBookValue);
        restFixedAssetNetBookValueMockMvc.perform(post("/api/fixed-asset-net-book-values").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetNetBookValueDTO)))
            .andExpect(status().isCreated());

        // Validate the FixedAssetNetBookValue in the database
        List<FixedAssetNetBookValue> fixedAssetNetBookValueList = fixedAssetNetBookValueRepository.findAll();
        assertThat(fixedAssetNetBookValueList).hasSize(databaseSizeBeforeCreate + 1);
        FixedAssetNetBookValue testFixedAssetNetBookValue = fixedAssetNetBookValueList.get(fixedAssetNetBookValueList.size() - 1);
        assertThat(testFixedAssetNetBookValue.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testFixedAssetNetBookValue.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetNetBookValue.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testFixedAssetNetBookValue.getAssetDescription()).isEqualTo(DEFAULT_ASSET_DESCRIPTION);
        assertThat(testFixedAssetNetBookValue.getNetBookValueDate()).isEqualTo(DEFAULT_NET_BOOK_VALUE_DATE);
        assertThat(testFixedAssetNetBookValue.getAssetCategory()).isEqualTo(DEFAULT_ASSET_CATEGORY);
        assertThat(testFixedAssetNetBookValue.getNetBookValue()).isEqualTo(DEFAULT_NET_BOOK_VALUE);
        assertThat(testFixedAssetNetBookValue.getDepreciationRegime()).isEqualTo(DEFAULT_DEPRECIATION_REGIME);
        assertThat(testFixedAssetNetBookValue.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetNetBookValue.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the FixedAssetNetBookValue in Elasticsearch
        verify(mockFixedAssetNetBookValueSearchRepository, times(1)).save(testFixedAssetNetBookValue);
    }

    @Test
    @Transactional
    public void createFixedAssetNetBookValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetNetBookValueRepository.findAll().size();

        // Create the FixedAssetNetBookValue with an existing ID
        fixedAssetNetBookValue.setId(1L);
        FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO = fixedAssetNetBookValueMapper.toDto(fixedAssetNetBookValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFixedAssetNetBookValueMockMvc.perform(post("/api/fixed-asset-net-book-values").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetNetBookValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetNetBookValue in the database
        List<FixedAssetNetBookValue> fixedAssetNetBookValueList = fixedAssetNetBookValueRepository.findAll();
        assertThat(fixedAssetNetBookValueList).hasSize(databaseSizeBeforeCreate);

        // Validate the FixedAssetNetBookValue in Elasticsearch
        verify(mockFixedAssetNetBookValueSearchRepository, times(0)).save(fixedAssetNetBookValue);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValues() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList
        restFixedAssetNetBookValueMockMvc.perform(get("/api/fixed-asset-net-book-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetNetBookValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].netBookValueDate").value(hasItem(DEFAULT_NET_BOOK_VALUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(DEFAULT_NET_BOOK_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
    
    @Test
    @Transactional
    public void getFixedAssetNetBookValue() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get the fixedAssetNetBookValue
        restFixedAssetNetBookValueMockMvc.perform(get("/api/fixed-asset-net-book-values/{id}", fixedAssetNetBookValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fixedAssetNetBookValue.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.assetDescription").value(DEFAULT_ASSET_DESCRIPTION))
            .andExpect(jsonPath("$.netBookValueDate").value(DEFAULT_NET_BOOK_VALUE_DATE.toString()))
            .andExpect(jsonPath("$.assetCategory").value(DEFAULT_ASSET_CATEGORY))
            .andExpect(jsonPath("$.netBookValue").value(DEFAULT_NET_BOOK_VALUE.intValue()))
            .andExpect(jsonPath("$.depreciationRegime").value(DEFAULT_DEPRECIATION_REGIME.toString()))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }


    @Test
    @Transactional
    public void getFixedAssetNetBookValuesByIdFiltering() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        Long id = fixedAssetNetBookValue.getId();

        defaultFixedAssetNetBookValueShouldBeFound("id.equals=" + id);
        defaultFixedAssetNetBookValueShouldNotBeFound("id.notEquals=" + id);

        defaultFixedAssetNetBookValueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFixedAssetNetBookValueShouldNotBeFound("id.greaterThan=" + id);

        defaultFixedAssetNetBookValueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFixedAssetNetBookValueShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetNetBookValueList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetNetBookValueList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the fixedAssetNetBookValueList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber is not null
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.specified=true");

        // Get all the fixedAssetNetBookValueList where assetNumber is null
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber is greater than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.greaterThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetNetBookValueList where assetNumber is greater than or equal to UPDATED_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.greaterThanOrEqual=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber is less than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.lessThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetNetBookValueList where assetNumber is less than or equal to SMALLER_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.lessThanOrEqual=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber is less than DEFAULT_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.lessThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetNetBookValueList where assetNumber is less than UPDATED_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.lessThan=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetNumber is greater than DEFAULT_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldNotBeFound("assetNumber.greaterThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetNetBookValueList where assetNumber is greater than SMALLER_ASSET_NUMBER
        defaultFixedAssetNetBookValueShouldBeFound("assetNumber.greaterThan=" + SMALLER_ASSET_NUMBER);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldBeFound("serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode is not null
        defaultFixedAssetNetBookValueShouldBeFound("serviceOutletCode.specified=true");

        // Get all the fixedAssetNetBookValueList where serviceOutletCode is null
        defaultFixedAssetNetBookValueShouldNotBeFound("serviceOutletCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetNetBookValueList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetNetBookValueShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetTag equals to DEFAULT_ASSET_TAG
        defaultFixedAssetNetBookValueShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetNetBookValueList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetNetBookValueShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultFixedAssetNetBookValueShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetNetBookValueList where assetTag not equals to UPDATED_ASSET_TAG
        defaultFixedAssetNetBookValueShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultFixedAssetNetBookValueShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the fixedAssetNetBookValueList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetNetBookValueShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetTag is not null
        defaultFixedAssetNetBookValueShouldBeFound("assetTag.specified=true");

        // Get all the fixedAssetNetBookValueList where assetTag is null
        defaultFixedAssetNetBookValueShouldNotBeFound("assetTag.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetTag contains DEFAULT_ASSET_TAG
        defaultFixedAssetNetBookValueShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetNetBookValueList where assetTag contains UPDATED_ASSET_TAG
        defaultFixedAssetNetBookValueShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultFixedAssetNetBookValueShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetNetBookValueList where assetTag does not contain UPDATED_ASSET_TAG
        defaultFixedAssetNetBookValueShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetDescription equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldBeFound("assetDescription.equals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetNetBookValueList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldNotBeFound("assetDescription.equals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetDescription not equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldNotBeFound("assetDescription.notEquals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetNetBookValueList where assetDescription not equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldBeFound("assetDescription.notEquals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetDescription in DEFAULT_ASSET_DESCRIPTION or UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldBeFound("assetDescription.in=" + DEFAULT_ASSET_DESCRIPTION + "," + UPDATED_ASSET_DESCRIPTION);

        // Get all the fixedAssetNetBookValueList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldNotBeFound("assetDescription.in=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetDescription is not null
        defaultFixedAssetNetBookValueShouldBeFound("assetDescription.specified=true");

        // Get all the fixedAssetNetBookValueList where assetDescription is null
        defaultFixedAssetNetBookValueShouldNotBeFound("assetDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetDescription contains DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldBeFound("assetDescription.contains=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetNetBookValueList where assetDescription contains UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldNotBeFound("assetDescription.contains=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetDescription does not contain DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldNotBeFound("assetDescription.doesNotContain=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetNetBookValueList where assetDescription does not contain UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetNetBookValueShouldBeFound("assetDescription.doesNotContain=" + UPDATED_ASSET_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate equals to DEFAULT_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.equals=" + DEFAULT_NET_BOOK_VALUE_DATE);

        // Get all the fixedAssetNetBookValueList where netBookValueDate equals to UPDATED_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.equals=" + UPDATED_NET_BOOK_VALUE_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate not equals to DEFAULT_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.notEquals=" + DEFAULT_NET_BOOK_VALUE_DATE);

        // Get all the fixedAssetNetBookValueList where netBookValueDate not equals to UPDATED_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.notEquals=" + UPDATED_NET_BOOK_VALUE_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate in DEFAULT_NET_BOOK_VALUE_DATE or UPDATED_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.in=" + DEFAULT_NET_BOOK_VALUE_DATE + "," + UPDATED_NET_BOOK_VALUE_DATE);

        // Get all the fixedAssetNetBookValueList where netBookValueDate equals to UPDATED_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.in=" + UPDATED_NET_BOOK_VALUE_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is not null
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.specified=true");

        // Get all the fixedAssetNetBookValueList where netBookValueDate is null
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is greater than or equal to DEFAULT_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE_DATE);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is greater than or equal to UPDATED_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is less than or equal to DEFAULT_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE_DATE);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is less than or equal to SMALLER_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is less than DEFAULT_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.lessThan=" + DEFAULT_NET_BOOK_VALUE_DATE);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is less than UPDATED_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.lessThan=" + UPDATED_NET_BOOK_VALUE_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is greater than DEFAULT_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValueDate.greaterThan=" + DEFAULT_NET_BOOK_VALUE_DATE);

        // Get all the fixedAssetNetBookValueList where netBookValueDate is greater than SMALLER_NET_BOOK_VALUE_DATE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValueDate.greaterThan=" + SMALLER_NET_BOOK_VALUE_DATE);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetCategory equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldBeFound("assetCategory.equals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetNetBookValueList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldNotBeFound("assetCategory.equals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetCategory not equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldNotBeFound("assetCategory.notEquals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetNetBookValueList where assetCategory not equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldBeFound("assetCategory.notEquals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetCategory in DEFAULT_ASSET_CATEGORY or UPDATED_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldBeFound("assetCategory.in=" + DEFAULT_ASSET_CATEGORY + "," + UPDATED_ASSET_CATEGORY);

        // Get all the fixedAssetNetBookValueList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldNotBeFound("assetCategory.in=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetCategory is not null
        defaultFixedAssetNetBookValueShouldBeFound("assetCategory.specified=true");

        // Get all the fixedAssetNetBookValueList where assetCategory is null
        defaultFixedAssetNetBookValueShouldNotBeFound("assetCategory.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetCategoryContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetCategory contains DEFAULT_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldBeFound("assetCategory.contains=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetNetBookValueList where assetCategory contains UPDATED_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldNotBeFound("assetCategory.contains=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByAssetCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where assetCategory does not contain DEFAULT_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldNotBeFound("assetCategory.doesNotContain=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetNetBookValueList where assetCategory does not contain UPDATED_ASSET_CATEGORY
        defaultFixedAssetNetBookValueShouldBeFound("assetCategory.doesNotContain=" + UPDATED_ASSET_CATEGORY);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue equals to DEFAULT_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.equals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the fixedAssetNetBookValueList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.equals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue not equals to DEFAULT_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.notEquals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the fixedAssetNetBookValueList where netBookValue not equals to UPDATED_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.notEquals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue in DEFAULT_NET_BOOK_VALUE or UPDATED_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.in=" + DEFAULT_NET_BOOK_VALUE + "," + UPDATED_NET_BOOK_VALUE);

        // Get all the fixedAssetNetBookValueList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.in=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue is not null
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.specified=true");

        // Get all the fixedAssetNetBookValueList where netBookValue is null
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue is greater than or equal to DEFAULT_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the fixedAssetNetBookValueList where netBookValue is greater than or equal to UPDATED_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue is less than or equal to DEFAULT_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the fixedAssetNetBookValueList where netBookValue is less than or equal to SMALLER_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue is less than DEFAULT_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.lessThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the fixedAssetNetBookValueList where netBookValue is less than UPDATED_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.lessThan=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByNetBookValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where netBookValue is greater than DEFAULT_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldNotBeFound("netBookValue.greaterThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the fixedAssetNetBookValueList where netBookValue is greater than SMALLER_NET_BOOK_VALUE
        defaultFixedAssetNetBookValueShouldBeFound("netBookValue.greaterThan=" + SMALLER_NET_BOOK_VALUE);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByDepreciationRegimeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where depreciationRegime equals to DEFAULT_DEPRECIATION_REGIME
        defaultFixedAssetNetBookValueShouldBeFound("depreciationRegime.equals=" + DEFAULT_DEPRECIATION_REGIME);

        // Get all the fixedAssetNetBookValueList where depreciationRegime equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetNetBookValueShouldNotBeFound("depreciationRegime.equals=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByDepreciationRegimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where depreciationRegime not equals to DEFAULT_DEPRECIATION_REGIME
        defaultFixedAssetNetBookValueShouldNotBeFound("depreciationRegime.notEquals=" + DEFAULT_DEPRECIATION_REGIME);

        // Get all the fixedAssetNetBookValueList where depreciationRegime not equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetNetBookValueShouldBeFound("depreciationRegime.notEquals=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByDepreciationRegimeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where depreciationRegime in DEFAULT_DEPRECIATION_REGIME or UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetNetBookValueShouldBeFound("depreciationRegime.in=" + DEFAULT_DEPRECIATION_REGIME + "," + UPDATED_DEPRECIATION_REGIME);

        // Get all the fixedAssetNetBookValueList where depreciationRegime equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetNetBookValueShouldNotBeFound("depreciationRegime.in=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByDepreciationRegimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where depreciationRegime is not null
        defaultFixedAssetNetBookValueShouldBeFound("depreciationRegime.specified=true");

        // Get all the fixedAssetNetBookValueList where depreciationRegime is null
        defaultFixedAssetNetBookValueShouldNotBeFound("depreciationRegime.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetNetBookValueList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetNetBookValueList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetNetBookValueList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where fileUploadToken is not null
        defaultFixedAssetNetBookValueShouldBeFound("fileUploadToken.specified=true");

        // Get all the fixedAssetNetBookValueList where fileUploadToken is null
        defaultFixedAssetNetBookValueShouldNotBeFound("fileUploadToken.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetNetBookValueList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetNetBookValueList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }


    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetNetBookValueList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetNetBookValueList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the fixedAssetNetBookValueList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where compilationToken is not null
        defaultFixedAssetNetBookValueShouldBeFound("compilationToken.specified=true");

        // Get all the fixedAssetNetBookValueList where compilationToken is null
        defaultFixedAssetNetBookValueShouldNotBeFound("compilationToken.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetNetBookValueList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetNetBookValuesByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        // Get all the fixedAssetNetBookValueList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetNetBookValueList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultFixedAssetNetBookValueShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFixedAssetNetBookValueShouldBeFound(String filter) throws Exception {
        restFixedAssetNetBookValueMockMvc.perform(get("/api/fixed-asset-net-book-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetNetBookValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].netBookValueDate").value(hasItem(DEFAULT_NET_BOOK_VALUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(DEFAULT_NET_BOOK_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restFixedAssetNetBookValueMockMvc.perform(get("/api/fixed-asset-net-book-values/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFixedAssetNetBookValueShouldNotBeFound(String filter) throws Exception {
        restFixedAssetNetBookValueMockMvc.perform(get("/api/fixed-asset-net-book-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFixedAssetNetBookValueMockMvc.perform(get("/api/fixed-asset-net-book-values/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFixedAssetNetBookValue() throws Exception {
        // Get the fixedAssetNetBookValue
        restFixedAssetNetBookValueMockMvc.perform(get("/api/fixed-asset-net-book-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFixedAssetNetBookValue() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        int databaseSizeBeforeUpdate = fixedAssetNetBookValueRepository.findAll().size();

        // Update the fixedAssetNetBookValue
        FixedAssetNetBookValue updatedFixedAssetNetBookValue = fixedAssetNetBookValueRepository.findById(fixedAssetNetBookValue.getId()).get();
        // Disconnect from session so that the updates on updatedFixedAssetNetBookValue are not directly saved in db
        em.detach(updatedFixedAssetNetBookValue);
        updatedFixedAssetNetBookValue.setAssetNumber(UPDATED_ASSET_NUMBER);
        updatedFixedAssetNetBookValue.setServiceOutletCode(UPDATED_SERVICE_OUTLET_CODE);
        updatedFixedAssetNetBookValue.setAssetTag(UPDATED_ASSET_TAG);
        updatedFixedAssetNetBookValue.setAssetDescription(UPDATED_ASSET_DESCRIPTION);
        updatedFixedAssetNetBookValue.setNetBookValueDate(UPDATED_NET_BOOK_VALUE_DATE);
        updatedFixedAssetNetBookValue.setAssetCategory(UPDATED_ASSET_CATEGORY);
        updatedFixedAssetNetBookValue.setNetBookValue(UPDATED_NET_BOOK_VALUE);
        updatedFixedAssetNetBookValue.setDepreciationRegime(UPDATED_DEPRECIATION_REGIME);
        updatedFixedAssetNetBookValue.setFileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);
        updatedFixedAssetNetBookValue.setCompilationToken(UPDATED_COMPILATION_TOKEN);
        FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO = fixedAssetNetBookValueMapper.toDto(updatedFixedAssetNetBookValue);

        restFixedAssetNetBookValueMockMvc.perform(put("/api/fixed-asset-net-book-values").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetNetBookValueDTO)))
            .andExpect(status().isOk());

        // Validate the FixedAssetNetBookValue in the database
        List<FixedAssetNetBookValue> fixedAssetNetBookValueList = fixedAssetNetBookValueRepository.findAll();
        assertThat(fixedAssetNetBookValueList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetNetBookValue testFixedAssetNetBookValue = fixedAssetNetBookValueList.get(fixedAssetNetBookValueList.size() - 1);
        assertThat(testFixedAssetNetBookValue.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testFixedAssetNetBookValue.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetNetBookValue.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testFixedAssetNetBookValue.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testFixedAssetNetBookValue.getNetBookValueDate()).isEqualTo(UPDATED_NET_BOOK_VALUE_DATE);
        assertThat(testFixedAssetNetBookValue.getAssetCategory()).isEqualTo(UPDATED_ASSET_CATEGORY);
        assertThat(testFixedAssetNetBookValue.getNetBookValue()).isEqualTo(UPDATED_NET_BOOK_VALUE);
        assertThat(testFixedAssetNetBookValue.getDepreciationRegime()).isEqualTo(UPDATED_DEPRECIATION_REGIME);
        assertThat(testFixedAssetNetBookValue.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetNetBookValue.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the FixedAssetNetBookValue in Elasticsearch
        verify(mockFixedAssetNetBookValueSearchRepository, times(1)).save(testFixedAssetNetBookValue);
    }

    @Test
    @Transactional
    public void updateNonExistingFixedAssetNetBookValue() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetNetBookValueRepository.findAll().size();

        // Create the FixedAssetNetBookValue
        FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO = fixedAssetNetBookValueMapper.toDto(fixedAssetNetBookValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedAssetNetBookValueMockMvc.perform(put("/api/fixed-asset-net-book-values").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetNetBookValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetNetBookValue in the database
        List<FixedAssetNetBookValue> fixedAssetNetBookValueList = fixedAssetNetBookValueRepository.findAll();
        assertThat(fixedAssetNetBookValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetNetBookValue in Elasticsearch
        verify(mockFixedAssetNetBookValueSearchRepository, times(0)).save(fixedAssetNetBookValue);
    }

    @Test
    @Transactional
    public void deleteFixedAssetNetBookValue() throws Exception {
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);

        int databaseSizeBeforeDelete = fixedAssetNetBookValueRepository.findAll().size();

        // Delete the fixedAssetNetBookValue
        restFixedAssetNetBookValueMockMvc.perform(delete("/api/fixed-asset-net-book-values/{id}", fixedAssetNetBookValue.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FixedAssetNetBookValue> fixedAssetNetBookValueList = fixedAssetNetBookValueRepository.findAll();
        assertThat(fixedAssetNetBookValueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FixedAssetNetBookValue in Elasticsearch
        verify(mockFixedAssetNetBookValueSearchRepository, times(1)).deleteById(fixedAssetNetBookValue.getId());
    }

    @Test
    @Transactional
    public void searchFixedAssetNetBookValue() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fixedAssetNetBookValueRepository.saveAndFlush(fixedAssetNetBookValue);
        when(mockFixedAssetNetBookValueSearchRepository.search(queryStringQuery("id:" + fixedAssetNetBookValue.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fixedAssetNetBookValue), PageRequest.of(0, 1), 1));

        // Search the fixedAssetNetBookValue
        restFixedAssetNetBookValueMockMvc.perform(get("/api/_search/fixed-asset-net-book-values?query=id:" + fixedAssetNetBookValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetNetBookValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].netBookValueDate").value(hasItem(DEFAULT_NET_BOOK_VALUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(DEFAULT_NET_BOOK_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
