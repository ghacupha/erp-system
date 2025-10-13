package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.WIPTransferListItem;
import io.github.erp.repository.WIPTransferListItemRepository;
import io.github.erp.repository.search.WIPTransferListItemSearchRepository;
import io.github.erp.service.criteria.WIPTransferListItemCriteria;
import io.github.erp.service.dto.WIPTransferListItemDTO;
import io.github.erp.service.mapper.WIPTransferListItemMapper;
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
 * Integration tests for the {@link WIPTransferListItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WIPTransferListItemResourceIT {

    private static final Long DEFAULT_WIP_SEQUENCE = 1L;
    private static final Long UPDATED_WIP_SEQUENCE = 2L;
    private static final Long SMALLER_WIP_SEQUENCE = 1L - 1L;

    private static final String DEFAULT_WIP_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_WIP_PARTICULARS = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSFER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSFER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSFER_SETTLEMENT = "AAAAAAAAAA";
    private static final String UPDATED_TRANSFER_SETTLEMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSFER_SETTLEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSFER_SETTLEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSFER_SETTLEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TRANSFER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSFER_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSFER_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_WIP_TRANSFER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WIP_TRANSFER_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_WIP_TRANSFER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ORIGINAL_SETTLEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_SETTLEMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ORIGINAL_SETTLEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORIGINAL_SETTLEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ORIGINAL_SETTLEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET = "BBBBBBBBBB";

    private static final String DEFAULT_WORK_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_WORK_PROJECT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wip-transfer-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/wip-transfer-list-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WIPTransferListItemRepository wIPTransferListItemRepository;

    @Autowired
    private WIPTransferListItemMapper wIPTransferListItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WIPTransferListItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private WIPTransferListItemSearchRepository mockWIPTransferListItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWIPTransferListItemMockMvc;

    private WIPTransferListItem wIPTransferListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPTransferListItem createEntity(EntityManager em) {
        WIPTransferListItem wIPTransferListItem = new WIPTransferListItem()
            .wipSequence(DEFAULT_WIP_SEQUENCE)
            .wipParticulars(DEFAULT_WIP_PARTICULARS)
            .transferType(DEFAULT_TRANSFER_TYPE)
            .transferSettlement(DEFAULT_TRANSFER_SETTLEMENT)
            .transferSettlementDate(DEFAULT_TRANSFER_SETTLEMENT_DATE)
            .transferAmount(DEFAULT_TRANSFER_AMOUNT)
            .wipTransferDate(DEFAULT_WIP_TRANSFER_DATE)
            .originalSettlement(DEFAULT_ORIGINAL_SETTLEMENT)
            .originalSettlementDate(DEFAULT_ORIGINAL_SETTLEMENT_DATE)
            .assetCategory(DEFAULT_ASSET_CATEGORY)
            .serviceOutlet(DEFAULT_SERVICE_OUTLET)
            .workProject(DEFAULT_WORK_PROJECT);
        return wIPTransferListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPTransferListItem createUpdatedEntity(EntityManager em) {
        WIPTransferListItem wIPTransferListItem = new WIPTransferListItem()
            .wipSequence(UPDATED_WIP_SEQUENCE)
            .wipParticulars(UPDATED_WIP_PARTICULARS)
            .transferType(UPDATED_TRANSFER_TYPE)
            .transferSettlement(UPDATED_TRANSFER_SETTLEMENT)
            .transferSettlementDate(UPDATED_TRANSFER_SETTLEMENT_DATE)
            .transferAmount(UPDATED_TRANSFER_AMOUNT)
            .wipTransferDate(UPDATED_WIP_TRANSFER_DATE)
            .originalSettlement(UPDATED_ORIGINAL_SETTLEMENT)
            .originalSettlementDate(UPDATED_ORIGINAL_SETTLEMENT_DATE)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .serviceOutlet(UPDATED_SERVICE_OUTLET)
            .workProject(UPDATED_WORK_PROJECT);
        return wIPTransferListItem;
    }

    @BeforeEach
    public void initTest() {
        wIPTransferListItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItems() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList
        restWIPTransferListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPTransferListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].wipSequence").value(hasItem(DEFAULT_WIP_SEQUENCE.intValue())))
            .andExpect(jsonPath("$.[*].wipParticulars").value(hasItem(DEFAULT_WIP_PARTICULARS)))
            .andExpect(jsonPath("$.[*].transferType").value(hasItem(DEFAULT_TRANSFER_TYPE)))
            .andExpect(jsonPath("$.[*].transferSettlement").value(hasItem(DEFAULT_TRANSFER_SETTLEMENT)))
            .andExpect(jsonPath("$.[*].transferSettlementDate").value(hasItem(DEFAULT_TRANSFER_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(sameNumber(DEFAULT_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].wipTransferDate").value(hasItem(DEFAULT_WIP_TRANSFER_DATE.toString())))
            .andExpect(jsonPath("$.[*].originalSettlement").value(hasItem(DEFAULT_ORIGINAL_SETTLEMENT)))
            .andExpect(jsonPath("$.[*].originalSettlementDate").value(hasItem(DEFAULT_ORIGINAL_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET)))
            .andExpect(jsonPath("$.[*].workProject").value(hasItem(DEFAULT_WORK_PROJECT)));
    }

    @Test
    @Transactional
    void getWIPTransferListItem() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get the wIPTransferListItem
        restWIPTransferListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, wIPTransferListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wIPTransferListItem.getId().intValue()))
            .andExpect(jsonPath("$.wipSequence").value(DEFAULT_WIP_SEQUENCE.intValue()))
            .andExpect(jsonPath("$.wipParticulars").value(DEFAULT_WIP_PARTICULARS))
            .andExpect(jsonPath("$.transferType").value(DEFAULT_TRANSFER_TYPE))
            .andExpect(jsonPath("$.transferSettlement").value(DEFAULT_TRANSFER_SETTLEMENT))
            .andExpect(jsonPath("$.transferSettlementDate").value(DEFAULT_TRANSFER_SETTLEMENT_DATE.toString()))
            .andExpect(jsonPath("$.transferAmount").value(sameNumber(DEFAULT_TRANSFER_AMOUNT)))
            .andExpect(jsonPath("$.wipTransferDate").value(DEFAULT_WIP_TRANSFER_DATE.toString()))
            .andExpect(jsonPath("$.originalSettlement").value(DEFAULT_ORIGINAL_SETTLEMENT))
            .andExpect(jsonPath("$.originalSettlementDate").value(DEFAULT_ORIGINAL_SETTLEMENT_DATE.toString()))
            .andExpect(jsonPath("$.assetCategory").value(DEFAULT_ASSET_CATEGORY))
            .andExpect(jsonPath("$.serviceOutlet").value(DEFAULT_SERVICE_OUTLET))
            .andExpect(jsonPath("$.workProject").value(DEFAULT_WORK_PROJECT));
    }

    @Test
    @Transactional
    void getWIPTransferListItemsByIdFiltering() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        Long id = wIPTransferListItem.getId();

        defaultWIPTransferListItemShouldBeFound("id.equals=" + id);
        defaultWIPTransferListItemShouldNotBeFound("id.notEquals=" + id);

        defaultWIPTransferListItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWIPTransferListItemShouldNotBeFound("id.greaterThan=" + id);

        defaultWIPTransferListItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWIPTransferListItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence equals to DEFAULT_WIP_SEQUENCE
        defaultWIPTransferListItemShouldBeFound("wipSequence.equals=" + DEFAULT_WIP_SEQUENCE);

        // Get all the wIPTransferListItemList where wipSequence equals to UPDATED_WIP_SEQUENCE
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.equals=" + UPDATED_WIP_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence not equals to DEFAULT_WIP_SEQUENCE
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.notEquals=" + DEFAULT_WIP_SEQUENCE);

        // Get all the wIPTransferListItemList where wipSequence not equals to UPDATED_WIP_SEQUENCE
        defaultWIPTransferListItemShouldBeFound("wipSequence.notEquals=" + UPDATED_WIP_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence in DEFAULT_WIP_SEQUENCE or UPDATED_WIP_SEQUENCE
        defaultWIPTransferListItemShouldBeFound("wipSequence.in=" + DEFAULT_WIP_SEQUENCE + "," + UPDATED_WIP_SEQUENCE);

        // Get all the wIPTransferListItemList where wipSequence equals to UPDATED_WIP_SEQUENCE
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.in=" + UPDATED_WIP_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence is not null
        defaultWIPTransferListItemShouldBeFound("wipSequence.specified=true");

        // Get all the wIPTransferListItemList where wipSequence is null
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence is greater than or equal to DEFAULT_WIP_SEQUENCE
        defaultWIPTransferListItemShouldBeFound("wipSequence.greaterThanOrEqual=" + DEFAULT_WIP_SEQUENCE);

        // Get all the wIPTransferListItemList where wipSequence is greater than or equal to UPDATED_WIP_SEQUENCE
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.greaterThanOrEqual=" + UPDATED_WIP_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence is less than or equal to DEFAULT_WIP_SEQUENCE
        defaultWIPTransferListItemShouldBeFound("wipSequence.lessThanOrEqual=" + DEFAULT_WIP_SEQUENCE);

        // Get all the wIPTransferListItemList where wipSequence is less than or equal to SMALLER_WIP_SEQUENCE
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.lessThanOrEqual=" + SMALLER_WIP_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence is less than DEFAULT_WIP_SEQUENCE
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.lessThan=" + DEFAULT_WIP_SEQUENCE);

        // Get all the wIPTransferListItemList where wipSequence is less than UPDATED_WIP_SEQUENCE
        defaultWIPTransferListItemShouldBeFound("wipSequence.lessThan=" + UPDATED_WIP_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipSequenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipSequence is greater than DEFAULT_WIP_SEQUENCE
        defaultWIPTransferListItemShouldNotBeFound("wipSequence.greaterThan=" + DEFAULT_WIP_SEQUENCE);

        // Get all the wIPTransferListItemList where wipSequence is greater than SMALLER_WIP_SEQUENCE
        defaultWIPTransferListItemShouldBeFound("wipSequence.greaterThan=" + SMALLER_WIP_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipParticulars equals to DEFAULT_WIP_PARTICULARS
        defaultWIPTransferListItemShouldBeFound("wipParticulars.equals=" + DEFAULT_WIP_PARTICULARS);

        // Get all the wIPTransferListItemList where wipParticulars equals to UPDATED_WIP_PARTICULARS
        defaultWIPTransferListItemShouldNotBeFound("wipParticulars.equals=" + UPDATED_WIP_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipParticulars not equals to DEFAULT_WIP_PARTICULARS
        defaultWIPTransferListItemShouldNotBeFound("wipParticulars.notEquals=" + DEFAULT_WIP_PARTICULARS);

        // Get all the wIPTransferListItemList where wipParticulars not equals to UPDATED_WIP_PARTICULARS
        defaultWIPTransferListItemShouldBeFound("wipParticulars.notEquals=" + UPDATED_WIP_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipParticulars in DEFAULT_WIP_PARTICULARS or UPDATED_WIP_PARTICULARS
        defaultWIPTransferListItemShouldBeFound("wipParticulars.in=" + DEFAULT_WIP_PARTICULARS + "," + UPDATED_WIP_PARTICULARS);

        // Get all the wIPTransferListItemList where wipParticulars equals to UPDATED_WIP_PARTICULARS
        defaultWIPTransferListItemShouldNotBeFound("wipParticulars.in=" + UPDATED_WIP_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipParticulars is not null
        defaultWIPTransferListItemShouldBeFound("wipParticulars.specified=true");

        // Get all the wIPTransferListItemList where wipParticulars is null
        defaultWIPTransferListItemShouldNotBeFound("wipParticulars.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipParticularsContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipParticulars contains DEFAULT_WIP_PARTICULARS
        defaultWIPTransferListItemShouldBeFound("wipParticulars.contains=" + DEFAULT_WIP_PARTICULARS);

        // Get all the wIPTransferListItemList where wipParticulars contains UPDATED_WIP_PARTICULARS
        defaultWIPTransferListItemShouldNotBeFound("wipParticulars.contains=" + UPDATED_WIP_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipParticulars does not contain DEFAULT_WIP_PARTICULARS
        defaultWIPTransferListItemShouldNotBeFound("wipParticulars.doesNotContain=" + DEFAULT_WIP_PARTICULARS);

        // Get all the wIPTransferListItemList where wipParticulars does not contain UPDATED_WIP_PARTICULARS
        defaultWIPTransferListItemShouldBeFound("wipParticulars.doesNotContain=" + UPDATED_WIP_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferType equals to DEFAULT_TRANSFER_TYPE
        defaultWIPTransferListItemShouldBeFound("transferType.equals=" + DEFAULT_TRANSFER_TYPE);

        // Get all the wIPTransferListItemList where transferType equals to UPDATED_TRANSFER_TYPE
        defaultWIPTransferListItemShouldNotBeFound("transferType.equals=" + UPDATED_TRANSFER_TYPE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferType not equals to DEFAULT_TRANSFER_TYPE
        defaultWIPTransferListItemShouldNotBeFound("transferType.notEquals=" + DEFAULT_TRANSFER_TYPE);

        // Get all the wIPTransferListItemList where transferType not equals to UPDATED_TRANSFER_TYPE
        defaultWIPTransferListItemShouldBeFound("transferType.notEquals=" + UPDATED_TRANSFER_TYPE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferTypeIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferType in DEFAULT_TRANSFER_TYPE or UPDATED_TRANSFER_TYPE
        defaultWIPTransferListItemShouldBeFound("transferType.in=" + DEFAULT_TRANSFER_TYPE + "," + UPDATED_TRANSFER_TYPE);

        // Get all the wIPTransferListItemList where transferType equals to UPDATED_TRANSFER_TYPE
        defaultWIPTransferListItemShouldNotBeFound("transferType.in=" + UPDATED_TRANSFER_TYPE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferType is not null
        defaultWIPTransferListItemShouldBeFound("transferType.specified=true");

        // Get all the wIPTransferListItemList where transferType is null
        defaultWIPTransferListItemShouldNotBeFound("transferType.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferTypeContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferType contains DEFAULT_TRANSFER_TYPE
        defaultWIPTransferListItemShouldBeFound("transferType.contains=" + DEFAULT_TRANSFER_TYPE);

        // Get all the wIPTransferListItemList where transferType contains UPDATED_TRANSFER_TYPE
        defaultWIPTransferListItemShouldNotBeFound("transferType.contains=" + UPDATED_TRANSFER_TYPE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferTypeNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferType does not contain DEFAULT_TRANSFER_TYPE
        defaultWIPTransferListItemShouldNotBeFound("transferType.doesNotContain=" + DEFAULT_TRANSFER_TYPE);

        // Get all the wIPTransferListItemList where transferType does not contain UPDATED_TRANSFER_TYPE
        defaultWIPTransferListItemShouldBeFound("transferType.doesNotContain=" + UPDATED_TRANSFER_TYPE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlement equals to DEFAULT_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("transferSettlement.equals=" + DEFAULT_TRANSFER_SETTLEMENT);

        // Get all the wIPTransferListItemList where transferSettlement equals to UPDATED_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("transferSettlement.equals=" + UPDATED_TRANSFER_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlement not equals to DEFAULT_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("transferSettlement.notEquals=" + DEFAULT_TRANSFER_SETTLEMENT);

        // Get all the wIPTransferListItemList where transferSettlement not equals to UPDATED_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("transferSettlement.notEquals=" + UPDATED_TRANSFER_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlement in DEFAULT_TRANSFER_SETTLEMENT or UPDATED_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("transferSettlement.in=" + DEFAULT_TRANSFER_SETTLEMENT + "," + UPDATED_TRANSFER_SETTLEMENT);

        // Get all the wIPTransferListItemList where transferSettlement equals to UPDATED_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("transferSettlement.in=" + UPDATED_TRANSFER_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlement is not null
        defaultWIPTransferListItemShouldBeFound("transferSettlement.specified=true");

        // Get all the wIPTransferListItemList where transferSettlement is null
        defaultWIPTransferListItemShouldNotBeFound("transferSettlement.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlement contains DEFAULT_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("transferSettlement.contains=" + DEFAULT_TRANSFER_SETTLEMENT);

        // Get all the wIPTransferListItemList where transferSettlement contains UPDATED_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("transferSettlement.contains=" + UPDATED_TRANSFER_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlement does not contain DEFAULT_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("transferSettlement.doesNotContain=" + DEFAULT_TRANSFER_SETTLEMENT);

        // Get all the wIPTransferListItemList where transferSettlement does not contain UPDATED_TRANSFER_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("transferSettlement.doesNotContain=" + UPDATED_TRANSFER_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate equals to DEFAULT_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("transferSettlementDate.equals=" + DEFAULT_TRANSFER_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where transferSettlementDate equals to UPDATED_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.equals=" + UPDATED_TRANSFER_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate not equals to DEFAULT_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.notEquals=" + DEFAULT_TRANSFER_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where transferSettlementDate not equals to UPDATED_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("transferSettlementDate.notEquals=" + UPDATED_TRANSFER_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate in DEFAULT_TRANSFER_SETTLEMENT_DATE or UPDATED_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound(
            "transferSettlementDate.in=" + DEFAULT_TRANSFER_SETTLEMENT_DATE + "," + UPDATED_TRANSFER_SETTLEMENT_DATE
        );

        // Get all the wIPTransferListItemList where transferSettlementDate equals to UPDATED_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.in=" + UPDATED_TRANSFER_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate is not null
        defaultWIPTransferListItemShouldBeFound("transferSettlementDate.specified=true");

        // Get all the wIPTransferListItemList where transferSettlementDate is null
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate is greater than or equal to DEFAULT_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("transferSettlementDate.greaterThanOrEqual=" + DEFAULT_TRANSFER_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where transferSettlementDate is greater than or equal to UPDATED_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.greaterThanOrEqual=" + UPDATED_TRANSFER_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate is less than or equal to DEFAULT_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("transferSettlementDate.lessThanOrEqual=" + DEFAULT_TRANSFER_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where transferSettlementDate is less than or equal to SMALLER_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.lessThanOrEqual=" + SMALLER_TRANSFER_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate is less than DEFAULT_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.lessThan=" + DEFAULT_TRANSFER_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where transferSettlementDate is less than UPDATED_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("transferSettlementDate.lessThan=" + UPDATED_TRANSFER_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferSettlementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferSettlementDate is greater than DEFAULT_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("transferSettlementDate.greaterThan=" + DEFAULT_TRANSFER_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where transferSettlementDate is greater than SMALLER_TRANSFER_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("transferSettlementDate.greaterThan=" + SMALLER_TRANSFER_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount equals to DEFAULT_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldBeFound("transferAmount.equals=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the wIPTransferListItemList where transferAmount equals to UPDATED_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.equals=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount not equals to DEFAULT_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.notEquals=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the wIPTransferListItemList where transferAmount not equals to UPDATED_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldBeFound("transferAmount.notEquals=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount in DEFAULT_TRANSFER_AMOUNT or UPDATED_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldBeFound("transferAmount.in=" + DEFAULT_TRANSFER_AMOUNT + "," + UPDATED_TRANSFER_AMOUNT);

        // Get all the wIPTransferListItemList where transferAmount equals to UPDATED_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.in=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount is not null
        defaultWIPTransferListItemShouldBeFound("transferAmount.specified=true");

        // Get all the wIPTransferListItemList where transferAmount is null
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount is greater than or equal to DEFAULT_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldBeFound("transferAmount.greaterThanOrEqual=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the wIPTransferListItemList where transferAmount is greater than or equal to UPDATED_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.greaterThanOrEqual=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount is less than or equal to DEFAULT_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldBeFound("transferAmount.lessThanOrEqual=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the wIPTransferListItemList where transferAmount is less than or equal to SMALLER_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.lessThanOrEqual=" + SMALLER_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount is less than DEFAULT_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.lessThan=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the wIPTransferListItemList where transferAmount is less than UPDATED_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldBeFound("transferAmount.lessThan=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByTransferAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where transferAmount is greater than DEFAULT_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldNotBeFound("transferAmount.greaterThan=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the wIPTransferListItemList where transferAmount is greater than SMALLER_TRANSFER_AMOUNT
        defaultWIPTransferListItemShouldBeFound("transferAmount.greaterThan=" + SMALLER_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate equals to DEFAULT_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.equals=" + DEFAULT_WIP_TRANSFER_DATE);

        // Get all the wIPTransferListItemList where wipTransferDate equals to UPDATED_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.equals=" + UPDATED_WIP_TRANSFER_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate not equals to DEFAULT_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.notEquals=" + DEFAULT_WIP_TRANSFER_DATE);

        // Get all the wIPTransferListItemList where wipTransferDate not equals to UPDATED_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.notEquals=" + UPDATED_WIP_TRANSFER_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate in DEFAULT_WIP_TRANSFER_DATE or UPDATED_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.in=" + DEFAULT_WIP_TRANSFER_DATE + "," + UPDATED_WIP_TRANSFER_DATE);

        // Get all the wIPTransferListItemList where wipTransferDate equals to UPDATED_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.in=" + UPDATED_WIP_TRANSFER_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate is not null
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.specified=true");

        // Get all the wIPTransferListItemList where wipTransferDate is null
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate is greater than or equal to DEFAULT_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.greaterThanOrEqual=" + DEFAULT_WIP_TRANSFER_DATE);

        // Get all the wIPTransferListItemList where wipTransferDate is greater than or equal to UPDATED_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.greaterThanOrEqual=" + UPDATED_WIP_TRANSFER_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate is less than or equal to DEFAULT_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.lessThanOrEqual=" + DEFAULT_WIP_TRANSFER_DATE);

        // Get all the wIPTransferListItemList where wipTransferDate is less than or equal to SMALLER_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.lessThanOrEqual=" + SMALLER_WIP_TRANSFER_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate is less than DEFAULT_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.lessThan=" + DEFAULT_WIP_TRANSFER_DATE);

        // Get all the wIPTransferListItemList where wipTransferDate is less than UPDATED_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.lessThan=" + UPDATED_WIP_TRANSFER_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWipTransferDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where wipTransferDate is greater than DEFAULT_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldNotBeFound("wipTransferDate.greaterThan=" + DEFAULT_WIP_TRANSFER_DATE);

        // Get all the wIPTransferListItemList where wipTransferDate is greater than SMALLER_WIP_TRANSFER_DATE
        defaultWIPTransferListItemShouldBeFound("wipTransferDate.greaterThan=" + SMALLER_WIP_TRANSFER_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlement equals to DEFAULT_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("originalSettlement.equals=" + DEFAULT_ORIGINAL_SETTLEMENT);

        // Get all the wIPTransferListItemList where originalSettlement equals to UPDATED_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("originalSettlement.equals=" + UPDATED_ORIGINAL_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlement not equals to DEFAULT_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("originalSettlement.notEquals=" + DEFAULT_ORIGINAL_SETTLEMENT);

        // Get all the wIPTransferListItemList where originalSettlement not equals to UPDATED_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("originalSettlement.notEquals=" + UPDATED_ORIGINAL_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlement in DEFAULT_ORIGINAL_SETTLEMENT or UPDATED_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("originalSettlement.in=" + DEFAULT_ORIGINAL_SETTLEMENT + "," + UPDATED_ORIGINAL_SETTLEMENT);

        // Get all the wIPTransferListItemList where originalSettlement equals to UPDATED_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("originalSettlement.in=" + UPDATED_ORIGINAL_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlement is not null
        defaultWIPTransferListItemShouldBeFound("originalSettlement.specified=true");

        // Get all the wIPTransferListItemList where originalSettlement is null
        defaultWIPTransferListItemShouldNotBeFound("originalSettlement.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlement contains DEFAULT_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("originalSettlement.contains=" + DEFAULT_ORIGINAL_SETTLEMENT);

        // Get all the wIPTransferListItemList where originalSettlement contains UPDATED_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("originalSettlement.contains=" + UPDATED_ORIGINAL_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlement does not contain DEFAULT_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldNotBeFound("originalSettlement.doesNotContain=" + DEFAULT_ORIGINAL_SETTLEMENT);

        // Get all the wIPTransferListItemList where originalSettlement does not contain UPDATED_ORIGINAL_SETTLEMENT
        defaultWIPTransferListItemShouldBeFound("originalSettlement.doesNotContain=" + UPDATED_ORIGINAL_SETTLEMENT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate equals to DEFAULT_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("originalSettlementDate.equals=" + DEFAULT_ORIGINAL_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where originalSettlementDate equals to UPDATED_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.equals=" + UPDATED_ORIGINAL_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate not equals to DEFAULT_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.notEquals=" + DEFAULT_ORIGINAL_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where originalSettlementDate not equals to UPDATED_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("originalSettlementDate.notEquals=" + UPDATED_ORIGINAL_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate in DEFAULT_ORIGINAL_SETTLEMENT_DATE or UPDATED_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound(
            "originalSettlementDate.in=" + DEFAULT_ORIGINAL_SETTLEMENT_DATE + "," + UPDATED_ORIGINAL_SETTLEMENT_DATE
        );

        // Get all the wIPTransferListItemList where originalSettlementDate equals to UPDATED_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.in=" + UPDATED_ORIGINAL_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate is not null
        defaultWIPTransferListItemShouldBeFound("originalSettlementDate.specified=true");

        // Get all the wIPTransferListItemList where originalSettlementDate is null
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate is greater than or equal to DEFAULT_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("originalSettlementDate.greaterThanOrEqual=" + DEFAULT_ORIGINAL_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where originalSettlementDate is greater than or equal to UPDATED_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.greaterThanOrEqual=" + UPDATED_ORIGINAL_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate is less than or equal to DEFAULT_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("originalSettlementDate.lessThanOrEqual=" + DEFAULT_ORIGINAL_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where originalSettlementDate is less than or equal to SMALLER_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.lessThanOrEqual=" + SMALLER_ORIGINAL_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate is less than DEFAULT_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.lessThan=" + DEFAULT_ORIGINAL_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where originalSettlementDate is less than UPDATED_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("originalSettlementDate.lessThan=" + UPDATED_ORIGINAL_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByOriginalSettlementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where originalSettlementDate is greater than DEFAULT_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldNotBeFound("originalSettlementDate.greaterThan=" + DEFAULT_ORIGINAL_SETTLEMENT_DATE);

        // Get all the wIPTransferListItemList where originalSettlementDate is greater than SMALLER_ORIGINAL_SETTLEMENT_DATE
        defaultWIPTransferListItemShouldBeFound("originalSettlementDate.greaterThan=" + SMALLER_ORIGINAL_SETTLEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where assetCategory equals to DEFAULT_ASSET_CATEGORY
        defaultWIPTransferListItemShouldBeFound("assetCategory.equals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the wIPTransferListItemList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultWIPTransferListItemShouldNotBeFound("assetCategory.equals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByAssetCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where assetCategory not equals to DEFAULT_ASSET_CATEGORY
        defaultWIPTransferListItemShouldNotBeFound("assetCategory.notEquals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the wIPTransferListItemList where assetCategory not equals to UPDATED_ASSET_CATEGORY
        defaultWIPTransferListItemShouldBeFound("assetCategory.notEquals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByAssetCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where assetCategory in DEFAULT_ASSET_CATEGORY or UPDATED_ASSET_CATEGORY
        defaultWIPTransferListItemShouldBeFound("assetCategory.in=" + DEFAULT_ASSET_CATEGORY + "," + UPDATED_ASSET_CATEGORY);

        // Get all the wIPTransferListItemList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultWIPTransferListItemShouldNotBeFound("assetCategory.in=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByAssetCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where assetCategory is not null
        defaultWIPTransferListItemShouldBeFound("assetCategory.specified=true");

        // Get all the wIPTransferListItemList where assetCategory is null
        defaultWIPTransferListItemShouldNotBeFound("assetCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByAssetCategoryContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where assetCategory contains DEFAULT_ASSET_CATEGORY
        defaultWIPTransferListItemShouldBeFound("assetCategory.contains=" + DEFAULT_ASSET_CATEGORY);

        // Get all the wIPTransferListItemList where assetCategory contains UPDATED_ASSET_CATEGORY
        defaultWIPTransferListItemShouldNotBeFound("assetCategory.contains=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByAssetCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where assetCategory does not contain DEFAULT_ASSET_CATEGORY
        defaultWIPTransferListItemShouldNotBeFound("assetCategory.doesNotContain=" + DEFAULT_ASSET_CATEGORY);

        // Get all the wIPTransferListItemList where assetCategory does not contain UPDATED_ASSET_CATEGORY
        defaultWIPTransferListItemShouldBeFound("assetCategory.doesNotContain=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where serviceOutlet equals to DEFAULT_SERVICE_OUTLET
        defaultWIPTransferListItemShouldBeFound("serviceOutlet.equals=" + DEFAULT_SERVICE_OUTLET);

        // Get all the wIPTransferListItemList where serviceOutlet equals to UPDATED_SERVICE_OUTLET
        defaultWIPTransferListItemShouldNotBeFound("serviceOutlet.equals=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByServiceOutletIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where serviceOutlet not equals to DEFAULT_SERVICE_OUTLET
        defaultWIPTransferListItemShouldNotBeFound("serviceOutlet.notEquals=" + DEFAULT_SERVICE_OUTLET);

        // Get all the wIPTransferListItemList where serviceOutlet not equals to UPDATED_SERVICE_OUTLET
        defaultWIPTransferListItemShouldBeFound("serviceOutlet.notEquals=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByServiceOutletIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where serviceOutlet in DEFAULT_SERVICE_OUTLET or UPDATED_SERVICE_OUTLET
        defaultWIPTransferListItemShouldBeFound("serviceOutlet.in=" + DEFAULT_SERVICE_OUTLET + "," + UPDATED_SERVICE_OUTLET);

        // Get all the wIPTransferListItemList where serviceOutlet equals to UPDATED_SERVICE_OUTLET
        defaultWIPTransferListItemShouldNotBeFound("serviceOutlet.in=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByServiceOutletIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where serviceOutlet is not null
        defaultWIPTransferListItemShouldBeFound("serviceOutlet.specified=true");

        // Get all the wIPTransferListItemList where serviceOutlet is null
        defaultWIPTransferListItemShouldNotBeFound("serviceOutlet.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByServiceOutletContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where serviceOutlet contains DEFAULT_SERVICE_OUTLET
        defaultWIPTransferListItemShouldBeFound("serviceOutlet.contains=" + DEFAULT_SERVICE_OUTLET);

        // Get all the wIPTransferListItemList where serviceOutlet contains UPDATED_SERVICE_OUTLET
        defaultWIPTransferListItemShouldNotBeFound("serviceOutlet.contains=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByServiceOutletNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where serviceOutlet does not contain DEFAULT_SERVICE_OUTLET
        defaultWIPTransferListItemShouldNotBeFound("serviceOutlet.doesNotContain=" + DEFAULT_SERVICE_OUTLET);

        // Get all the wIPTransferListItemList where serviceOutlet does not contain UPDATED_SERVICE_OUTLET
        defaultWIPTransferListItemShouldBeFound("serviceOutlet.doesNotContain=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWorkProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where workProject equals to DEFAULT_WORK_PROJECT
        defaultWIPTransferListItemShouldBeFound("workProject.equals=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPTransferListItemList where workProject equals to UPDATED_WORK_PROJECT
        defaultWIPTransferListItemShouldNotBeFound("workProject.equals=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWorkProjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where workProject not equals to DEFAULT_WORK_PROJECT
        defaultWIPTransferListItemShouldNotBeFound("workProject.notEquals=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPTransferListItemList where workProject not equals to UPDATED_WORK_PROJECT
        defaultWIPTransferListItemShouldBeFound("workProject.notEquals=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWorkProjectIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where workProject in DEFAULT_WORK_PROJECT or UPDATED_WORK_PROJECT
        defaultWIPTransferListItemShouldBeFound("workProject.in=" + DEFAULT_WORK_PROJECT + "," + UPDATED_WORK_PROJECT);

        // Get all the wIPTransferListItemList where workProject equals to UPDATED_WORK_PROJECT
        defaultWIPTransferListItemShouldNotBeFound("workProject.in=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWorkProjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where workProject is not null
        defaultWIPTransferListItemShouldBeFound("workProject.specified=true");

        // Get all the wIPTransferListItemList where workProject is null
        defaultWIPTransferListItemShouldNotBeFound("workProject.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWorkProjectContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where workProject contains DEFAULT_WORK_PROJECT
        defaultWIPTransferListItemShouldBeFound("workProject.contains=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPTransferListItemList where workProject contains UPDATED_WORK_PROJECT
        defaultWIPTransferListItemShouldNotBeFound("workProject.contains=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPTransferListItemsByWorkProjectNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);

        // Get all the wIPTransferListItemList where workProject does not contain DEFAULT_WORK_PROJECT
        defaultWIPTransferListItemShouldNotBeFound("workProject.doesNotContain=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPTransferListItemList where workProject does not contain UPDATED_WORK_PROJECT
        defaultWIPTransferListItemShouldBeFound("workProject.doesNotContain=" + UPDATED_WORK_PROJECT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWIPTransferListItemShouldBeFound(String filter) throws Exception {
        restWIPTransferListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPTransferListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].wipSequence").value(hasItem(DEFAULT_WIP_SEQUENCE.intValue())))
            .andExpect(jsonPath("$.[*].wipParticulars").value(hasItem(DEFAULT_WIP_PARTICULARS)))
            .andExpect(jsonPath("$.[*].transferType").value(hasItem(DEFAULT_TRANSFER_TYPE)))
            .andExpect(jsonPath("$.[*].transferSettlement").value(hasItem(DEFAULT_TRANSFER_SETTLEMENT)))
            .andExpect(jsonPath("$.[*].transferSettlementDate").value(hasItem(DEFAULT_TRANSFER_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(sameNumber(DEFAULT_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].wipTransferDate").value(hasItem(DEFAULT_WIP_TRANSFER_DATE.toString())))
            .andExpect(jsonPath("$.[*].originalSettlement").value(hasItem(DEFAULT_ORIGINAL_SETTLEMENT)))
            .andExpect(jsonPath("$.[*].originalSettlementDate").value(hasItem(DEFAULT_ORIGINAL_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET)))
            .andExpect(jsonPath("$.[*].workProject").value(hasItem(DEFAULT_WORK_PROJECT)));

        // Check, that the count call also returns 1
        restWIPTransferListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWIPTransferListItemShouldNotBeFound(String filter) throws Exception {
        restWIPTransferListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWIPTransferListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWIPTransferListItem() throws Exception {
        // Get the wIPTransferListItem
        restWIPTransferListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchWIPTransferListItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        wIPTransferListItemRepository.saveAndFlush(wIPTransferListItem);
        when(mockWIPTransferListItemSearchRepository.search("id:" + wIPTransferListItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(wIPTransferListItem), PageRequest.of(0, 1), 1));

        // Search the wIPTransferListItem
        restWIPTransferListItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + wIPTransferListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPTransferListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].wipSequence").value(hasItem(DEFAULT_WIP_SEQUENCE.intValue())))
            .andExpect(jsonPath("$.[*].wipParticulars").value(hasItem(DEFAULT_WIP_PARTICULARS)))
            .andExpect(jsonPath("$.[*].transferType").value(hasItem(DEFAULT_TRANSFER_TYPE)))
            .andExpect(jsonPath("$.[*].transferSettlement").value(hasItem(DEFAULT_TRANSFER_SETTLEMENT)))
            .andExpect(jsonPath("$.[*].transferSettlementDate").value(hasItem(DEFAULT_TRANSFER_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(sameNumber(DEFAULT_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].wipTransferDate").value(hasItem(DEFAULT_WIP_TRANSFER_DATE.toString())))
            .andExpect(jsonPath("$.[*].originalSettlement").value(hasItem(DEFAULT_ORIGINAL_SETTLEMENT)))
            .andExpect(jsonPath("$.[*].originalSettlementDate").value(hasItem(DEFAULT_ORIGINAL_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET)))
            .andExpect(jsonPath("$.[*].workProject").value(hasItem(DEFAULT_WORK_PROJECT)));
    }
}
