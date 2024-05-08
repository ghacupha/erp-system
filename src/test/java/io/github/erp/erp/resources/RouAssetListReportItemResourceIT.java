package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.RouAssetListReportItem;
import io.github.erp.repository.RouAssetListReportItemRepository;
import io.github.erp.repository.search.RouAssetListReportItemSearchRepository;
import io.github.erp.service.mapper.RouAssetListReportItemMapper;
import io.github.erp.web.rest.RouAssetListReportItemResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RouAssetListReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class RouAssetListReportItemResourceIT {

    private static final String DEFAULT_MODEL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MODEL_VERSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_MODEL_VERSION = new BigDecimal(2);
    private static final BigDecimal SMALLER_MODEL_VERSION = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEASE_TERM_PERIODS = 1;
    private static final Integer UPDATED_LEASE_TERM_PERIODS = 2;
    private static final Integer SMALLER_LEASE_TERM_PERIODS = 1 - 1;

    private static final UUID DEFAULT_ROU_MODEL_REFERENCE = UUID.randomUUID();
    private static final UUID UPDATED_ROU_MODEL_REFERENCE = UUID.randomUUID();

    private static final LocalDate DEFAULT_COMMENCEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMMENCEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMMENCEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LEASE_CONTRACT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_CONTRACT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LEASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LEASE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_LEASE_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_CONTRACT_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/leases/rou-asset-list-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/rou-asset-list-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouAssetListReportItemRepository rouAssetListReportItemRepository;

    @Autowired
    private RouAssetListReportItemMapper rouAssetListReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouAssetListReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouAssetListReportItemSearchRepository mockRouAssetListReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouAssetListReportItemMockMvc;

    private RouAssetListReportItem rouAssetListReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetListReportItem createEntity(EntityManager em) {
        RouAssetListReportItem rouAssetListReportItem = new RouAssetListReportItem()
            .modelTitle(DEFAULT_MODEL_TITLE)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .description(DEFAULT_DESCRIPTION)
            .leaseTermPeriods(DEFAULT_LEASE_TERM_PERIODS)
            .rouModelReference(DEFAULT_ROU_MODEL_REFERENCE)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .leaseContractTitle(DEFAULT_LEASE_CONTRACT_TITLE)
            .assetAccountNumber(DEFAULT_ASSET_ACCOUNT_NUMBER)
            .depreciationAccountNumber(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)
            .accruedDepreciationAccountNumber(DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER)
            .assetCategoryName(DEFAULT_ASSET_CATEGORY_NAME)
            .leaseAmount(DEFAULT_LEASE_AMOUNT)
            .leaseContractSerialNumber(DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER);
        return rouAssetListReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetListReportItem createUpdatedEntity(EntityManager em) {
        RouAssetListReportItem rouAssetListReportItem = new RouAssetListReportItem()
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .leaseTermPeriods(UPDATED_LEASE_TERM_PERIODS)
            .rouModelReference(UPDATED_ROU_MODEL_REFERENCE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .leaseContractTitle(UPDATED_LEASE_CONTRACT_TITLE)
            .assetAccountNumber(UPDATED_ASSET_ACCOUNT_NUMBER)
            .depreciationAccountNumber(UPDATED_DEPRECIATION_ACCOUNT_NUMBER)
            .accruedDepreciationAccountNumber(UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .leaseAmount(UPDATED_LEASE_AMOUNT)
            .leaseContractSerialNumber(UPDATED_LEASE_CONTRACT_SERIAL_NUMBER);
        return rouAssetListReportItem;
    }

    @BeforeEach
    public void initTest() {
        rouAssetListReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItems() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList
        restRouAssetListReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetListReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leaseTermPeriods").value(hasItem(DEFAULT_LEASE_TERM_PERIODS)))
            .andExpect(jsonPath("$.[*].rouModelReference").value(hasItem(DEFAULT_ROU_MODEL_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseContractTitle").value(hasItem(DEFAULT_LEASE_CONTRACT_TITLE)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accruedDepreciationAccountNumber").value(hasItem(DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].leaseContractSerialNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER)));
    }

    @Test
    @Transactional
    void getRouAssetListReportItem() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get the rouAssetListReportItem
        restRouAssetListReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, rouAssetListReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouAssetListReportItem.getId().intValue()))
            .andExpect(jsonPath("$.modelTitle").value(DEFAULT_MODEL_TITLE))
            .andExpect(jsonPath("$.modelVersion").value(sameNumber(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.leaseTermPeriods").value(DEFAULT_LEASE_TERM_PERIODS))
            .andExpect(jsonPath("$.rouModelReference").value(DEFAULT_ROU_MODEL_REFERENCE.toString()))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.leaseContractTitle").value(DEFAULT_LEASE_CONTRACT_TITLE))
            .andExpect(jsonPath("$.assetAccountNumber").value(DEFAULT_ASSET_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.depreciationAccountNumber").value(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.accruedDepreciationAccountNumber").value(DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.assetCategoryName").value(DEFAULT_ASSET_CATEGORY_NAME))
            .andExpect(jsonPath("$.leaseAmount").value(sameNumber(DEFAULT_LEASE_AMOUNT)))
            .andExpect(jsonPath("$.leaseContractSerialNumber").value(DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER));
    }

    @Test
    @Transactional
    void getRouAssetListReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        Long id = rouAssetListReportItem.getId();

        defaultRouAssetListReportItemShouldBeFound("id.equals=" + id);
        defaultRouAssetListReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultRouAssetListReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouAssetListReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultRouAssetListReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouAssetListReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelTitle equals to DEFAULT_MODEL_TITLE
        defaultRouAssetListReportItemShouldBeFound("modelTitle.equals=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetListReportItemList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("modelTitle.equals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelTitle not equals to DEFAULT_MODEL_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("modelTitle.notEquals=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetListReportItemList where modelTitle not equals to UPDATED_MODEL_TITLE
        defaultRouAssetListReportItemShouldBeFound("modelTitle.notEquals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelTitleIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelTitle in DEFAULT_MODEL_TITLE or UPDATED_MODEL_TITLE
        defaultRouAssetListReportItemShouldBeFound("modelTitle.in=" + DEFAULT_MODEL_TITLE + "," + UPDATED_MODEL_TITLE);

        // Get all the rouAssetListReportItemList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("modelTitle.in=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelTitle is not null
        defaultRouAssetListReportItemShouldBeFound("modelTitle.specified=true");

        // Get all the rouAssetListReportItemList where modelTitle is null
        defaultRouAssetListReportItemShouldNotBeFound("modelTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelTitleContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelTitle contains DEFAULT_MODEL_TITLE
        defaultRouAssetListReportItemShouldBeFound("modelTitle.contains=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetListReportItemList where modelTitle contains UPDATED_MODEL_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("modelTitle.contains=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelTitleNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelTitle does not contain DEFAULT_MODEL_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("modelTitle.doesNotContain=" + DEFAULT_MODEL_TITLE);

        // Get all the rouAssetListReportItemList where modelTitle does not contain UPDATED_MODEL_TITLE
        defaultRouAssetListReportItemShouldBeFound("modelTitle.doesNotContain=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion equals to DEFAULT_MODEL_VERSION
        defaultRouAssetListReportItemShouldBeFound("modelVersion.equals=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetListReportItemList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion not equals to DEFAULT_MODEL_VERSION
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.notEquals=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetListReportItemList where modelVersion not equals to UPDATED_MODEL_VERSION
        defaultRouAssetListReportItemShouldBeFound("modelVersion.notEquals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion in DEFAULT_MODEL_VERSION or UPDATED_MODEL_VERSION
        defaultRouAssetListReportItemShouldBeFound("modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION);

        // Get all the rouAssetListReportItemList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.in=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion is not null
        defaultRouAssetListReportItemShouldBeFound("modelVersion.specified=true");

        // Get all the rouAssetListReportItemList where modelVersion is null
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion is greater than or equal to DEFAULT_MODEL_VERSION
        defaultRouAssetListReportItemShouldBeFound("modelVersion.greaterThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetListReportItemList where modelVersion is greater than or equal to UPDATED_MODEL_VERSION
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.greaterThanOrEqual=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion is less than or equal to DEFAULT_MODEL_VERSION
        defaultRouAssetListReportItemShouldBeFound("modelVersion.lessThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetListReportItemList where modelVersion is less than or equal to SMALLER_MODEL_VERSION
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.lessThanOrEqual=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion is less than DEFAULT_MODEL_VERSION
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.lessThan=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetListReportItemList where modelVersion is less than UPDATED_MODEL_VERSION
        defaultRouAssetListReportItemShouldBeFound("modelVersion.lessThan=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByModelVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where modelVersion is greater than DEFAULT_MODEL_VERSION
        defaultRouAssetListReportItemShouldNotBeFound("modelVersion.greaterThan=" + DEFAULT_MODEL_VERSION);

        // Get all the rouAssetListReportItemList where modelVersion is greater than SMALLER_MODEL_VERSION
        defaultRouAssetListReportItemShouldBeFound("modelVersion.greaterThan=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where description equals to DEFAULT_DESCRIPTION
        defaultRouAssetListReportItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetListReportItemList where description equals to UPDATED_DESCRIPTION
        defaultRouAssetListReportItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where description not equals to DEFAULT_DESCRIPTION
        defaultRouAssetListReportItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetListReportItemList where description not equals to UPDATED_DESCRIPTION
        defaultRouAssetListReportItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRouAssetListReportItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rouAssetListReportItemList where description equals to UPDATED_DESCRIPTION
        defaultRouAssetListReportItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where description is not null
        defaultRouAssetListReportItemShouldBeFound("description.specified=true");

        // Get all the rouAssetListReportItemList where description is null
        defaultRouAssetListReportItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where description contains DEFAULT_DESCRIPTION
        defaultRouAssetListReportItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetListReportItemList where description contains UPDATED_DESCRIPTION
        defaultRouAssetListReportItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where description does not contain DEFAULT_DESCRIPTION
        defaultRouAssetListReportItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rouAssetListReportItemList where description does not contain UPDATED_DESCRIPTION
        defaultRouAssetListReportItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods equals to DEFAULT_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.equals=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouAssetListReportItemList where leaseTermPeriods equals to UPDATED_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.equals=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods not equals to DEFAULT_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.notEquals=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouAssetListReportItemList where leaseTermPeriods not equals to UPDATED_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.notEquals=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods in DEFAULT_LEASE_TERM_PERIODS or UPDATED_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.in=" + DEFAULT_LEASE_TERM_PERIODS + "," + UPDATED_LEASE_TERM_PERIODS);

        // Get all the rouAssetListReportItemList where leaseTermPeriods equals to UPDATED_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.in=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is not null
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.specified=true");

        // Get all the rouAssetListReportItemList where leaseTermPeriods is null
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is greater than or equal to DEFAULT_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.greaterThanOrEqual=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is greater than or equal to UPDATED_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.greaterThanOrEqual=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is less than or equal to DEFAULT_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.lessThanOrEqual=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is less than or equal to SMALLER_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.lessThanOrEqual=" + SMALLER_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is less than DEFAULT_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.lessThan=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is less than UPDATED_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.lessThan=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseTermPeriodsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is greater than DEFAULT_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldNotBeFound("leaseTermPeriods.greaterThan=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouAssetListReportItemList where leaseTermPeriods is greater than SMALLER_LEASE_TERM_PERIODS
        defaultRouAssetListReportItemShouldBeFound("leaseTermPeriods.greaterThan=" + SMALLER_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByRouModelReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where rouModelReference equals to DEFAULT_ROU_MODEL_REFERENCE
        defaultRouAssetListReportItemShouldBeFound("rouModelReference.equals=" + DEFAULT_ROU_MODEL_REFERENCE);

        // Get all the rouAssetListReportItemList where rouModelReference equals to UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetListReportItemShouldNotBeFound("rouModelReference.equals=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByRouModelReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where rouModelReference not equals to DEFAULT_ROU_MODEL_REFERENCE
        defaultRouAssetListReportItemShouldNotBeFound("rouModelReference.notEquals=" + DEFAULT_ROU_MODEL_REFERENCE);

        // Get all the rouAssetListReportItemList where rouModelReference not equals to UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetListReportItemShouldBeFound("rouModelReference.notEquals=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByRouModelReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where rouModelReference in DEFAULT_ROU_MODEL_REFERENCE or UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetListReportItemShouldBeFound(
            "rouModelReference.in=" + DEFAULT_ROU_MODEL_REFERENCE + "," + UPDATED_ROU_MODEL_REFERENCE
        );

        // Get all the rouAssetListReportItemList where rouModelReference equals to UPDATED_ROU_MODEL_REFERENCE
        defaultRouAssetListReportItemShouldNotBeFound("rouModelReference.in=" + UPDATED_ROU_MODEL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByRouModelReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where rouModelReference is not null
        defaultRouAssetListReportItemShouldBeFound("rouModelReference.specified=true");

        // Get all the rouAssetListReportItemList where rouModelReference is null
        defaultRouAssetListReportItemShouldNotBeFound("rouModelReference.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate equals to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldBeFound("commencementDate.equals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetListReportItemList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.equals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate not equals to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.notEquals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetListReportItemList where commencementDate not equals to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldBeFound("commencementDate.notEquals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate in DEFAULT_COMMENCEMENT_DATE or UPDATED_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldBeFound("commencementDate.in=" + DEFAULT_COMMENCEMENT_DATE + "," + UPDATED_COMMENCEMENT_DATE);

        // Get all the rouAssetListReportItemList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.in=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate is not null
        defaultRouAssetListReportItemShouldBeFound("commencementDate.specified=true");

        // Get all the rouAssetListReportItemList where commencementDate is null
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate is greater than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldBeFound("commencementDate.greaterThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetListReportItemList where commencementDate is greater than or equal to UPDATED_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.greaterThanOrEqual=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate is less than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldBeFound("commencementDate.lessThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetListReportItemList where commencementDate is less than or equal to SMALLER_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.lessThanOrEqual=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate is less than DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.lessThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetListReportItemList where commencementDate is less than UPDATED_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldBeFound("commencementDate.lessThan=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByCommencementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where commencementDate is greater than DEFAULT_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldNotBeFound("commencementDate.greaterThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the rouAssetListReportItemList where commencementDate is greater than SMALLER_COMMENCEMENT_DATE
        defaultRouAssetListReportItemShouldBeFound("commencementDate.greaterThan=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetListReportItemList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetListReportItemList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the rouAssetListReportItemList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate is not null
        defaultRouAssetListReportItemShouldBeFound("expirationDate.specified=true");

        // Get all the rouAssetListReportItemList where expirationDate is null
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetListReportItemList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetListReportItemList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetListReportItemList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the rouAssetListReportItemList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultRouAssetListReportItemShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractTitle equals to DEFAULT_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldBeFound("leaseContractTitle.equals=" + DEFAULT_LEASE_CONTRACT_TITLE);

        // Get all the rouAssetListReportItemList where leaseContractTitle equals to UPDATED_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractTitle.equals=" + UPDATED_LEASE_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractTitle not equals to DEFAULT_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractTitle.notEquals=" + DEFAULT_LEASE_CONTRACT_TITLE);

        // Get all the rouAssetListReportItemList where leaseContractTitle not equals to UPDATED_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldBeFound("leaseContractTitle.notEquals=" + UPDATED_LEASE_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractTitleIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractTitle in DEFAULT_LEASE_CONTRACT_TITLE or UPDATED_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldBeFound(
            "leaseContractTitle.in=" + DEFAULT_LEASE_CONTRACT_TITLE + "," + UPDATED_LEASE_CONTRACT_TITLE
        );

        // Get all the rouAssetListReportItemList where leaseContractTitle equals to UPDATED_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractTitle.in=" + UPDATED_LEASE_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractTitle is not null
        defaultRouAssetListReportItemShouldBeFound("leaseContractTitle.specified=true");

        // Get all the rouAssetListReportItemList where leaseContractTitle is null
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractTitleContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractTitle contains DEFAULT_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldBeFound("leaseContractTitle.contains=" + DEFAULT_LEASE_CONTRACT_TITLE);

        // Get all the rouAssetListReportItemList where leaseContractTitle contains UPDATED_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractTitle.contains=" + UPDATED_LEASE_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractTitleNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractTitle does not contain DEFAULT_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractTitle.doesNotContain=" + DEFAULT_LEASE_CONTRACT_TITLE);

        // Get all the rouAssetListReportItemList where leaseContractTitle does not contain UPDATED_LEASE_CONTRACT_TITLE
        defaultRouAssetListReportItemShouldBeFound("leaseContractTitle.doesNotContain=" + UPDATED_LEASE_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetAccountNumber equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("assetAccountNumber.equals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("assetAccountNumber.equals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetAccountNumber not equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("assetAccountNumber.notEquals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where assetAccountNumber not equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("assetAccountNumber.notEquals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetAccountNumber in DEFAULT_ASSET_ACCOUNT_NUMBER or UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "assetAccountNumber.in=" + DEFAULT_ASSET_ACCOUNT_NUMBER + "," + UPDATED_ASSET_ACCOUNT_NUMBER
        );

        // Get all the rouAssetListReportItemList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("assetAccountNumber.in=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetAccountNumber is not null
        defaultRouAssetListReportItemShouldBeFound("assetAccountNumber.specified=true");

        // Get all the rouAssetListReportItemList where assetAccountNumber is null
        defaultRouAssetListReportItemShouldNotBeFound("assetAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetAccountNumber contains DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("assetAccountNumber.contains=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where assetAccountNumber contains UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("assetAccountNumber.contains=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetAccountNumber does not contain DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("assetAccountNumber.doesNotContain=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where assetAccountNumber does not contain UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("assetAccountNumber.doesNotContain=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDepreciationAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("depreciationAccountNumber.equals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("depreciationAccountNumber.equals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDepreciationAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber not equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("depreciationAccountNumber.notEquals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber not equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("depreciationAccountNumber.notEquals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDepreciationAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber in DEFAULT_DEPRECIATION_ACCOUNT_NUMBER or UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "depreciationAccountNumber.in=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER + "," + UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAssetListReportItemList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("depreciationAccountNumber.in=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDepreciationAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber is not null
        defaultRouAssetListReportItemShouldBeFound("depreciationAccountNumber.specified=true");

        // Get all the rouAssetListReportItemList where depreciationAccountNumber is null
        defaultRouAssetListReportItemShouldNotBeFound("depreciationAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDepreciationAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber contains DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("depreciationAccountNumber.contains=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber contains UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("depreciationAccountNumber.contains=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByDepreciationAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber does not contain DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("depreciationAccountNumber.doesNotContain=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAssetListReportItemList where depreciationAccountNumber does not contain UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound("depreciationAccountNumber.doesNotContain=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAccruedDepreciationAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber equals to DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "accruedDepreciationAccountNumber.equals=" + DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber equals to UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound(
            "accruedDepreciationAccountNumber.equals=" + UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAccruedDepreciationAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber not equals to DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound(
            "accruedDepreciationAccountNumber.notEquals=" + DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber not equals to UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "accruedDepreciationAccountNumber.notEquals=" + UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAccruedDepreciationAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber in DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER or UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "accruedDepreciationAccountNumber.in=" +
            DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER +
            "," +
            UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber equals to UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("accruedDepreciationAccountNumber.in=" + UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAccruedDepreciationAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber is not null
        defaultRouAssetListReportItemShouldBeFound("accruedDepreciationAccountNumber.specified=true");

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber is null
        defaultRouAssetListReportItemShouldNotBeFound("accruedDepreciationAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAccruedDepreciationAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber contains DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "accruedDepreciationAccountNumber.contains=" + DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber contains UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound(
            "accruedDepreciationAccountNumber.contains=" + UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAccruedDepreciationAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber does not contain DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound(
            "accruedDepreciationAccountNumber.doesNotContain=" + DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAssetListReportItemList where accruedDepreciationAccountNumber does not contain UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "accruedDepreciationAccountNumber.doesNotContain=" + UPDATED_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetCategoryName equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldBeFound("assetCategoryName.equals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetListReportItemList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldNotBeFound("assetCategoryName.equals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetCategoryName not equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldNotBeFound("assetCategoryName.notEquals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetListReportItemList where assetCategoryName not equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldBeFound("assetCategoryName.notEquals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetCategoryName in DEFAULT_ASSET_CATEGORY_NAME or UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldBeFound(
            "assetCategoryName.in=" + DEFAULT_ASSET_CATEGORY_NAME + "," + UPDATED_ASSET_CATEGORY_NAME
        );

        // Get all the rouAssetListReportItemList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldNotBeFound("assetCategoryName.in=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetCategoryName is not null
        defaultRouAssetListReportItemShouldBeFound("assetCategoryName.specified=true");

        // Get all the rouAssetListReportItemList where assetCategoryName is null
        defaultRouAssetListReportItemShouldNotBeFound("assetCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetCategoryName contains DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldBeFound("assetCategoryName.contains=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetListReportItemList where assetCategoryName contains UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldNotBeFound("assetCategoryName.contains=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByAssetCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where assetCategoryName does not contain DEFAULT_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldNotBeFound("assetCategoryName.doesNotContain=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouAssetListReportItemList where assetCategoryName does not contain UPDATED_ASSET_CATEGORY_NAME
        defaultRouAssetListReportItemShouldBeFound("assetCategoryName.doesNotContain=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount equals to DEFAULT_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.equals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetListReportItemList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.equals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount not equals to DEFAULT_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.notEquals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetListReportItemList where leaseAmount not equals to UPDATED_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.notEquals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount in DEFAULT_LEASE_AMOUNT or UPDATED_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.in=" + DEFAULT_LEASE_AMOUNT + "," + UPDATED_LEASE_AMOUNT);

        // Get all the rouAssetListReportItemList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.in=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount is not null
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.specified=true");

        // Get all the rouAssetListReportItemList where leaseAmount is null
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount is greater than or equal to DEFAULT_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.greaterThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetListReportItemList where leaseAmount is greater than or equal to UPDATED_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.greaterThanOrEqual=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount is less than or equal to DEFAULT_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.lessThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetListReportItemList where leaseAmount is less than or equal to SMALLER_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.lessThanOrEqual=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount is less than DEFAULT_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.lessThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetListReportItemList where leaseAmount is less than UPDATED_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.lessThan=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseAmount is greater than DEFAULT_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldNotBeFound("leaseAmount.greaterThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouAssetListReportItemList where leaseAmount is greater than SMALLER_LEASE_AMOUNT
        defaultRouAssetListReportItemShouldBeFound("leaseAmount.greaterThan=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractSerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber equals to DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldBeFound("leaseContractSerialNumber.equals=" + DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber equals to UPDATED_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractSerialNumber.equals=" + UPDATED_LEASE_CONTRACT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractSerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber not equals to DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractSerialNumber.notEquals=" + DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber not equals to UPDATED_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldBeFound("leaseContractSerialNumber.notEquals=" + UPDATED_LEASE_CONTRACT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractSerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber in DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER or UPDATED_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldBeFound(
            "leaseContractSerialNumber.in=" + DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER + "," + UPDATED_LEASE_CONTRACT_SERIAL_NUMBER
        );

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber equals to UPDATED_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractSerialNumber.in=" + UPDATED_LEASE_CONTRACT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractSerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber is not null
        defaultRouAssetListReportItemShouldBeFound("leaseContractSerialNumber.specified=true");

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber is null
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractSerialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractSerialNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber contains DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldBeFound("leaseContractSerialNumber.contains=" + DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber contains UPDATED_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractSerialNumber.contains=" + UPDATED_LEASE_CONTRACT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportItemsByLeaseContractSerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber does not contain DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldNotBeFound("leaseContractSerialNumber.doesNotContain=" + DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER);

        // Get all the rouAssetListReportItemList where leaseContractSerialNumber does not contain UPDATED_LEASE_CONTRACT_SERIAL_NUMBER
        defaultRouAssetListReportItemShouldBeFound("leaseContractSerialNumber.doesNotContain=" + UPDATED_LEASE_CONTRACT_SERIAL_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouAssetListReportItemShouldBeFound(String filter) throws Exception {
        restRouAssetListReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetListReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leaseTermPeriods").value(hasItem(DEFAULT_LEASE_TERM_PERIODS)))
            .andExpect(jsonPath("$.[*].rouModelReference").value(hasItem(DEFAULT_ROU_MODEL_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseContractTitle").value(hasItem(DEFAULT_LEASE_CONTRACT_TITLE)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accruedDepreciationAccountNumber").value(hasItem(DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].leaseContractSerialNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER)));

        // Check, that the count call also returns 1
        restRouAssetListReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouAssetListReportItemShouldNotBeFound(String filter) throws Exception {
        restRouAssetListReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouAssetListReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouAssetListReportItem() throws Exception {
        // Get the rouAssetListReportItem
        restRouAssetListReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchRouAssetListReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouAssetListReportItemRepository.saveAndFlush(rouAssetListReportItem);
        when(mockRouAssetListReportItemSearchRepository.search("id:" + rouAssetListReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouAssetListReportItem), PageRequest.of(0, 1), 1));

        // Search the rouAssetListReportItem
        restRouAssetListReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouAssetListReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetListReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leaseTermPeriods").value(hasItem(DEFAULT_LEASE_TERM_PERIODS)))
            .andExpect(jsonPath("$.[*].rouModelReference").value(hasItem(DEFAULT_ROU_MODEL_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseContractTitle").value(hasItem(DEFAULT_LEASE_CONTRACT_TITLE)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accruedDepreciationAccountNumber").value(hasItem(DEFAULT_ACCRUED_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].leaseContractSerialNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_SERIAL_NUMBER)));
    }
}
