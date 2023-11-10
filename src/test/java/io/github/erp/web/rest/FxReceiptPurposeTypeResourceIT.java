package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.FxReceiptPurposeType;
import io.github.erp.repository.FxReceiptPurposeTypeRepository;
import io.github.erp.repository.search.FxReceiptPurposeTypeSearchRepository;
import io.github.erp.service.criteria.FxReceiptPurposeTypeCriteria;
import io.github.erp.service.dto.FxReceiptPurposeTypeDTO;
import io.github.erp.service.mapper.FxReceiptPurposeTypeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

/**
 * Integration tests for the {@link FxReceiptPurposeTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FxReceiptPurposeTypeResourceIT {

    private static final String DEFAULT_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_CHILD = "AAAAAAAAAA";
    private static final String UPDATED_LAST_CHILD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fx-receipt-purpose-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fx-receipt-purpose-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FxReceiptPurposeTypeRepository fxReceiptPurposeTypeRepository;

    @Autowired
    private FxReceiptPurposeTypeMapper fxReceiptPurposeTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FxReceiptPurposeTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FxReceiptPurposeTypeSearchRepository mockFxReceiptPurposeTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFxReceiptPurposeTypeMockMvc;

    private FxReceiptPurposeType fxReceiptPurposeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxReceiptPurposeType createEntity(EntityManager em) {
        FxReceiptPurposeType fxReceiptPurposeType = new FxReceiptPurposeType()
            .itemCode(DEFAULT_ITEM_CODE)
            .attribute1ReceiptPaymentPurposeCode(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute1ReceiptPaymentPurposeType(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE)
            .attribute2ReceiptPaymentPurposeCode(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute2ReceiptPaymentPurposeDescription(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute3ReceiptPaymentPurposeCode(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute3ReceiptPaymentPurposeDescription(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute4ReceiptPaymentPurposeCode(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute4ReceiptPaymentPurposeDescription(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute5ReceiptPaymentPurposeCode(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute5ReceiptPaymentPurposeDescription(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .lastChild(DEFAULT_LAST_CHILD);
        return fxReceiptPurposeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxReceiptPurposeType createUpdatedEntity(EntityManager em) {
        FxReceiptPurposeType fxReceiptPurposeType = new FxReceiptPurposeType()
            .itemCode(UPDATED_ITEM_CODE)
            .attribute1ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute1ReceiptPaymentPurposeType(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE)
            .attribute2ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute2ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute3ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute3ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute4ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute4ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute5ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute5ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .lastChild(UPDATED_LAST_CHILD);
        return fxReceiptPurposeType;
    }

    @BeforeEach
    public void initTest() {
        fxReceiptPurposeType = createEntity(em);
    }

    @Test
    @Transactional
    void createFxReceiptPurposeType() throws Exception {
        int databaseSizeBeforeCreate = fxReceiptPurposeTypeRepository.findAll().size();
        // Create the FxReceiptPurposeType
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);
        restFxReceiptPurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FxReceiptPurposeType testFxReceiptPurposeType = fxReceiptPurposeTypeList.get(fxReceiptPurposeTypeList.size() - 1);
        assertThat(testFxReceiptPurposeType.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeCode())
            .isEqualTo(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeType())
            .isEqualTo(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeCode())
            .isEqualTo(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeDescription())
            .isEqualTo(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeCode())
            .isEqualTo(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeDescription())
            .isEqualTo(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeCode())
            .isEqualTo(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeDescription())
            .isEqualTo(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeCode())
            .isEqualTo(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeDescription())
            .isEqualTo(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getLastChild()).isEqualTo(DEFAULT_LAST_CHILD);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(1)).save(testFxReceiptPurposeType);
    }

    @Test
    @Transactional
    void createFxReceiptPurposeTypeWithExistingId() throws Exception {
        // Create the FxReceiptPurposeType with an existing ID
        fxReceiptPurposeType.setId(1L);
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        int databaseSizeBeforeCreate = fxReceiptPurposeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxReceiptPurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(0)).save(fxReceiptPurposeType);
    }

    @Test
    @Transactional
    void checkItemCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxReceiptPurposeTypeRepository.findAll().size();
        // set the field null
        fxReceiptPurposeType.setItemCode(null);

        // Create the FxReceiptPurposeType, which fails.
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        restFxReceiptPurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypes() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList
        restFxReceiptPurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxReceiptPurposeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(
                jsonPath("$.[*].attribute1ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute1ReceiptPaymentPurposeType").value(hasItem(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE))
            )
            .andExpect(
                jsonPath("$.[*].attribute2ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute2ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute3ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute3ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute4ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute4ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute5ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute5ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(jsonPath("$.[*].lastChild").value(hasItem(DEFAULT_LAST_CHILD)));
    }

    @Test
    @Transactional
    void getFxReceiptPurposeType() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get the fxReceiptPurposeType
        restFxReceiptPurposeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fxReceiptPurposeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fxReceiptPurposeType.getId().intValue()))
            .andExpect(jsonPath("$.itemCode").value(DEFAULT_ITEM_CODE))
            .andExpect(jsonPath("$.attribute1ReceiptPaymentPurposeCode").value(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE))
            .andExpect(jsonPath("$.attribute1ReceiptPaymentPurposeType").value(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE))
            .andExpect(jsonPath("$.attribute2ReceiptPaymentPurposeCode").value(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE))
            .andExpect(
                jsonPath("$.attribute2ReceiptPaymentPurposeDescription").value(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            )
            .andExpect(jsonPath("$.attribute3ReceiptPaymentPurposeCode").value(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE))
            .andExpect(
                jsonPath("$.attribute3ReceiptPaymentPurposeDescription").value(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            )
            .andExpect(jsonPath("$.attribute4ReceiptPaymentPurposeCode").value(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE))
            .andExpect(
                jsonPath("$.attribute4ReceiptPaymentPurposeDescription").value(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            )
            .andExpect(jsonPath("$.attribute5ReceiptPaymentPurposeCode").value(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE))
            .andExpect(
                jsonPath("$.attribute5ReceiptPaymentPurposeDescription").value(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            )
            .andExpect(jsonPath("$.lastChild").value(DEFAULT_LAST_CHILD));
    }

    @Test
    @Transactional
    void getFxReceiptPurposeTypesByIdFiltering() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        Long id = fxReceiptPurposeType.getId();

        defaultFxReceiptPurposeTypeShouldBeFound("id.equals=" + id);
        defaultFxReceiptPurposeTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFxReceiptPurposeTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFxReceiptPurposeTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFxReceiptPurposeTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFxReceiptPurposeTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByItemCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where itemCode equals to DEFAULT_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldBeFound("itemCode.equals=" + DEFAULT_ITEM_CODE);

        // Get all the fxReceiptPurposeTypeList where itemCode equals to UPDATED_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound("itemCode.equals=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByItemCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where itemCode not equals to DEFAULT_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound("itemCode.notEquals=" + DEFAULT_ITEM_CODE);

        // Get all the fxReceiptPurposeTypeList where itemCode not equals to UPDATED_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldBeFound("itemCode.notEquals=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByItemCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where itemCode in DEFAULT_ITEM_CODE or UPDATED_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldBeFound("itemCode.in=" + DEFAULT_ITEM_CODE + "," + UPDATED_ITEM_CODE);

        // Get all the fxReceiptPurposeTypeList where itemCode equals to UPDATED_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound("itemCode.in=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByItemCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where itemCode is not null
        defaultFxReceiptPurposeTypeShouldBeFound("itemCode.specified=true");

        // Get all the fxReceiptPurposeTypeList where itemCode is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("itemCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByItemCodeContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where itemCode contains DEFAULT_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldBeFound("itemCode.contains=" + DEFAULT_ITEM_CODE);

        // Get all the fxReceiptPurposeTypeList where itemCode contains UPDATED_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound("itemCode.contains=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByItemCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where itemCode does not contain DEFAULT_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound("itemCode.doesNotContain=" + DEFAULT_ITEM_CODE);

        // Get all the fxReceiptPurposeTypeList where itemCode does not contain UPDATED_ITEM_CODE
        defaultFxReceiptPurposeTypeShouldBeFound("itemCode.doesNotContain=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode equals to DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeCode.equals=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeCode.equals=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode not equals to DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeCode.notEquals=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode not equals to UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeCode.notEquals=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode in DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE or UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeCode.in=" +
            DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE +
            "," +
            UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeCode.in=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute1ReceiptPaymentPurposeCode.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute1ReceiptPaymentPurposeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode contains DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeCode.contains=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode contains UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeCode.contains=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode does not contain DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeCode.doesNotContain=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeCode does not contain UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeCode.doesNotContain=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType equals to DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeType.equals=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType equals to UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeType.equals=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType not equals to DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeType.notEquals=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType not equals to UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeType.notEquals=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType in DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE or UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeType.in=" +
            DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE +
            "," +
            UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType equals to UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeType.in=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute1ReceiptPaymentPurposeType.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute1ReceiptPaymentPurposeType.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeTypeContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType contains DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeType.contains=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType contains UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeType.contains=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute1ReceiptPaymentPurposeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType does not contain DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute1ReceiptPaymentPurposeType.doesNotContain=" + DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );

        // Get all the fxReceiptPurposeTypeList where attribute1ReceiptPaymentPurposeType does not contain UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute1ReceiptPaymentPurposeType.doesNotContain=" + UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode equals to DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeCode.equals=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeCode.equals=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode not equals to DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeCode.notEquals=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode not equals to UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeCode.notEquals=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode in DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE or UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeCode.in=" +
            DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE +
            "," +
            UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeCode.in=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute2ReceiptPaymentPurposeCode.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute2ReceiptPaymentPurposeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode contains DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeCode.contains=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode contains UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeCode.contains=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode does not contain DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeCode.doesNotContain=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeCode does not contain UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeCode.doesNotContain=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription equals to DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeDescription.equals=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeDescription.equals=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription not equals to DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeDescription.notEquals=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription not equals to UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeDescription.notEquals=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription in DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION or UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeDescription.in=" +
            DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION +
            "," +
            UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeDescription.in=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute2ReceiptPaymentPurposeDescription.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute2ReceiptPaymentPurposeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription contains DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeDescription.contains=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription contains UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeDescription.contains=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute2ReceiptPaymentPurposeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription does not contain DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute2ReceiptPaymentPurposeDescription.doesNotContain=" + DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute2ReceiptPaymentPurposeDescription does not contain UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute2ReceiptPaymentPurposeDescription.doesNotContain=" + UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode equals to DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeCode.equals=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeCode.equals=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode not equals to DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeCode.notEquals=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode not equals to UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeCode.notEquals=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode in DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE or UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeCode.in=" +
            DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE +
            "," +
            UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeCode.in=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute3ReceiptPaymentPurposeCode.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute3ReceiptPaymentPurposeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode contains DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeCode.contains=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode contains UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeCode.contains=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode does not contain DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeCode.doesNotContain=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeCode does not contain UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeCode.doesNotContain=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription equals to DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeDescription.equals=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeDescription.equals=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription not equals to DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeDescription.notEquals=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription not equals to UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeDescription.notEquals=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription in DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION or UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeDescription.in=" +
            DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION +
            "," +
            UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeDescription.in=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute3ReceiptPaymentPurposeDescription.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute3ReceiptPaymentPurposeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription contains DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeDescription.contains=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription contains UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeDescription.contains=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute3ReceiptPaymentPurposeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription does not contain DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute3ReceiptPaymentPurposeDescription.doesNotContain=" + DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute3ReceiptPaymentPurposeDescription does not contain UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute3ReceiptPaymentPurposeDescription.doesNotContain=" + UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode equals to DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeCode.equals=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeCode.equals=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode not equals to DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeCode.notEquals=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode not equals to UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeCode.notEquals=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode in DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE or UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeCode.in=" +
            DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE +
            "," +
            UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeCode.in=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute4ReceiptPaymentPurposeCode.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute4ReceiptPaymentPurposeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode contains DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeCode.contains=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode contains UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeCode.contains=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode does not contain DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeCode.doesNotContain=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeCode does not contain UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeCode.doesNotContain=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription equals to DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeDescription.equals=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeDescription.equals=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription not equals to DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeDescription.notEquals=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription not equals to UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeDescription.notEquals=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription in DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION or UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeDescription.in=" +
            DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION +
            "," +
            UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeDescription.in=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute4ReceiptPaymentPurposeDescription.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute4ReceiptPaymentPurposeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription contains DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeDescription.contains=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription contains UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeDescription.contains=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute4ReceiptPaymentPurposeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription does not contain DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute4ReceiptPaymentPurposeDescription.doesNotContain=" + DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute4ReceiptPaymentPurposeDescription does not contain UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute4ReceiptPaymentPurposeDescription.doesNotContain=" + UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode equals to DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeCode.equals=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeCode.equals=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode not equals to DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeCode.notEquals=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode not equals to UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeCode.notEquals=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode in DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE or UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeCode.in=" +
            DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE +
            "," +
            UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode equals to UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeCode.in=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute5ReceiptPaymentPurposeCode.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute5ReceiptPaymentPurposeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode contains DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeCode.contains=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode contains UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeCode.contains=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode does not contain DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeCode.doesNotContain=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeCode does not contain UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeCode.doesNotContain=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription equals to DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeDescription.equals=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeDescription.equals=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription not equals to DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeDescription.notEquals=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription not equals to UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeDescription.notEquals=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription in DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION or UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeDescription.in=" +
            DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION +
            "," +
            UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription equals to UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeDescription.in=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription is not null
        defaultFxReceiptPurposeTypeShouldBeFound("attribute5ReceiptPaymentPurposeDescription.specified=true");

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("attribute5ReceiptPaymentPurposeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription contains DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeDescription.contains=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription contains UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeDescription.contains=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByAttribute5ReceiptPaymentPurposeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription does not contain DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldNotBeFound(
            "attribute5ReceiptPaymentPurposeDescription.doesNotContain=" + DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );

        // Get all the fxReceiptPurposeTypeList where attribute5ReceiptPaymentPurposeDescription does not contain UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        defaultFxReceiptPurposeTypeShouldBeFound(
            "attribute5ReceiptPaymentPurposeDescription.doesNotContain=" + UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByLastChildIsEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where lastChild equals to DEFAULT_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldBeFound("lastChild.equals=" + DEFAULT_LAST_CHILD);

        // Get all the fxReceiptPurposeTypeList where lastChild equals to UPDATED_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldNotBeFound("lastChild.equals=" + UPDATED_LAST_CHILD);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByLastChildIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where lastChild not equals to DEFAULT_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldNotBeFound("lastChild.notEquals=" + DEFAULT_LAST_CHILD);

        // Get all the fxReceiptPurposeTypeList where lastChild not equals to UPDATED_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldBeFound("lastChild.notEquals=" + UPDATED_LAST_CHILD);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByLastChildIsInShouldWork() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where lastChild in DEFAULT_LAST_CHILD or UPDATED_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldBeFound("lastChild.in=" + DEFAULT_LAST_CHILD + "," + UPDATED_LAST_CHILD);

        // Get all the fxReceiptPurposeTypeList where lastChild equals to UPDATED_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldNotBeFound("lastChild.in=" + UPDATED_LAST_CHILD);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByLastChildIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where lastChild is not null
        defaultFxReceiptPurposeTypeShouldBeFound("lastChild.specified=true");

        // Get all the fxReceiptPurposeTypeList where lastChild is null
        defaultFxReceiptPurposeTypeShouldNotBeFound("lastChild.specified=false");
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByLastChildContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where lastChild contains DEFAULT_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldBeFound("lastChild.contains=" + DEFAULT_LAST_CHILD);

        // Get all the fxReceiptPurposeTypeList where lastChild contains UPDATED_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldNotBeFound("lastChild.contains=" + UPDATED_LAST_CHILD);
    }

    @Test
    @Transactional
    void getAllFxReceiptPurposeTypesByLastChildNotContainsSomething() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        // Get all the fxReceiptPurposeTypeList where lastChild does not contain DEFAULT_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldNotBeFound("lastChild.doesNotContain=" + DEFAULT_LAST_CHILD);

        // Get all the fxReceiptPurposeTypeList where lastChild does not contain UPDATED_LAST_CHILD
        defaultFxReceiptPurposeTypeShouldBeFound("lastChild.doesNotContain=" + UPDATED_LAST_CHILD);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFxReceiptPurposeTypeShouldBeFound(String filter) throws Exception {
        restFxReceiptPurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxReceiptPurposeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(
                jsonPath("$.[*].attribute1ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute1ReceiptPaymentPurposeType").value(hasItem(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE))
            )
            .andExpect(
                jsonPath("$.[*].attribute2ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute2ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute3ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute3ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute4ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute4ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute5ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute5ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(jsonPath("$.[*].lastChild").value(hasItem(DEFAULT_LAST_CHILD)));

        // Check, that the count call also returns 1
        restFxReceiptPurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFxReceiptPurposeTypeShouldNotBeFound(String filter) throws Exception {
        restFxReceiptPurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFxReceiptPurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFxReceiptPurposeType() throws Exception {
        // Get the fxReceiptPurposeType
        restFxReceiptPurposeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFxReceiptPurposeType() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();

        // Update the fxReceiptPurposeType
        FxReceiptPurposeType updatedFxReceiptPurposeType = fxReceiptPurposeTypeRepository.findById(fxReceiptPurposeType.getId()).get();
        // Disconnect from session so that the updates on updatedFxReceiptPurposeType are not directly saved in db
        em.detach(updatedFxReceiptPurposeType);
        updatedFxReceiptPurposeType
            .itemCode(UPDATED_ITEM_CODE)
            .attribute1ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute1ReceiptPaymentPurposeType(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE)
            .attribute2ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute2ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute3ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute3ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute4ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute4ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute5ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute5ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .lastChild(UPDATED_LAST_CHILD);
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(updatedFxReceiptPurposeType);

        restFxReceiptPurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxReceiptPurposeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);
        FxReceiptPurposeType testFxReceiptPurposeType = fxReceiptPurposeTypeList.get(fxReceiptPurposeTypeList.size() - 1);
        assertThat(testFxReceiptPurposeType.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeType())
            .isEqualTo(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getLastChild()).isEqualTo(UPDATED_LAST_CHILD);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository).save(testFxReceiptPurposeType);
    }

    @Test
    @Transactional
    void putNonExistingFxReceiptPurposeType() throws Exception {
        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();
        fxReceiptPurposeType.setId(count.incrementAndGet());

        // Create the FxReceiptPurposeType
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxReceiptPurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxReceiptPurposeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(0)).save(fxReceiptPurposeType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFxReceiptPurposeType() throws Exception {
        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();
        fxReceiptPurposeType.setId(count.incrementAndGet());

        // Create the FxReceiptPurposeType
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxReceiptPurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(0)).save(fxReceiptPurposeType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFxReceiptPurposeType() throws Exception {
        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();
        fxReceiptPurposeType.setId(count.incrementAndGet());

        // Create the FxReceiptPurposeType
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxReceiptPurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(0)).save(fxReceiptPurposeType);
    }

    @Test
    @Transactional
    void partialUpdateFxReceiptPurposeTypeWithPatch() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();

        // Update the fxReceiptPurposeType using partial update
        FxReceiptPurposeType partialUpdatedFxReceiptPurposeType = new FxReceiptPurposeType();
        partialUpdatedFxReceiptPurposeType.setId(fxReceiptPurposeType.getId());

        partialUpdatedFxReceiptPurposeType
            .attribute1ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute1ReceiptPaymentPurposeType(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE)
            .attribute2ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute3ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute5ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute5ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .lastChild(UPDATED_LAST_CHILD);

        restFxReceiptPurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxReceiptPurposeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxReceiptPurposeType))
            )
            .andExpect(status().isOk());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);
        FxReceiptPurposeType testFxReceiptPurposeType = fxReceiptPurposeTypeList.get(fxReceiptPurposeTypeList.size() - 1);
        assertThat(testFxReceiptPurposeType.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeType())
            .isEqualTo(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeDescription())
            .isEqualTo(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeCode())
            .isEqualTo(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeCode())
            .isEqualTo(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeDescription())
            .isEqualTo(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getLastChild()).isEqualTo(UPDATED_LAST_CHILD);
    }

    @Test
    @Transactional
    void fullUpdateFxReceiptPurposeTypeWithPatch() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();

        // Update the fxReceiptPurposeType using partial update
        FxReceiptPurposeType partialUpdatedFxReceiptPurposeType = new FxReceiptPurposeType();
        partialUpdatedFxReceiptPurposeType.setId(fxReceiptPurposeType.getId());

        partialUpdatedFxReceiptPurposeType
            .itemCode(UPDATED_ITEM_CODE)
            .attribute1ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute1ReceiptPaymentPurposeType(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE)
            .attribute2ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute2ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute3ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute3ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute4ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute4ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .attribute5ReceiptPaymentPurposeCode(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE)
            .attribute5ReceiptPaymentPurposeDescription(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION)
            .lastChild(UPDATED_LAST_CHILD);

        restFxReceiptPurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxReceiptPurposeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxReceiptPurposeType))
            )
            .andExpect(status().isOk());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);
        FxReceiptPurposeType testFxReceiptPurposeType = fxReceiptPurposeTypeList.get(fxReceiptPurposeTypeList.size() - 1);
        assertThat(testFxReceiptPurposeType.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute1ReceiptPaymentPurposeType())
            .isEqualTo(UPDATED_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute2ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute3ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute4ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeCode())
            .isEqualTo(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE);
        assertThat(testFxReceiptPurposeType.getAttribute5ReceiptPaymentPurposeDescription())
            .isEqualTo(UPDATED_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION);
        assertThat(testFxReceiptPurposeType.getLastChild()).isEqualTo(UPDATED_LAST_CHILD);
    }

    @Test
    @Transactional
    void patchNonExistingFxReceiptPurposeType() throws Exception {
        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();
        fxReceiptPurposeType.setId(count.incrementAndGet());

        // Create the FxReceiptPurposeType
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxReceiptPurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fxReceiptPurposeTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(0)).save(fxReceiptPurposeType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFxReceiptPurposeType() throws Exception {
        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();
        fxReceiptPurposeType.setId(count.incrementAndGet());

        // Create the FxReceiptPurposeType
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxReceiptPurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(0)).save(fxReceiptPurposeType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFxReceiptPurposeType() throws Exception {
        int databaseSizeBeforeUpdate = fxReceiptPurposeTypeRepository.findAll().size();
        fxReceiptPurposeType.setId(count.incrementAndGet());

        // Create the FxReceiptPurposeType
        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxReceiptPurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxReceiptPurposeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxReceiptPurposeType in the database
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(0)).save(fxReceiptPurposeType);
    }

    @Test
    @Transactional
    void deleteFxReceiptPurposeType() throws Exception {
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);

        int databaseSizeBeforeDelete = fxReceiptPurposeTypeRepository.findAll().size();

        // Delete the fxReceiptPurposeType
        restFxReceiptPurposeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fxReceiptPurposeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FxReceiptPurposeType> fxReceiptPurposeTypeList = fxReceiptPurposeTypeRepository.findAll();
        assertThat(fxReceiptPurposeTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FxReceiptPurposeType in Elasticsearch
        verify(mockFxReceiptPurposeTypeSearchRepository, times(1)).deleteById(fxReceiptPurposeType.getId());
    }

    @Test
    @Transactional
    void searchFxReceiptPurposeType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fxReceiptPurposeTypeRepository.saveAndFlush(fxReceiptPurposeType);
        when(mockFxReceiptPurposeTypeSearchRepository.search("id:" + fxReceiptPurposeType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fxReceiptPurposeType), PageRequest.of(0, 1), 1));

        // Search the fxReceiptPurposeType
        restFxReceiptPurposeTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fxReceiptPurposeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxReceiptPurposeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(
                jsonPath("$.[*].attribute1ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute1ReceiptPaymentPurposeType").value(hasItem(DEFAULT_ATTRIBUTE_1_RECEIPT_PAYMENT_PURPOSE_TYPE))
            )
            .andExpect(
                jsonPath("$.[*].attribute2ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute2ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_2_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute3ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute3ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_3_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute4ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute4ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_4_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(
                jsonPath("$.[*].attribute5ReceiptPaymentPurposeCode").value(hasItem(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_CODE))
            )
            .andExpect(
                jsonPath("$.[*].attribute5ReceiptPaymentPurposeDescription")
                    .value(hasItem(DEFAULT_ATTRIBUTE_5_RECEIPT_PAYMENT_PURPOSE_DESCRIPTION))
            )
            .andExpect(jsonPath("$.[*].lastChild").value(hasItem(DEFAULT_LAST_CHILD)));
    }
}
