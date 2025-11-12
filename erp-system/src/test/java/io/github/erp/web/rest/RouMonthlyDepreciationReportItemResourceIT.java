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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.RouMonthlyDepreciationReportItem;
import io.github.erp.repository.RouMonthlyDepreciationReportItemRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportItemSearchRepository;
import io.github.erp.service.criteria.RouMonthlyDepreciationReportItemCriteria;
import io.github.erp.service.dto.RouMonthlyDepreciationReportItemDTO;
import io.github.erp.service.mapper.RouMonthlyDepreciationReportItemMapper;
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
 * Integration tests for the {@link RouMonthlyDepreciationReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouMonthlyDepreciationReportItemResourceIT {

    private static final LocalDate DEFAULT_FISCAL_MONTH_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_MONTH_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FISCAL_MONTH_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FISCAL_MONTH_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_MONTH_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FISCAL_MONTH_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TOTAL_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/rou-monthly-depreciation-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-monthly-depreciation-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouMonthlyDepreciationReportItemRepository rouMonthlyDepreciationReportItemRepository;

    @Autowired
    private RouMonthlyDepreciationReportItemMapper rouMonthlyDepreciationReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouMonthlyDepreciationReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouMonthlyDepreciationReportItemSearchRepository mockRouMonthlyDepreciationReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouMonthlyDepreciationReportItemMockMvc;

    private RouMonthlyDepreciationReportItem rouMonthlyDepreciationReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouMonthlyDepreciationReportItem createEntity(EntityManager em) {
        RouMonthlyDepreciationReportItem rouMonthlyDepreciationReportItem = new RouMonthlyDepreciationReportItem()
            .fiscalMonthStartDate(DEFAULT_FISCAL_MONTH_START_DATE)
            .fiscalMonthEndDate(DEFAULT_FISCAL_MONTH_END_DATE)
            .totalDepreciationAmount(DEFAULT_TOTAL_DEPRECIATION_AMOUNT);
        return rouMonthlyDepreciationReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouMonthlyDepreciationReportItem createUpdatedEntity(EntityManager em) {
        RouMonthlyDepreciationReportItem rouMonthlyDepreciationReportItem = new RouMonthlyDepreciationReportItem()
            .fiscalMonthStartDate(UPDATED_FISCAL_MONTH_START_DATE)
            .fiscalMonthEndDate(UPDATED_FISCAL_MONTH_END_DATE)
            .totalDepreciationAmount(UPDATED_TOTAL_DEPRECIATION_AMOUNT);
        return rouMonthlyDepreciationReportItem;
    }

    @BeforeEach
    public void initTest() {
        rouMonthlyDepreciationReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void createRouMonthlyDepreciationReportItem() throws Exception {
        int databaseSizeBeforeCreate = rouMonthlyDepreciationReportItemRepository.findAll().size();
        // Create the RouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeCreate + 1);
        RouMonthlyDepreciationReportItem testRouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItemList.get(
            rouMonthlyDepreciationReportItemList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthStartDate()).isEqualTo(DEFAULT_FISCAL_MONTH_START_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthEndDate()).isEqualTo(DEFAULT_FISCAL_MONTH_END_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getTotalDepreciationAmount())
            .isEqualByComparingTo(DEFAULT_TOTAL_DEPRECIATION_AMOUNT);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(1)).save(testRouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void createRouMonthlyDepreciationReportItemWithExistingId() throws Exception {
        // Create the RouMonthlyDepreciationReportItem with an existing ID
        rouMonthlyDepreciationReportItem.setId(1L);
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );

        int databaseSizeBeforeCreate = rouMonthlyDepreciationReportItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(0)).save(rouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItems() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouMonthlyDepreciationReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalMonthStartDate").value(hasItem(DEFAULT_FISCAL_MONTH_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalDepreciationAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_DEPRECIATION_AMOUNT))));
    }

    @Test
    @Transactional
    void getRouMonthlyDepreciationReportItem() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get the rouMonthlyDepreciationReportItem
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, rouMonthlyDepreciationReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouMonthlyDepreciationReportItem.getId().intValue()))
            .andExpect(jsonPath("$.fiscalMonthStartDate").value(DEFAULT_FISCAL_MONTH_START_DATE.toString()))
            .andExpect(jsonPath("$.fiscalMonthEndDate").value(DEFAULT_FISCAL_MONTH_END_DATE.toString()))
            .andExpect(jsonPath("$.totalDepreciationAmount").value(sameNumber(DEFAULT_TOTAL_DEPRECIATION_AMOUNT)));
    }

    @Test
    @Transactional
    void getRouMonthlyDepreciationReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        Long id = rouMonthlyDepreciationReportItem.getId();

        defaultRouMonthlyDepreciationReportItemShouldBeFound("id.equals=" + id);
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultRouMonthlyDepreciationReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultRouMonthlyDepreciationReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate equals to DEFAULT_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthStartDate.equals=" + DEFAULT_FISCAL_MONTH_START_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate equals to UPDATED_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthStartDate.equals=" + UPDATED_FISCAL_MONTH_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate not equals to DEFAULT_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthStartDate.notEquals=" + DEFAULT_FISCAL_MONTH_START_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate not equals to UPDATED_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthStartDate.notEquals=" + UPDATED_FISCAL_MONTH_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate in DEFAULT_FISCAL_MONTH_START_DATE or UPDATED_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound(
            "fiscalMonthStartDate.in=" + DEFAULT_FISCAL_MONTH_START_DATE + "," + UPDATED_FISCAL_MONTH_START_DATE
        );

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate equals to UPDATED_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthStartDate.in=" + UPDATED_FISCAL_MONTH_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is not null
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthStartDate.specified=true");

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is null
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is greater than or equal to DEFAULT_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthStartDate.greaterThanOrEqual=" + DEFAULT_FISCAL_MONTH_START_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is greater than or equal to UPDATED_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound(
            "fiscalMonthStartDate.greaterThanOrEqual=" + UPDATED_FISCAL_MONTH_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is less than or equal to DEFAULT_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthStartDate.lessThanOrEqual=" + DEFAULT_FISCAL_MONTH_START_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is less than or equal to SMALLER_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthStartDate.lessThanOrEqual=" + SMALLER_FISCAL_MONTH_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is less than DEFAULT_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthStartDate.lessThan=" + DEFAULT_FISCAL_MONTH_START_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is less than UPDATED_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthStartDate.lessThan=" + UPDATED_FISCAL_MONTH_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is greater than DEFAULT_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthStartDate.greaterThan=" + DEFAULT_FISCAL_MONTH_START_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthStartDate is greater than SMALLER_FISCAL_MONTH_START_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthStartDate.greaterThan=" + SMALLER_FISCAL_MONTH_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate equals to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthEndDate.equals=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.equals=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate not equals to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.notEquals=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate not equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthEndDate.notEquals=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate in DEFAULT_FISCAL_MONTH_END_DATE or UPDATED_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound(
            "fiscalMonthEndDate.in=" + DEFAULT_FISCAL_MONTH_END_DATE + "," + UPDATED_FISCAL_MONTH_END_DATE
        );

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.in=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is not null
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthEndDate.specified=true");

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is null
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is greater than or equal to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthEndDate.greaterThanOrEqual=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is greater than or equal to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.greaterThanOrEqual=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is less than or equal to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthEndDate.lessThanOrEqual=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is less than or equal to SMALLER_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.lessThanOrEqual=" + SMALLER_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is less than DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.lessThan=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is less than UPDATED_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthEndDate.lessThan=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByFiscalMonthEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is greater than DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("fiscalMonthEndDate.greaterThan=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouMonthlyDepreciationReportItemList where fiscalMonthEndDate is greater than SMALLER_FISCAL_MONTH_END_DATE
        defaultRouMonthlyDepreciationReportItemShouldBeFound("fiscalMonthEndDate.greaterThan=" + SMALLER_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount equals to DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldBeFound("totalDepreciationAmount.equals=" + DEFAULT_TOTAL_DEPRECIATION_AMOUNT);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount equals to UPDATED_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("totalDepreciationAmount.equals=" + UPDATED_TOTAL_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount not equals to DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("totalDepreciationAmount.notEquals=" + DEFAULT_TOTAL_DEPRECIATION_AMOUNT);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount not equals to UPDATED_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldBeFound("totalDepreciationAmount.notEquals=" + UPDATED_TOTAL_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount in DEFAULT_TOTAL_DEPRECIATION_AMOUNT or UPDATED_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldBeFound(
            "totalDepreciationAmount.in=" + DEFAULT_TOTAL_DEPRECIATION_AMOUNT + "," + UPDATED_TOTAL_DEPRECIATION_AMOUNT
        );

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount equals to UPDATED_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("totalDepreciationAmount.in=" + UPDATED_TOTAL_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is not null
        defaultRouMonthlyDepreciationReportItemShouldBeFound("totalDepreciationAmount.specified=true");

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is null
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("totalDepreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is greater than or equal to DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldBeFound(
            "totalDepreciationAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        );

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is greater than or equal to UPDATED_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound(
            "totalDepreciationAmount.greaterThanOrEqual=" + UPDATED_TOTAL_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is less than or equal to DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldBeFound(
            "totalDepreciationAmount.lessThanOrEqual=" + DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        );

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is less than or equal to SMALLER_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound(
            "totalDepreciationAmount.lessThanOrEqual=" + SMALLER_TOTAL_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is less than DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("totalDepreciationAmount.lessThan=" + DEFAULT_TOTAL_DEPRECIATION_AMOUNT);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is less than UPDATED_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldBeFound("totalDepreciationAmount.lessThan=" + UPDATED_TOTAL_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportItemsByTotalDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is greater than DEFAULT_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldNotBeFound("totalDepreciationAmount.greaterThan=" + DEFAULT_TOTAL_DEPRECIATION_AMOUNT);

        // Get all the rouMonthlyDepreciationReportItemList where totalDepreciationAmount is greater than SMALLER_TOTAL_DEPRECIATION_AMOUNT
        defaultRouMonthlyDepreciationReportItemShouldBeFound("totalDepreciationAmount.greaterThan=" + SMALLER_TOTAL_DEPRECIATION_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouMonthlyDepreciationReportItemShouldBeFound(String filter) throws Exception {
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouMonthlyDepreciationReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalMonthStartDate").value(hasItem(DEFAULT_FISCAL_MONTH_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalDepreciationAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_DEPRECIATION_AMOUNT))));

        // Check, that the count call also returns 1
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouMonthlyDepreciationReportItemShouldNotBeFound(String filter) throws Exception {
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouMonthlyDepreciationReportItem() throws Exception {
        // Get the rouMonthlyDepreciationReportItem
        restRouMonthlyDepreciationReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouMonthlyDepreciationReportItem() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();

        // Update the rouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItem updatedRouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItemRepository
            .findById(rouMonthlyDepreciationReportItem.getId())
            .get();
        // Disconnect from session so that the updates on updatedRouMonthlyDepreciationReportItem are not directly saved in db
        em.detach(updatedRouMonthlyDepreciationReportItem);
        updatedRouMonthlyDepreciationReportItem
            .fiscalMonthStartDate(UPDATED_FISCAL_MONTH_START_DATE)
            .fiscalMonthEndDate(UPDATED_FISCAL_MONTH_END_DATE)
            .totalDepreciationAmount(UPDATED_TOTAL_DEPRECIATION_AMOUNT);
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            updatedRouMonthlyDepreciationReportItem
        );

        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouMonthlyDepreciationReportItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);
        RouMonthlyDepreciationReportItem testRouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItemList.get(
            rouMonthlyDepreciationReportItemList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthStartDate()).isEqualTo(UPDATED_FISCAL_MONTH_START_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthEndDate()).isEqualTo(UPDATED_FISCAL_MONTH_END_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getTotalDepreciationAmount()).isEqualTo(UPDATED_TOTAL_DEPRECIATION_AMOUNT);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository).save(testRouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void putNonExistingRouMonthlyDepreciationReportItem() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();
        rouMonthlyDepreciationReportItem.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouMonthlyDepreciationReportItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(0)).save(rouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouMonthlyDepreciationReportItem() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();
        rouMonthlyDepreciationReportItem.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(0)).save(rouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouMonthlyDepreciationReportItem() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();
        rouMonthlyDepreciationReportItem.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(0)).save(rouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void partialUpdateRouMonthlyDepreciationReportItemWithPatch() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();

        // Update the rouMonthlyDepreciationReportItem using partial update
        RouMonthlyDepreciationReportItem partialUpdatedRouMonthlyDepreciationReportItem = new RouMonthlyDepreciationReportItem();
        partialUpdatedRouMonthlyDepreciationReportItem.setId(rouMonthlyDepreciationReportItem.getId());

        partialUpdatedRouMonthlyDepreciationReportItem
            .fiscalMonthStartDate(UPDATED_FISCAL_MONTH_START_DATE)
            .fiscalMonthEndDate(UPDATED_FISCAL_MONTH_END_DATE)
            .totalDepreciationAmount(UPDATED_TOTAL_DEPRECIATION_AMOUNT);

        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouMonthlyDepreciationReportItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouMonthlyDepreciationReportItem))
            )
            .andExpect(status().isOk());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);
        RouMonthlyDepreciationReportItem testRouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItemList.get(
            rouMonthlyDepreciationReportItemList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthStartDate()).isEqualTo(UPDATED_FISCAL_MONTH_START_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthEndDate()).isEqualTo(UPDATED_FISCAL_MONTH_END_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getTotalDepreciationAmount())
            .isEqualByComparingTo(UPDATED_TOTAL_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateRouMonthlyDepreciationReportItemWithPatch() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();

        // Update the rouMonthlyDepreciationReportItem using partial update
        RouMonthlyDepreciationReportItem partialUpdatedRouMonthlyDepreciationReportItem = new RouMonthlyDepreciationReportItem();
        partialUpdatedRouMonthlyDepreciationReportItem.setId(rouMonthlyDepreciationReportItem.getId());

        partialUpdatedRouMonthlyDepreciationReportItem
            .fiscalMonthStartDate(UPDATED_FISCAL_MONTH_START_DATE)
            .fiscalMonthEndDate(UPDATED_FISCAL_MONTH_END_DATE)
            .totalDepreciationAmount(UPDATED_TOTAL_DEPRECIATION_AMOUNT);

        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouMonthlyDepreciationReportItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouMonthlyDepreciationReportItem))
            )
            .andExpect(status().isOk());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);
        RouMonthlyDepreciationReportItem testRouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItemList.get(
            rouMonthlyDepreciationReportItemList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthStartDate()).isEqualTo(UPDATED_FISCAL_MONTH_START_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getFiscalMonthEndDate()).isEqualTo(UPDATED_FISCAL_MONTH_END_DATE);
        assertThat(testRouMonthlyDepreciationReportItem.getTotalDepreciationAmount())
            .isEqualByComparingTo(UPDATED_TOTAL_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingRouMonthlyDepreciationReportItem() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();
        rouMonthlyDepreciationReportItem.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouMonthlyDepreciationReportItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(0)).save(rouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouMonthlyDepreciationReportItem() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();
        rouMonthlyDepreciationReportItem.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(0)).save(rouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouMonthlyDepreciationReportItem() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportItemRepository.findAll().size();
        rouMonthlyDepreciationReportItem.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReportItem
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemMapper.toDto(
            rouMonthlyDepreciationReportItem
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouMonthlyDepreciationReportItem in the database
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(0)).save(rouMonthlyDepreciationReportItem);
    }

    @Test
    @Transactional
    void deleteRouMonthlyDepreciationReportItem() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);

        int databaseSizeBeforeDelete = rouMonthlyDepreciationReportItemRepository.findAll().size();

        // Delete the rouMonthlyDepreciationReportItem
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouMonthlyDepreciationReportItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouMonthlyDepreciationReportItem> rouMonthlyDepreciationReportItemList = rouMonthlyDepreciationReportItemRepository.findAll();
        assertThat(rouMonthlyDepreciationReportItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouMonthlyDepreciationReportItem in Elasticsearch
        verify(mockRouMonthlyDepreciationReportItemSearchRepository, times(1)).deleteById(rouMonthlyDepreciationReportItem.getId());
    }

    @Test
    @Transactional
    void searchRouMonthlyDepreciationReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouMonthlyDepreciationReportItemRepository.saveAndFlush(rouMonthlyDepreciationReportItem);
        when(
            mockRouMonthlyDepreciationReportItemSearchRepository.search(
                "id:" + rouMonthlyDepreciationReportItem.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(rouMonthlyDepreciationReportItem), PageRequest.of(0, 1), 1));

        // Search the rouMonthlyDepreciationReportItem
        restRouMonthlyDepreciationReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouMonthlyDepreciationReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouMonthlyDepreciationReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalMonthStartDate").value(hasItem(DEFAULT_FISCAL_MONTH_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalDepreciationAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_DEPRECIATION_AMOUNT))));
    }
}
