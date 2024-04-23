package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AssetAdditionsReportItem;
import io.github.erp.repository.AssetAdditionsReportItemRepository;
import io.github.erp.repository.search.AssetAdditionsReportItemSearchRepository;
import io.github.erp.service.criteria.AssetAdditionsReportItemCriteria;
import io.github.erp.service.dto.AssetAdditionsReportItemDTO;
import io.github.erp.service.mapper.AssetAdditionsReportItemMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AssetAdditionsReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetAdditionsReportItemResourceIT {

    private static final String DEFAULT_ASSET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CAPITALIZATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CAPITALIZATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CAPITALIZATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DETAILS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ASSET_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASSET_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ASSET_COST = new BigDecimal(1 - 1);

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_HISTORICAL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_HISTORICAL_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_HISTORICAL_COST = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/asset-additions-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/asset-additions-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetAdditionsReportItemRepository assetAdditionsReportItemRepository;

    @Autowired
    private AssetAdditionsReportItemMapper assetAdditionsReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetAdditionsReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetAdditionsReportItemSearchRepository mockAssetAdditionsReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetAdditionsReportItemMockMvc;

    private AssetAdditionsReportItem assetAdditionsReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAdditionsReportItem createEntity(EntityManager em) {
        AssetAdditionsReportItem assetAdditionsReportItem = new AssetAdditionsReportItem()
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .assetTag(DEFAULT_ASSET_TAG)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .capitalizationDate(DEFAULT_CAPITALIZATION_DATE)
            .assetCategory(DEFAULT_ASSET_CATEGORY)
            .assetDetails(DEFAULT_ASSET_DETAILS)
            .assetCost(DEFAULT_ASSET_COST)
            .supplier(DEFAULT_SUPPLIER)
            .historicalCost(DEFAULT_HISTORICAL_COST)
            .registrationDate(DEFAULT_REGISTRATION_DATE);
        return assetAdditionsReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAdditionsReportItem createUpdatedEntity(EntityManager em) {
        AssetAdditionsReportItem assetAdditionsReportItem = new AssetAdditionsReportItem()
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .transactionId(UPDATED_TRANSACTION_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .assetDetails(UPDATED_ASSET_DETAILS)
            .assetCost(UPDATED_ASSET_COST)
            .supplier(UPDATED_SUPPLIER)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .registrationDate(UPDATED_REGISTRATION_DATE);
        return assetAdditionsReportItem;
    }

    @BeforeEach
    public void initTest() {
        assetAdditionsReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItems() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList
        restAssetAdditionsReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAdditionsReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getAssetAdditionsReportItem() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get the assetAdditionsReportItem
        restAssetAdditionsReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, assetAdditionsReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetAdditionsReportItem.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.capitalizationDate").value(DEFAULT_CAPITALIZATION_DATE.toString()))
            .andExpect(jsonPath("$.assetCategory").value(DEFAULT_ASSET_CATEGORY))
            .andExpect(jsonPath("$.assetDetails").value(DEFAULT_ASSET_DETAILS))
            .andExpect(jsonPath("$.assetCost").value(sameNumber(DEFAULT_ASSET_COST)))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER))
            .andExpect(jsonPath("$.historicalCost").value(sameNumber(DEFAULT_HISTORICAL_COST)))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getAssetAdditionsReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        Long id = assetAdditionsReportItem.getId();

        defaultAssetAdditionsReportItemShouldBeFound("id.equals=" + id);
        defaultAssetAdditionsReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultAssetAdditionsReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetAdditionsReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetAdditionsReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetAdditionsReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetAdditionsReportItemList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetAdditionsReportItemList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the assetAdditionsReportItemList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetNumber is not null
        defaultAssetAdditionsReportItemShouldBeFound("assetNumber.specified=true");

        // Get all the assetAdditionsReportItemList where assetNumber is null
        defaultAssetAdditionsReportItemShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetNumberContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetNumber contains DEFAULT_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldBeFound("assetNumber.contains=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetAdditionsReportItemList where assetNumber contains UPDATED_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldNotBeFound("assetNumber.contains=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetNumberNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetNumber does not contain DEFAULT_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldNotBeFound("assetNumber.doesNotContain=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetAdditionsReportItemList where assetNumber does not contain UPDATED_ASSET_NUMBER
        defaultAssetAdditionsReportItemShouldBeFound("assetNumber.doesNotContain=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetTag equals to DEFAULT_ASSET_TAG
        defaultAssetAdditionsReportItemShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the assetAdditionsReportItemList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetAdditionsReportItemShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultAssetAdditionsReportItemShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the assetAdditionsReportItemList where assetTag not equals to UPDATED_ASSET_TAG
        defaultAssetAdditionsReportItemShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultAssetAdditionsReportItemShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the assetAdditionsReportItemList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetAdditionsReportItemShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetTag is not null
        defaultAssetAdditionsReportItemShouldBeFound("assetTag.specified=true");

        // Get all the assetAdditionsReportItemList where assetTag is null
        defaultAssetAdditionsReportItemShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetTag contains DEFAULT_ASSET_TAG
        defaultAssetAdditionsReportItemShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the assetAdditionsReportItemList where assetTag contains UPDATED_ASSET_TAG
        defaultAssetAdditionsReportItemShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultAssetAdditionsReportItemShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the assetAdditionsReportItemList where assetTag does not contain UPDATED_ASSET_TAG
        defaultAssetAdditionsReportItemShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAdditionsReportItemList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAdditionsReportItemList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldBeFound(
            "serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE
        );

        // Get all the assetAdditionsReportItemList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where serviceOutletCode is not null
        defaultAssetAdditionsReportItemShouldBeFound("serviceOutletCode.specified=true");

        // Get all the assetAdditionsReportItemList where serviceOutletCode is null
        defaultAssetAdditionsReportItemShouldNotBeFound("serviceOutletCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAdditionsReportItemList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAdditionsReportItemList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAdditionsReportItemShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionId equals to DEFAULT_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldBeFound("transactionId.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the assetAdditionsReportItemList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionId not equals to DEFAULT_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionId.notEquals=" + DEFAULT_TRANSACTION_ID);

        // Get all the assetAdditionsReportItemList where transactionId not equals to UPDATED_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldBeFound("transactionId.notEquals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionId in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldBeFound("transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the assetAdditionsReportItemList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionId.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionId is not null
        defaultAssetAdditionsReportItemShouldBeFound("transactionId.specified=true");

        // Get all the assetAdditionsReportItemList where transactionId is null
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionIdContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionId contains DEFAULT_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldBeFound("transactionId.contains=" + DEFAULT_TRANSACTION_ID);

        // Get all the assetAdditionsReportItemList where transactionId contains UPDATED_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionId.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionIdNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionId does not contain DEFAULT_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionId.doesNotContain=" + DEFAULT_TRANSACTION_ID);

        // Get all the assetAdditionsReportItemList where transactionId does not contain UPDATED_TRANSACTION_ID
        defaultAssetAdditionsReportItemShouldBeFound("transactionId.doesNotContain=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetAdditionsReportItemList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetAdditionsReportItemList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the assetAdditionsReportItemList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate is not null
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.specified=true");

        // Get all the assetAdditionsReportItemList where transactionDate is null
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetAdditionsReportItemList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetAdditionsReportItemList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetAdditionsReportItemList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetAdditionsReportItemList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate equals to DEFAULT_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("capitalizationDate.equals=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the assetAdditionsReportItemList where capitalizationDate equals to UPDATED_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.equals=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate not equals to DEFAULT_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.notEquals=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the assetAdditionsReportItemList where capitalizationDate not equals to UPDATED_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("capitalizationDate.notEquals=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate in DEFAULT_CAPITALIZATION_DATE or UPDATED_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound(
            "capitalizationDate.in=" + DEFAULT_CAPITALIZATION_DATE + "," + UPDATED_CAPITALIZATION_DATE
        );

        // Get all the assetAdditionsReportItemList where capitalizationDate equals to UPDATED_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.in=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate is not null
        defaultAssetAdditionsReportItemShouldBeFound("capitalizationDate.specified=true");

        // Get all the assetAdditionsReportItemList where capitalizationDate is null
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate is greater than or equal to DEFAULT_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("capitalizationDate.greaterThanOrEqual=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the assetAdditionsReportItemList where capitalizationDate is greater than or equal to UPDATED_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.greaterThanOrEqual=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate is less than or equal to DEFAULT_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("capitalizationDate.lessThanOrEqual=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the assetAdditionsReportItemList where capitalizationDate is less than or equal to SMALLER_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.lessThanOrEqual=" + SMALLER_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate is less than DEFAULT_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.lessThan=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the assetAdditionsReportItemList where capitalizationDate is less than UPDATED_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("capitalizationDate.lessThan=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByCapitalizationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where capitalizationDate is greater than DEFAULT_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("capitalizationDate.greaterThan=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the assetAdditionsReportItemList where capitalizationDate is greater than SMALLER_CAPITALIZATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("capitalizationDate.greaterThan=" + SMALLER_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCategory equals to DEFAULT_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldBeFound("assetCategory.equals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the assetAdditionsReportItemList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCategory.equals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCategory not equals to DEFAULT_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCategory.notEquals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the assetAdditionsReportItemList where assetCategory not equals to UPDATED_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldBeFound("assetCategory.notEquals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCategory in DEFAULT_ASSET_CATEGORY or UPDATED_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldBeFound("assetCategory.in=" + DEFAULT_ASSET_CATEGORY + "," + UPDATED_ASSET_CATEGORY);

        // Get all the assetAdditionsReportItemList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCategory.in=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCategory is not null
        defaultAssetAdditionsReportItemShouldBeFound("assetCategory.specified=true");

        // Get all the assetAdditionsReportItemList where assetCategory is null
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCategoryContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCategory contains DEFAULT_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldBeFound("assetCategory.contains=" + DEFAULT_ASSET_CATEGORY);

        // Get all the assetAdditionsReportItemList where assetCategory contains UPDATED_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCategory.contains=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCategory does not contain DEFAULT_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCategory.doesNotContain=" + DEFAULT_ASSET_CATEGORY);

        // Get all the assetAdditionsReportItemList where assetCategory does not contain UPDATED_ASSET_CATEGORY
        defaultAssetAdditionsReportItemShouldBeFound("assetCategory.doesNotContain=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetDetails equals to DEFAULT_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldBeFound("assetDetails.equals=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAdditionsReportItemList where assetDetails equals to UPDATED_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldNotBeFound("assetDetails.equals=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetDetails not equals to DEFAULT_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldNotBeFound("assetDetails.notEquals=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAdditionsReportItemList where assetDetails not equals to UPDATED_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldBeFound("assetDetails.notEquals=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetDetails in DEFAULT_ASSET_DETAILS or UPDATED_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldBeFound("assetDetails.in=" + DEFAULT_ASSET_DETAILS + "," + UPDATED_ASSET_DETAILS);

        // Get all the assetAdditionsReportItemList where assetDetails equals to UPDATED_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldNotBeFound("assetDetails.in=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetDetails is not null
        defaultAssetAdditionsReportItemShouldBeFound("assetDetails.specified=true");

        // Get all the assetAdditionsReportItemList where assetDetails is null
        defaultAssetAdditionsReportItemShouldNotBeFound("assetDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetDetailsContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetDetails contains DEFAULT_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldBeFound("assetDetails.contains=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAdditionsReportItemList where assetDetails contains UPDATED_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldNotBeFound("assetDetails.contains=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetDetails does not contain DEFAULT_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldNotBeFound("assetDetails.doesNotContain=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAdditionsReportItemList where assetDetails does not contain UPDATED_ASSET_DETAILS
        defaultAssetAdditionsReportItemShouldBeFound("assetDetails.doesNotContain=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost equals to DEFAULT_ASSET_COST
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.equals=" + DEFAULT_ASSET_COST);

        // Get all the assetAdditionsReportItemList where assetCost equals to UPDATED_ASSET_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.equals=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost not equals to DEFAULT_ASSET_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.notEquals=" + DEFAULT_ASSET_COST);

        // Get all the assetAdditionsReportItemList where assetCost not equals to UPDATED_ASSET_COST
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.notEquals=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost in DEFAULT_ASSET_COST or UPDATED_ASSET_COST
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.in=" + DEFAULT_ASSET_COST + "," + UPDATED_ASSET_COST);

        // Get all the assetAdditionsReportItemList where assetCost equals to UPDATED_ASSET_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.in=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost is not null
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.specified=true");

        // Get all the assetAdditionsReportItemList where assetCost is null
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost is greater than or equal to DEFAULT_ASSET_COST
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.greaterThanOrEqual=" + DEFAULT_ASSET_COST);

        // Get all the assetAdditionsReportItemList where assetCost is greater than or equal to UPDATED_ASSET_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.greaterThanOrEqual=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost is less than or equal to DEFAULT_ASSET_COST
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.lessThanOrEqual=" + DEFAULT_ASSET_COST);

        // Get all the assetAdditionsReportItemList where assetCost is less than or equal to SMALLER_ASSET_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.lessThanOrEqual=" + SMALLER_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost is less than DEFAULT_ASSET_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.lessThan=" + DEFAULT_ASSET_COST);

        // Get all the assetAdditionsReportItemList where assetCost is less than UPDATED_ASSET_COST
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.lessThan=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByAssetCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where assetCost is greater than DEFAULT_ASSET_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("assetCost.greaterThan=" + DEFAULT_ASSET_COST);

        // Get all the assetAdditionsReportItemList where assetCost is greater than SMALLER_ASSET_COST
        defaultAssetAdditionsReportItemShouldBeFound("assetCost.greaterThan=" + SMALLER_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where supplier equals to DEFAULT_SUPPLIER
        defaultAssetAdditionsReportItemShouldBeFound("supplier.equals=" + DEFAULT_SUPPLIER);

        // Get all the assetAdditionsReportItemList where supplier equals to UPDATED_SUPPLIER
        defaultAssetAdditionsReportItemShouldNotBeFound("supplier.equals=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsBySupplierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where supplier not equals to DEFAULT_SUPPLIER
        defaultAssetAdditionsReportItemShouldNotBeFound("supplier.notEquals=" + DEFAULT_SUPPLIER);

        // Get all the assetAdditionsReportItemList where supplier not equals to UPDATED_SUPPLIER
        defaultAssetAdditionsReportItemShouldBeFound("supplier.notEquals=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsBySupplierIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where supplier in DEFAULT_SUPPLIER or UPDATED_SUPPLIER
        defaultAssetAdditionsReportItemShouldBeFound("supplier.in=" + DEFAULT_SUPPLIER + "," + UPDATED_SUPPLIER);

        // Get all the assetAdditionsReportItemList where supplier equals to UPDATED_SUPPLIER
        defaultAssetAdditionsReportItemShouldNotBeFound("supplier.in=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsBySupplierIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where supplier is not null
        defaultAssetAdditionsReportItemShouldBeFound("supplier.specified=true");

        // Get all the assetAdditionsReportItemList where supplier is null
        defaultAssetAdditionsReportItemShouldNotBeFound("supplier.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsBySupplierContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where supplier contains DEFAULT_SUPPLIER
        defaultAssetAdditionsReportItemShouldBeFound("supplier.contains=" + DEFAULT_SUPPLIER);

        // Get all the assetAdditionsReportItemList where supplier contains UPDATED_SUPPLIER
        defaultAssetAdditionsReportItemShouldNotBeFound("supplier.contains=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsBySupplierNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where supplier does not contain DEFAULT_SUPPLIER
        defaultAssetAdditionsReportItemShouldNotBeFound("supplier.doesNotContain=" + DEFAULT_SUPPLIER);

        // Get all the assetAdditionsReportItemList where supplier does not contain UPDATED_SUPPLIER
        defaultAssetAdditionsReportItemShouldBeFound("supplier.doesNotContain=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost equals to DEFAULT_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.equals=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetAdditionsReportItemList where historicalCost equals to UPDATED_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.equals=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost not equals to DEFAULT_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.notEquals=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetAdditionsReportItemList where historicalCost not equals to UPDATED_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.notEquals=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost in DEFAULT_HISTORICAL_COST or UPDATED_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.in=" + DEFAULT_HISTORICAL_COST + "," + UPDATED_HISTORICAL_COST);

        // Get all the assetAdditionsReportItemList where historicalCost equals to UPDATED_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.in=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost is not null
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.specified=true");

        // Get all the assetAdditionsReportItemList where historicalCost is null
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost is greater than or equal to DEFAULT_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.greaterThanOrEqual=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetAdditionsReportItemList where historicalCost is greater than or equal to UPDATED_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.greaterThanOrEqual=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost is less than or equal to DEFAULT_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.lessThanOrEqual=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetAdditionsReportItemList where historicalCost is less than or equal to SMALLER_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.lessThanOrEqual=" + SMALLER_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost is less than DEFAULT_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.lessThan=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetAdditionsReportItemList where historicalCost is less than UPDATED_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.lessThan=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByHistoricalCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where historicalCost is greater than DEFAULT_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldNotBeFound("historicalCost.greaterThan=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetAdditionsReportItemList where historicalCost is greater than SMALLER_HISTORICAL_COST
        defaultAssetAdditionsReportItemShouldBeFound("historicalCost.greaterThan=" + SMALLER_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate equals to DEFAULT_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.equals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the assetAdditionsReportItemList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.equals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate not equals to DEFAULT_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.notEquals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the assetAdditionsReportItemList where registrationDate not equals to UPDATED_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.notEquals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate in DEFAULT_REGISTRATION_DATE or UPDATED_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE);

        // Get all the assetAdditionsReportItemList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.in=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate is not null
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.specified=true");

        // Get all the assetAdditionsReportItemList where registrationDate is null
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate is greater than or equal to DEFAULT_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the assetAdditionsReportItemList where registrationDate is greater than or equal to UPDATED_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate is less than or equal to DEFAULT_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the assetAdditionsReportItemList where registrationDate is less than or equal to SMALLER_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate is less than DEFAULT_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the assetAdditionsReportItemList where registrationDate is less than UPDATED_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportItemsByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);

        // Get all the assetAdditionsReportItemList where registrationDate is greater than DEFAULT_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldNotBeFound("registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the assetAdditionsReportItemList where registrationDate is greater than SMALLER_REGISTRATION_DATE
        defaultAssetAdditionsReportItemShouldBeFound("registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetAdditionsReportItemShouldBeFound(String filter) throws Exception {
        restAssetAdditionsReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAdditionsReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));

        // Check, that the count call also returns 1
        restAssetAdditionsReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetAdditionsReportItemShouldNotBeFound(String filter) throws Exception {
        restAssetAdditionsReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetAdditionsReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetAdditionsReportItem() throws Exception {
        // Get the assetAdditionsReportItem
        restAssetAdditionsReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchAssetAdditionsReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetAdditionsReportItemRepository.saveAndFlush(assetAdditionsReportItem);
        when(mockAssetAdditionsReportItemSearchRepository.search("id:" + assetAdditionsReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetAdditionsReportItem), PageRequest.of(0, 1), 1));

        // Search the assetAdditionsReportItem
        restAssetAdditionsReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetAdditionsReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAdditionsReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));
    }
}
