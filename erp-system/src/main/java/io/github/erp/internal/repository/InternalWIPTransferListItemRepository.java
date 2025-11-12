package io.github.erp.internal.repository;

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
import io.github.erp.domain.WIPListItem;
import io.github.erp.domain.WIPListItemREPO;
import io.github.erp.domain.WIPTransferListItem;
import io.github.erp.domain.WIPTransferListItemREPO;
import io.reactivex.Flowable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WIPTransferListItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalWIPTransferListItemRepository
    extends JpaRepository<WIPTransferListItem, Long>, JpaSpecificationExecutor<WIPTransferListItem> {

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    trf.id as id,  " +
            "    r.sequence_number as wipSequence, " +
            "    r.particulars as wipParticulars,  " +
            "    transfer_type as transferType, " +
            "    trfSet.payment_number as transferSettlement,  " +
            "    trfSet.payment_date as transferSettlementDate, " +
            "    transfer_amount as transferAmount,  " +
            "    transfer_date as wipTransferDate,  " +
            "    orgSet.payment_number as originalSettlement,  " +
            "    orgSet.payment_date as originalSettlementDate,  " +
            "    cat.asset_category_name as assetCategory,   " +
            "    sol.outlet_code as serviceOutlet,  " +
            "    prj.project_title as workProject  " +
            "FROM public.work_in_progress_transfer trf " +
            "  LEFT JOIN work_in_progress_registration r ON r.id = trf.work_in_progress_registration_id " +
            "  LEFT JOIN settlement trfSet ON trfSet.id = transfer_settlement_id  " +
            "  LEFT JOIN settlement orgSet ON orgSet.id = original_settlement_id  " +
            "  LEFT JOIN asset_category cat ON cat.id = asset_category_id  " +
            "  LEFT JOIN service_outlet sol ON sol.id = service_outlet_id  " +
            "  LEFT JOIN work_project_register prj ON prj.id = r.work_project_register_id",

        countQuery = "" +
            "SELECT  " +
            "    trf.id as id,  " +
            "    r.sequence_number as wipSequence, " +
            "    r.particulars as wipParticulars,  " +
            "    transfer_type as transferType, " +
            "    trfSet.payment_number as transferSettlement,  " +
            "    trfSet.payment_date as transferSettlementDate, " +
            "    transfer_amount as transferAmount,  " +
            "    transfer_date as wipTransferDate,  " +
            "    orgSet.payment_number as originalSettlement,  " +
            "    orgSet.payment_date as originalSettlementDate,  " +
            "    cat.asset_category_name as assetCategory,   " +
            "    sol.outlet_code as serviceOutlet,  " +
            "    prj.project_title as workProject  " +
            "FROM public.work_in_progress_transfer trf " +
            "  LEFT JOIN work_in_progress_registration r ON r.id = trf.work_in_progress_registration_id " +
            "  LEFT JOIN settlement trfSet ON trfSet.id = transfer_settlement_id  " +
            "  LEFT JOIN settlement orgSet ON orgSet.id = original_settlement_id  " +
            "  LEFT JOIN asset_category cat ON cat.id = asset_category_id  " +
            "  LEFT JOIN service_outlet sol ON sol.id = service_outlet_id  " +
            "  LEFT JOIN work_project_register prj ON prj.id = r.work_project_register_id"
    )
    Page<WIPTransferListItemREPO> findAllSpecifiedReportItems(Specification<WIPTransferListItem> specification, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    trf.id as id,  " +
            "    r.sequence_number as wipSequence, " +
            "    r.particulars as wipParticulars,  " +
            "    transfer_type as transferType, " +
            "    trfSet.payment_number as transferSettlement,  " +
            "    trfSet.payment_date as transferSettlementDate, " +
            "    transfer_amount as transferAmount,  " +
            "    transfer_date as wipTransferDate,  " +
            "    orgSet.payment_number as originalSettlement,  " +
            "    orgSet.payment_date as originalSettlementDate,  " +
            "    cat.asset_category_name as assetCategory,   " +
            "    sol.outlet_code as serviceOutlet,  " +
            "    prj.project_title as workProject  " +
            "FROM public.work_in_progress_transfer trf " +
            "  LEFT JOIN work_in_progress_registration r ON r.id = trf.work_in_progress_registration_id " +
            "  LEFT JOIN settlement trfSet ON trfSet.id = transfer_settlement_id  " +
            "  LEFT JOIN settlement orgSet ON orgSet.id = original_settlement_id  " +
            "  LEFT JOIN asset_category cat ON cat.id = asset_category_id  " +
            "  LEFT JOIN service_outlet sol ON sol.id = service_outlet_id  " +
            "  LEFT JOIN work_project_register prj ON prj.id = r.work_project_register_id",

        countQuery = "" +
            "SELECT  " +
            "    trf.id as id,  " +
            "    r.sequence_number as wipSequence, " +
            "    r.particulars as wipParticulars,  " +
            "    transfer_type as transferType, " +
            "    trfSet.payment_number as transferSettlement,  " +
            "    trfSet.payment_date as transferSettlementDate, " +
            "    transfer_amount as transferAmount,  " +
            "    transfer_date as wipTransferDate,  " +
            "    orgSet.payment_number as originalSettlement,  " +
            "    orgSet.payment_date as originalSettlementDate,  " +
            "    cat.asset_category_name as assetCategory,   " +
            "    sol.outlet_code as serviceOutlet,  " +
            "    prj.project_title as workProject  " +
            "FROM public.work_in_progress_transfer trf " +
            "  LEFT JOIN work_in_progress_registration r ON r.id = trf.work_in_progress_registration_id " +
            "  LEFT JOIN settlement trfSet ON trfSet.id = transfer_settlement_id  " +
            "  LEFT JOIN settlement orgSet ON orgSet.id = original_settlement_id  " +
            "  LEFT JOIN asset_category cat ON cat.id = asset_category_id  " +
            "  LEFT JOIN service_outlet sol ON sol.id = service_outlet_id  " +
            "  LEFT JOIN work_project_register prj ON prj.id = r.work_project_register_id"
    )
    Page<WIPTransferListItemREPO> findAllReportItems(Pageable pageable);
}
