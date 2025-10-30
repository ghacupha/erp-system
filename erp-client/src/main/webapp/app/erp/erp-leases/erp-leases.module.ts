///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forChild([
        {
          path: 'lease-contract',
          data: {
            pageTitle: 'ERP | leases',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-contract/lease-contract.module').then(
              m => m.LeaseContractModule
            ),
        },
      {
          path: 'lease-model-metadata',
          data: {
            pageTitle: 'ERP | leases',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-model-metadata/lease-model-metadata.module').then(
              m => m.LeaseModelMetadataModule
            ),
        },
        {
          path: 'lease-liability-schedule-view',
          data: {
            pageTitle: 'ERP | Lease Liability Dashboard',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-schedule-view/lease-liability-schedule-view.module').then(
              m => m.LeaseLiabilityScheduleViewModule
            ),
        },
        {
          path: 'lease-liability-schedule-item',
          data: {
            pageTitle: 'ERP | Schedule',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-schedule-item/lease-liability-schedule-item.module').then(
              m => m.LeaseLiabilityScheduleItemModule
            ),
        },
        {
          path: 'ifrs-16-lease-contract',
          data: {
            pageTitle: 'ERP | Lease Contract',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./ifrs-16-lease-contract/ifrs-16-lease-contract.module').then(
              m => m.IFRS16LeaseContractModule
            ),
        },
        {
          path: 'rou-model-metadata',
          data: {
            pageTitle: 'ERP | ROU Metadata',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-model-metadata/rou-model-metadata.module').then(
              m => m.RouModelMetadataModule
            ),
        },
        {
          path: 'rou-account-balance-report',
          data: {
            pageTitle: 'ERP | ROU A/C Balance',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-account-balance-report/rou-account-balance-report.module').then(
              m => m.RouAccountBalanceReportModule
            ),
        },
        {
          path: 'rou-account-balance-report-item',
          data: {
            pageTitle: 'ERP | ROU A/C Balance',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-account-balance-report-item/rou-account-balance-report-item.module').then(
              m => m.RouAccountBalanceReportItemModule
            ),
        },
        {
          path: 'rou-asset-list-report',
          data: {
            pageTitle: 'ERP | ROU Asset List Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-asset-list-report/rou-asset-list-report.module').then(
              m => m.RouAssetListReportModule
            ),
        },
        {
          path: 'rou-asset-list-report-item',
          data: {
            pageTitle: 'ERP | ROU Asset Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-asset-list-report-item/rou-asset-list-report-item.module').then(
              m => m.RouAssetListReportItemModule
            ),
        },
        {
          path: 'rou-asset-nbv-report',
          data: {
            pageTitle: 'ERP | ROU Asset NBV Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-asset-nbv-report/rou-asset-nbv-report.module').then(
              m => m.RouAssetNBVReportModule
            ),
        },
        {
          path: 'rou-asset-nbv-report-item',
          data: {
            pageTitle: 'ERP | ROU Asset NBV Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-asset-nbv-report-item/rou-asset-nbv-report-item.module').then(
              m => m.RouAssetNBVReportItemModule
            ),
        },
        {
          path: 'rou-depreciation-entry-report',
          data: {
            pageTitle: 'ERP | ROU Depreciation Entries Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-depreciation-entry-report/rou-depreciation-entry-report.module').then(
              m => m.RouDepreciationEntryReportModule
            ),
        },
        {
          path: 'rou-depreciation-posting-report',
          data: {
            pageTitle: 'ERP | ROU Depreciation Posting Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-depreciation-posting-report/rou-depreciation-posting-report.module').then(
              m => m.RouDepreciationPostingReportModule
            ),
        },
        {
          path: 'rou-depreciation-posting-report-item',
          data: {
            pageTitle: 'ERP | ROU Depreciation Posting Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-depreciation-posting-report-item/rou-depreciation-posting-report-item.module').then(
              m => m.RouDepreciationPostingReportItemModule
            ),
        },
        {
          path: 'rou-depreciation-request',
          data: {
            pageTitle: 'ERP | ROU Depreciation Request',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-depreciation-request/rou-depreciation-request.module').then(
              m => m.RouDepreciationRequestModule
            ),
        },
        {
          path: 'rou-monthly-depreciation-report',
          data: {
            pageTitle: 'ERP | ROU Monthly Depreciation',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-monthly-depreciation-report/rou-monthly-depreciation-report.module').then(
              m => m.RouMonthlyDepreciationReportModule
            ),
        },
        {
          path: 'rou-monthly-depreciation-report-item',
          data: {
            pageTitle: 'ERP | ROU Monthly Depreciation',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-monthly-depreciation-report-item/rou-monthly-depreciation-report-item.module').then(
              m => m.RouMonthlyDepreciationReportItemModule
            ),
        },
        {
          path: 'rou-depreciation-report-item',
          data: {
            pageTitle: 'ERP | ROU Depreciation Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-depreciation-entry-report-item/rou-depreciation-entry-report-item.module').then(
              m => m.RouDepreciationEntryReportItemModule
            ),
        },
        {
          path: 'lease-period',
          data: {
            pageTitle: 'ERP | Lease Period',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-period/lease-period.module').then(
              m => m.LeasePeriodModule
            ),
        },
        {
          path: 'lease-liability',
          data: {
            pageTitle: 'ERP | Lease Liability',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability/lease-liability.module').then(
              m => m.LeaseLiabilityModule
            ),
        },
        {
          path: 'lease-payment',
          data: {
            pageTitle: 'ERP | Lease Payment',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-payment/lease-payment.module').then(
              m => m.LeasePaymentModule
            ),
        },
        {
          path: 'lease-liability-compilation',
          data: {
            pageTitle: 'ERP | Lease Compilation',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-compilation/lease-liability-compilation.module').then(
              m => m.LeaseLiabilityCompilationModule
            ),
        },
        {
          path: 'lease-amortization-calculation',
          data: {
            pageTitle: 'ERP | Lease Calculation',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-amortization-calculation/lease-amortization-calculation.module').then(
              m => m.LeaseAmortizationCalculationModule
            ),
        },
        {
          path: 'lease-amortization-schedule',
          data: {
            pageTitle: 'ERP | Lease Schedule',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-amortization-schedule/lease-amortization-schedule.module').then(
              m => m.LeaseAmortizationScheduleModule
            ),
        },
        {
          path: 'lease-liability-schedule-item',
          data: {
            pageTitle: 'ERP | Lease Schedule Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-schedule-item/lease-liability-schedule-item.module').then(
              m => m.LeaseLiabilityScheduleItemModule
            ),
        },
        {
          path: 'lease-repayment-period',
          data: {
            pageTitle: 'ERP | Lease Repayment Period',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-repayment-period/lease-repayment-period.module').then(
              m => m.LeaseRepaymentPeriodModule
            ),
        },
        {
          path: 'lease-liability-by-account-report',
          data: {
            pageTitle: 'ERP | Liability Account Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-by-account-report/lease-liability-by-account-report.module').then(
              m => m.LeaseLiabilityByAccountReportModule
            ),
        },
        {
          path: 'lease-liability-by-account-report-item',
          data: {
            pageTitle: 'ERP | Liability Account Report Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-by-account-report-item/lease-liability-by-account-report-item.module').then(
              m => m.LeaseLiabilityByAccountReportItemModule
            ),
        },
        {
          path: 'lease-liability-posting-report',
          data: {
            pageTitle: 'ERP | Liability Posting Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-posting-report/lease-liability-posting-report.module').then(
              m => m.LeaseLiabilityPostingReportModule
            ),
        },
        {
          path: 'lease-liability-posting-report-item',
          data: {
            pageTitle: 'ERP | Liability Posting Report Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-posting-report-item/lease-liability-posting-report-item.module').then(
              m => m.LeaseLiabilityPostingReportItemModule
            ),
        },
        {
          path: 'lease-liability-report',
          data: {
            pageTitle: 'ERP | Liability Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-report/lease-liability-report.module').then(
              m => m.LeaseLiabilityReportModule
            ),
        },
        {
          path: 'lease-liability-report-item',
          data: {
            pageTitle: 'ERP | Liability Report Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-report-item/lease-liability-report-item.module').then(
              m => m.LeaseLiabilityReportItemModule
            ),
        },
        {
          path: 'lease-liability-schedule-report',
          data: {
            pageTitle: 'ERP | Liability Schedule Report',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-schedule-report/lease-liability-schedule-report.module').then(
              m => m.LeaseLiabilityScheduleReportModule
            ),
        },
        {
          path: 'lease-liability-schedule-report/report-nav',
          data: {
            pageTitle: 'ERP | Liability Schedule Parameters',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./ifrs16-lease-report-nav/ifrs16-lease-report-nav.module').then(
              m => m.Ifrs16LeaseReportNavModule
            ),
        },
        {
          path: 'lease-liability-schedule-report-item',
          data: {
            pageTitle: 'ERP | Liability Schedule Report Items',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./lease-liability-schedule-report-item/lease-liability-schedule-report-item.module').then(
              m => m.LeaseLiabilityScheduleReportItemModule
            ),
        },
        {
          path: 'rou-initial-direct-cost',
          data: {
            pageTitle: 'ERP | Initial Direct Costs',
            authorities: ['ROLE_LEASE_MANAGER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./rou-initial-direct-cost/rou-initial-direct-cost.module').then(
              m => m.RouInitialDirectCostModule
            ),
        },
      ]
    )
  ]
})
export class ErpLeasesModule {
}
