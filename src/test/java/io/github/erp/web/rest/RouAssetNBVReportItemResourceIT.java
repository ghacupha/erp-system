package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.RouAssetNBVReportItem;
import io.github.erp.repository.RouAssetNBVReportItemRepository;
import io.github.erp.repository.search.RouAssetNBVReportItemSearchRepository;
import io.github.erp.service.criteria.RouAssetNBVReportItemCriteria;
import io.github.erp.service.dto.RouAssetNBVReportItemDTO;
import io.github.erp.service.mapper.RouAssetNBVReportItemMapper;
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
 * Integration tests for the {@link RouAssetNBVReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouAssetNBVReportItemResourceIT {

    private static final String DEFAULT_MODEL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MODEL_VERSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_MODEL_VERSION = new BigDecimal(2);
    private static final BigDecimal SMALLER_MODEL_VERSION = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ROU_MODEL_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_ROU_MODEL_REFERENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_COMMENCEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMMENCEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMMENCEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FISCAL_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FISCAL_PERIOD_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_LEASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LEASE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_LEASE_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/rou-asset-nbv-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-asset-nbv-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouAssetNBVReportItemRepository rouAssetNBVReportItemRepository;

    @Autowired
    private RouAssetNBVReportItemMapper rouAssetNBVReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouAssetNBVReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouAssetNBVReportItemSearchRepository mockRouAssetNBVReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouAssetNBVReportItemMockMvc;

    private RouAssetNBVReportItem rouAssetNBVReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetNBVReportItem createEntity(EntityManager em) {
        RouAssetNBVReportItem rouAssetNBVReportItem = new RouAssetNBVReportItem()
            .modelTitle(DEFAULT_MODEL_TITLE)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .description(DEFAULT_DESCRIPTION)
            .rouModelReference(DEFAULT_ROU_MODEL_REFERENCE)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .assetCategoryName(DEFAULT_ASSET_CATEGORY_NAME)
            .assetAccountNumber(DEFAULT_ASSET_ACCOUNT_NUMBER)
            .depreciationAccountNumber(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)
            .fiscalPeriodEndDate(DEFAULT_FISCAL_PERIOD_END_DATE)
            .leaseAmount(DEFAULT_LEASE_AMOUNT)
            .netBookValue(DEFAULT_NET_BOOK_VALUE);
        return rouAssetNBVReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetNBVReportItem createUpdatedEntity(EntityManager em) {
        RouAssetNBVReportItem rouAssetNBVReportItem = new RouAssetNBVReportItem()
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .rouModelReference(UPDATED_ROU_MODEL_REFERENCE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .assetAccountNumber(UPDATED_ASSET_ACCOUNT_NUMBER)
            .depreciationAccountNumber(UPDATED_DEPRECIATION_ACCOUNT_NUMBER)
            .fiscalPeriodEndDate(UPDATED_FISCAL_PERIOD_END_DATE)
            .leaseAmount(UPDATED_LEASE_AMOUNT)
            .netBookValue(UPDATED_NET_BOOK_VALUE);
        return rouAssetNBVReportItem;
    }

    @BeforeEach
    public void initTest() {
        rouAssetNBVReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItems() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList
        restRouAssetNBVReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetNBVReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].rouModelReference").value(hasItem(DEFAULT_ROU_MODEL_REFERENCE)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))));
    }

    @Test
    @Transactional
    void getRouAssetNBVReportItem() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get the rouAssetNBVReportItem
        restRouAssetNBVReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, rouAssetNBVReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouAssetNBVReportItem.getId().intValue()))
            .andExpect(jsonPath("$.modelTitle").value(DEFAULT_MODEL_TITLE))
            .andExpect(jsonPath("$.modelVersion").value(sameNumber(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.rouModelReference").value(DEFAULT_ROU_MODEL_REFERENCE))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.assetCategoryName").value(DEFAULT_ASSET_CATEGORY_NAME))
            .andExpect(jsonPath("$.assetAccountNumber").value(DEFAULT_ASSET_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.depreciationAccountNumber").value(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.fiscalPeriodEndDate").value(DEFAULT_FISCAL_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.leaseAmount").value(sameNumber(DEFAULT_LEASE_AMOUNT)))
            .andExpect(jsonPath("$.netBookValue").value(sameNumber(DEFAULT_NET_BOOK_VALUE)));
    }

    @Test
    @Transactional
    void getRouAssetNBVReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        Long id = rouAssetNBVReportItem.getId();

        defaultRouAssetNBVReportItemShouldBeFound("id.equals=" + id);
        defaultRouAssetNBVReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultRouAssetNBVReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouAssetNBVReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultRouAssetNBVReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouAssetNBVReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelTitle equals to DEFAULT_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldBeFound("modelTitle.equals=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetNBVReportItemList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldNotBeFound("modelTitle.equals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelTitle not equals to DEFAULT_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldNotBeFound("modelTitle.notEquals=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetNBVReportItemList where modelTitle not equals to UPDATED_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldBeFound("modelTitle.notEquals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelTitleIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelTitle in DEFAULT_MODEL_TITLE or UPDATED_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldBeFound("modelTitle.in=" + DEFAULT_MODEL_TITLE + "," + UPDATED_MODEL_TITLE);

        // Get all the rouAssetNBVReportItemList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldNotBeFound("modelTitle.in=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelTitle is not null
        defaultRouAssetNBVReportItemShouldBeFound("modelTitle.specified=true");

        // Get all the rouAssetNBVReportItemList where modelTitle is null
        defaultRouAssetNBVReportItemShouldNotBeFound("modelTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelTitleContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelTitle contains DEFAULT_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldBeFound("modelTitle.contains=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetNBVReportItemList where modelTitle contains UPDATED_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldNotBeFound("modelTitle.contains=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelTitleNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelTitle does not contain DEFAULT_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldNotBeFound("modelTitle.doesNotContain=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetNBVReportItemList where modelTitle does not contain UPDATED_MODEL_TITLE
        defaultRouAssetNBVReportItemShouldBeFound("modelTitle.doesNotContain=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion equals to DEFAULT_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.equals=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetNBVReportItemList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion not equals to DEFAULT_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.notEquals=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetNBVReportItemList where modelVersion not equals to UPDATED_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.notEquals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion in DEFAULT_MODEL_VERSION or UPDATED_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION);

        // Get all the rouAssetNBVReportItemList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.in=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion is not null
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.specified=true");

        // Get all the rouAssetNBVReportItemList where modelVersion is null
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion is greater than or equal to DEFAULT_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.greaterThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetNBVReportItemList where modelVersion is greater than or equal to UPDATED_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.greaterThanOrEqual=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion is less than or equal to DEFAULT_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.lessThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetNBVReportItemList where modelVersion is less than or equal to SMALLER_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.lessThanOrEqual=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion is less than DEFAULT_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.lessThan=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetNBVReportItemList where modelVersion is less than UPDATED_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.lessThan=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByModelVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where modelVersion is greater than DEFAULT_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldNotBeFound("modelVersion.greaterThan=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetNBVReportItemList where modelVersion is greater than SMALLER_MODEL_VERSION
        defaultRouAssetNBVReportItemShouldBeFound("modelVersion.greaterThan=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where description equals to DEFAULT_DESCRIPTION
        defaultRouAssetNBVReportItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetNBVReportItemList where description equals to UPDATED_DESCRIPTION
        defaultRouAssetNBVReportItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where description not equals to DEFAULT_DESCRIPTION
        defaultRouAssetNBVReportItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetNBVReportItemList where description not equals to UPDATED_DESCRIPTION
        defaultRouAssetNBVReportItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRouAssetNBVReportItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rouAssetNBVReportItemList where description equals to UPDATED_DESCRIPTION
        defaultRouAssetNBVReportItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where description is not null
        defaultRouAssetNBVReportItemShouldBeFound("description.specified=true");

        // Get all the rouAssetNBVReportItemList where description is null
        defaultRouAssetNBVReportItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where description contains DEFAULT_DESCRIPTION
        defaultRouAssetNBVReportItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetNBVReportItemList where description contains UPDATED_DESCRIPTION
        defaultRouAssetNBVReportItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where description does not contain DEFAULT_DESCRIPTION
        defaultRouAssetNBVReportItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetNBVReportItemList where description does not contain UPDATED_DESCRIPTION
        defaultRouAssetNBVReportItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByRouModelReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where rouModelReference equals to DEFAULT_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldBeFound("rouModelReference.equals=" + DEFAULT_ROU_MODEL_REFERENCE);

        // Get all the rouAssetNBVReportItemList where rouModelReference equals to UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldNotBeFound("rouModelReference.equals=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByRouModelReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where rouModelReference not equals to DEFAULT_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldNotBeFound("rouModelReference.notEquals=" + DEFAULT_ROU_MODEL_REFERENCE);

        // Get all the rouAssetNBVReportItemList where rouModelReference not equals to UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldBeFound("rouModelReference.notEquals=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByRouModelReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where rouModelReference in DEFAULT_ROU_MODEL_REFERENCE or UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldBeFound(
            "rouModelReference.in=" + DEFAULT_ROU_MODEL_REFERENCE + "," + UPDATED_ROU_MODEL_REFERENCE
        );

        // Get all the rouAssetNBVReportItemList where rouModelReference equals to UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldNotBeFound("rouModelReference.in=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByRouModelReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where rouModelReference is not null
        defaultRouAssetNBVReportItemShouldBeFound("rouModelReference.specified=true");

        // Get all the rouAssetNBVReportItemList where rouModelReference is null
        defaultRouAssetNBVReportItemShouldNotBeFound("rouModelReference.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByRouModelReferenceContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where rouModelReference contains DEFAULT_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldBeFound("rouModelReference.contains=" + DEFAULT_ROU_MODEL_REFERENCE);

        // Get all the rouAssetNBVReportItemList where rouModelReference contains UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldNotBeFound("rouModelReference.contains=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByRouModelReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where rouModelReference does not contain DEFAULT_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldNotBeFound("rouModelReference.doesNotContain=" + DEFAULT_ROU_MODEL_REFERENCE);

        // Get all the rouAssetNBVReportItemList where rouModelReference does not contain UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetNBVReportItemShouldBeFound("rouModelReference.doesNotContain=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate equals to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.equals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetNBVReportItemList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.equals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate not equals to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.notEquals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetNBVReportItemList where commencementDate not equals to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.notEquals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate in DEFAULT_COMMENCEMENT_DATE or UPDATED_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.in=" + DEFAULT_COMMENCEMENT_DATE + "," + UPDATED_COMMENCEMENT_DATE);

        // Get all the rouAssetNBVReportItemList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.in=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate is not null
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.specified=true");

        // Get all the rouAssetNBVReportItemList where commencementDate is null
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate is greater than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.greaterThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetNBVReportItemList where commencementDate is greater than or equal to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.greaterThanOrEqual=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate is less than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.lessThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetNBVReportItemList where commencementDate is less than or equal to SMALLER_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.lessThanOrEqual=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate is less than DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.lessThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetNBVReportItemList where commencementDate is less than UPDATED_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.lessThan=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByCommencementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where commencementDate is greater than DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("commencementDate.greaterThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetNBVReportItemList where commencementDate is greater than SMALLER_COMMENCEMENT_DATE
        defaultRouAssetNBVReportItemShouldBeFound("commencementDate.greaterThan=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetNBVReportItemList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetNBVReportItemList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the rouAssetNBVReportItemList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate is not null
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.specified=true");

        // Get all the rouAssetNBVReportItemList where expirationDate is null
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetNBVReportItemList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetNBVReportItemList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetNBVReportItemList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetNBVReportItemList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultRouAssetNBVReportItemShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetCategoryName equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldBeFound("assetCategoryName.equals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetNBVReportItemList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldNotBeFound("assetCategoryName.equals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetCategoryName not equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldNotBeFound("assetCategoryName.notEquals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetNBVReportItemList where assetCategoryName not equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldBeFound("assetCategoryName.notEquals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetCategoryName in DEFAULT_ASSET_CATEGORY_NAME or UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldBeFound(
            "assetCategoryName.in=" + DEFAULT_ASSET_CATEGORY_NAME + "," + UPDATED_ASSET_CATEGORY_NAME
        );

        // Get all the rouAssetNBVReportItemList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldNotBeFound("assetCategoryName.in=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetCategoryName is not null
        defaultRouAssetNBVReportItemShouldBeFound("assetCategoryName.specified=true");

        // Get all the rouAssetNBVReportItemList where assetCategoryName is null
        defaultRouAssetNBVReportItemShouldNotBeFound("assetCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetCategoryName contains DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldBeFound("assetCategoryName.contains=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetNBVReportItemList where assetCategoryName contains UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldNotBeFound("assetCategoryName.contains=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetCategoryName does not contain DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldNotBeFound("assetCategoryName.doesNotContain=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetNBVReportItemList where assetCategoryName does not contain UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetNBVReportItemShouldBeFound("assetCategoryName.doesNotContain=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("assetAccountNumber.equals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("assetAccountNumber.equals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber not equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("assetAccountNumber.notEquals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber not equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("assetAccountNumber.notEquals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber in DEFAULT_ASSET_ACCOUNT_NUMBER or UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound(
            "assetAccountNumber.in=" + DEFAULT_ASSET_ACCOUNT_NUMBER + "," + UPDATED_ASSET_ACCOUNT_NUMBER
        );

        // Get all the rouAssetNBVReportItemList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("assetAccountNumber.in=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber is not null
        defaultRouAssetNBVReportItemShouldBeFound("assetAccountNumber.specified=true");

        // Get all the rouAssetNBVReportItemList where assetAccountNumber is null
        defaultRouAssetNBVReportItemShouldNotBeFound("assetAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber contains DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("assetAccountNumber.contains=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber contains UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("assetAccountNumber.contains=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByAssetAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber does not contain DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("assetAccountNumber.doesNotContain=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where assetAccountNumber does not contain UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("assetAccountNumber.doesNotContain=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDepreciationAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("depreciationAccountNumber.equals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("depreciationAccountNumber.equals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDepreciationAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber not equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("depreciationAccountNumber.notEquals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber not equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("depreciationAccountNumber.notEquals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDepreciationAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber in DEFAULT_DEPRECIATION_ACCOUNT_NUMBER or UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound(
            "depreciationAccountNumber.in=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER + "," + UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("depreciationAccountNumber.in=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDepreciationAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber is not null
        defaultRouAssetNBVReportItemShouldBeFound("depreciationAccountNumber.specified=true");

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber is null
        defaultRouAssetNBVReportItemShouldNotBeFound("depreciationAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDepreciationAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber contains DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("depreciationAccountNumber.contains=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber contains UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("depreciationAccountNumber.contains=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByDepreciationAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber does not contain DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldNotBeFound("depreciationAccountNumber.doesNotContain=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetNBVReportItemList where depreciationAccountNumber does not contain UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetNBVReportItemShouldBeFound("depreciationAccountNumber.doesNotContain=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate equals to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldBeFound("fiscalPeriodEndDate.equals=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.equals=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate not equals to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.notEquals=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate not equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldBeFound("fiscalPeriodEndDate.notEquals=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate in DEFAULT_FISCAL_PERIOD_END_DATE or UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldBeFound(
            "fiscalPeriodEndDate.in=" + DEFAULT_FISCAL_PERIOD_END_DATE + "," + UPDATED_FISCAL_PERIOD_END_DATE
        );

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.in=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is not null
        defaultRouAssetNBVReportItemShouldBeFound("fiscalPeriodEndDate.specified=true");

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is null
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is greater than or equal to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldBeFound("fiscalPeriodEndDate.greaterThanOrEqual=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is greater than or equal to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.greaterThanOrEqual=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is less than or equal to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldBeFound("fiscalPeriodEndDate.lessThanOrEqual=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is less than or equal to SMALLER_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.lessThanOrEqual=" + SMALLER_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is less than DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.lessThan=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is less than UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldBeFound("fiscalPeriodEndDate.lessThan=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByFiscalPeriodEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is greater than DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldNotBeFound("fiscalPeriodEndDate.greaterThan=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAssetNBVReportItemList where fiscalPeriodEndDate is greater than SMALLER_FISCAL_PERIOD_END_DATE
        defaultRouAssetNBVReportItemShouldBeFound("fiscalPeriodEndDate.greaterThan=" + SMALLER_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount equals to DEFAULT_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.equals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetNBVReportItemList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.equals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount not equals to DEFAULT_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.notEquals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetNBVReportItemList where leaseAmount not equals to UPDATED_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.notEquals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount in DEFAULT_LEASE_AMOUNT or UPDATED_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.in=" + DEFAULT_LEASE_AMOUNT + "," + UPDATED_LEASE_AMOUNT);

        // Get all the rouAssetNBVReportItemList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.in=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount is not null
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.specified=true");

        // Get all the rouAssetNBVReportItemList where leaseAmount is null
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount is greater than or equal to DEFAULT_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.greaterThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetNBVReportItemList where leaseAmount is greater than or equal to UPDATED_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.greaterThanOrEqual=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount is less than or equal to DEFAULT_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.lessThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetNBVReportItemList where leaseAmount is less than or equal to SMALLER_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.lessThanOrEqual=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount is less than DEFAULT_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.lessThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetNBVReportItemList where leaseAmount is less than UPDATED_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.lessThan=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByLeaseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where leaseAmount is greater than DEFAULT_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldNotBeFound("leaseAmount.greaterThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetNBVReportItemList where leaseAmount is greater than SMALLER_LEASE_AMOUNT
        defaultRouAssetNBVReportItemShouldBeFound("leaseAmount.greaterThan=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue equals to DEFAULT_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.equals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAssetNBVReportItemList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.equals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue not equals to DEFAULT_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.notEquals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAssetNBVReportItemList where netBookValue not equals to UPDATED_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.notEquals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue in DEFAULT_NET_BOOK_VALUE or UPDATED_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.in=" + DEFAULT_NET_BOOK_VALUE + "," + UPDATED_NET_BOOK_VALUE);

        // Get all the rouAssetNBVReportItemList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.in=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue is not null
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.specified=true");

        // Get all the rouAssetNBVReportItemList where netBookValue is null
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue is greater than or equal to DEFAULT_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAssetNBVReportItemList where netBookValue is greater than or equal to UPDATED_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue is less than or equal to DEFAULT_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAssetNBVReportItemList where netBookValue is less than or equal to SMALLER_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue is less than DEFAULT_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.lessThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAssetNBVReportItemList where netBookValue is less than UPDATED_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.lessThan=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportItemsByNetBookValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);

        // Get all the rouAssetNBVReportItemList where netBookValue is greater than DEFAULT_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldNotBeFound("netBookValue.greaterThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAssetNBVReportItemList where netBookValue is greater than SMALLER_NET_BOOK_VALUE
        defaultRouAssetNBVReportItemShouldBeFound("netBookValue.greaterThan=" + SMALLER_NET_BOOK_VALUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouAssetNBVReportItemShouldBeFound(String filter) throws Exception {
        restRouAssetNBVReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetNBVReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].rouModelReference").value(hasItem(DEFAULT_ROU_MODEL_REFERENCE)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))));

        // Check, that the count call also returns 1
        restRouAssetNBVReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouAssetNBVReportItemShouldNotBeFound(String filter) throws Exception {
        restRouAssetNBVReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouAssetNBVReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouAssetNBVReportItem() throws Exception {
        // Get the rouAssetNBVReportItem
        restRouAssetNBVReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchRouAssetNBVReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouAssetNBVReportItemRepository.saveAndFlush(rouAssetNBVReportItem);
        when(mockRouAssetNBVReportItemSearchRepository.search("id:" + rouAssetNBVReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouAssetNBVReportItem), PageRequest.of(0, 1), 1));

        // Search the rouAssetNBVReportItem
        restRouAssetNBVReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouAssetNBVReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetNBVReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].rouModelReference").value(hasItem(DEFAULT_ROU_MODEL_REFERENCE)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))));
    }
}
