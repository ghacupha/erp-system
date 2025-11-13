///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { NgModule } from '@angular/core';

@NgModule({
  imports: [
    RouterModule.forChild([
        {
          path: 'prepayment-account',
          data: {
            pageTitle: 'ERP | Prepayment Account',
            authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () =>
            import('./prepayment-account/prepayment-account.module').then(
              m => m.PrepaymentAccountModule
            ),
        },
        {
            path: 'amortization-recurrence',
            data: {
              pageTitle: 'ERP | Amortization Recurrence',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./amortization-recurrence/amortization-recurrence.module').then(
                m => m.AmortizationRecurrenceModule
              ),
          },
        {
            path: 'amortization-sequence',
            data: {
              pageTitle: 'ERP | Amortization Sequence',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./amortization-sequence/amortization-sequence.module').then(
                m => m.AmortizationSequenceModule
              ),
          },
          {
            path: 'prepayment-mapping',
            data: {
              pageTitle: 'ERP | Prepayment Mapping',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-mapping/prepayment-mapping.module').then(
                m => m.PrepaymentMappingModule
              ),
          },
          {
            path: 'prepayment-marshalling',
            data: {
              pageTitle: 'ERP | Prepayment Marshalling',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-marshalling/prepayment-marshalling.module').then(
                m => m.PrepaymentMarshallingModule
              ),
          },
          {
            path: 'prepayment-amortization',
            data: {
              pageTitle: 'ERP | Prepayment Amortization',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-amortization/prepayment-amortization.module').then(
                m => m.PrepaymentAmortizationModule
              ),
          },
          {
            path: 'prepayment-compilation-request',
            data: {
              pageTitle: 'ERP | Prepayment Compilation',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-compilation-request/prepayment-compilation-request.module').then(
                m => m.PrepaymentCompilationRequestModule
              ),
          },
          {
            path: 'prepayment-report',
            data: {
              pageTitle: 'ERP | Prepayment Report',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-report/prepayment-report.module').then(
                m => m.PrepaymentReportModule
              ),
          },
          {
            path: 'prepayment-account-report',
            data: {
              pageTitle: 'ERP | Prepayment Account Report',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-account-report/prepayment-account-report.module').then(
                m => m.PrepaymentAccountReportModule
              ),
          },
          {
            path: 'prepayment-outstanding-overview-report',
            data: {
              pageTitle: 'ERP | Prepayment Overview',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-outstanding-overview-report/prepayment-outstanding-overview-report.module').then(
                m => m.PrepaymentOutstandingOverviewReportModule
              ),
          },
          {
            path: 'amortization-posting-report',
            data: {
              pageTitle: 'ERP | Posting Amortization',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./amortization-posting-report/amortization-posting-report.module').then(
                m => m.AmortizationPostingReportModule
              ),
          },
          {
            path: 'monthly-prepayment-outstanding-report-item',
            data: {
              pageTitle: 'ERP | Prepayments Monthly',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./monthly-prepayment-outstanding-report-item/monthly-prepayment-outstanding-report-item.module').then(m => m.MonthlyPrepaymentOutstandingReportItemModule),
        },
        {
            path: 'monthly-prepayment-report-requisition',
            data: {
              pageTitle: 'ERP | Prepayments Monthly Requisition',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./monthly-prepayment-report-requisition/monthly-prepayment-report-requisition.module').then(m => m.MonthlyPrepaymentReportRequisitionModule),
        },
        {
            path: 'amortization-period',
            data: {
              pageTitle: 'ERP | Amortization Period',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./amortization-period/amortization-period.module').then(m => m.AmortizationPeriodModule),
        },
        {
            path: 'prepayment-report-requisition',
            data: {
              pageTitle: 'ERP | Prepayment Report',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-report-requisition/prepayment-report-requisition.module').then(m => m.PrepaymentReportRequisitionModule),
        },
        {
            path: 'amortization-posting-report-requisition',
            data: {
              pageTitle: 'ERP | Posting Amortization',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./amortization-posting-report-requisition/amortization-posting-report-requisition.module').then(m => m.AmortizationPostingReportRequisitionModule),
        },
        {
            path: 'prepayment-by-account-report-requisition',
            data: {
              pageTitle: 'ERP | Prepayment Accounts',
              authorities: ['ROLE_PREPAYMENTS_MODULE_USER'],
            },
            canActivate: [UserRouteAccessService],
            loadChildren: () =>
              import('./prepayment-by-account-report-requisition/prepayment-by-account-report-requisition.module').then(m => m.PrepaymentByAccountReportRequisitionModule),
        },
      ]
    )
  ]
})
export class ErpPrepaymentsAccountingModule {
}
