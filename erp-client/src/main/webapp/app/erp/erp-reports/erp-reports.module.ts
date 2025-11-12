///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
  imports: [RouterModule.forChild([
    {
      path: 'report-template',
      data: {
        pageTitle: 'ERP| Report Template',
        authorities: ['ROLE_DEV','ROLE_REPORT_DESIGNER'],
      },
      loadChildren: () => import('./report-template/report-template.module')
        .then(m => m.ReportTemplateModule),
    },
    {
      path: 'report-date-parameter',
      data: { pageTitle: 'ERP| Report Date' },
      loadChildren: () =>
        import('./date-parameter/report-parameters.module').then(
          m => m.ReportParametersModule
        ),
    },
    {
      path: 'work-in-progress-outstanding-report',
      data: {
        pageTitle: 'ERP| WIP Report',
        authorities: ['ROLE_FIXED_ASSETS_USER','ROLE_DEV']
      },
      loadChildren: () =>
        import('./work-in-progress-outstanding-report/work-in-progress-outstanding-report.module').then(
          m => m.WorkInProgressOutstandingReportModule
        ),
    },
    {
      path: 'work-in-progress-report',
      data: {
        pageTitle: 'ERP| WIP Summary',
        authorities: ['ROLE_FIXED_ASSETS_USER','ROLE_DEV']
      },
      loadChildren: () =>
        import('./work-in-progress-report/work-in-progress-report.module').then(
          m => m.WorkInProgressReportModule
        ),
    },
    {
      path: 'report-design',
      data: {
        pageTitle: 'ERP | Report Design',
        authorities: [],
      },
      loadChildren: () => import('./report-design/report-design.module')
        .then(m => m.ReportDesignModule),
    },
    {
      path: 'report-status',
      data: {
        pageTitle: 'ERP | Report Status',
        authorities: [],
      },
      loadChildren: () => import('./report-status/report-status.module')
        .then(m => m.ReportStatusModule),
    },
    {
      path: 'pdf-report-requisition',
      data: {
        pageTitle: 'ERP | Report Requisition - PDF',
        authorities: ['ROLE_DEV','ROLE_REPORT_ACCESSOR','ROLE_REPORT_DESIGNER'],
      },
      loadChildren: () => import('./pdf-report-requisition/pdf-report-requisition.module')
        .then(m => m.PdfReportRequisitionModule),
    },
    {
      path: 'xlsx-report-requisition',
      data: {
        pageTitle: 'ERP | EXCEL Report Requisition',
        authorities: ['ROLE_DEV','ROLE_REPORT_ACCESSOR','ROLE_REPORT_DESIGNER'],
      },
      loadChildren: () => import('./xlsx-report-requisition/xlsx-report-requisition.module')
        .then(m => m.XlsxReportRequisitionModule),
    },{
      path: 'excel-report-export',
      data: {
        pageTitle: 'ERP | Excel Export',
        authorities: ['ROLE_DEV','ROLE_REPORT_ACCESSOR','ROLE_REPORT_DESIGNER'],
      },
      loadChildren: () => import('./excel-report-export/excel-report-export.module')
        .then(m => m.ExcelReportExportModule),
    },
    {
      path: 'report-requisition',
      data: {
        pageTitle: 'ERP | Report Requisition',
        authorities: ['ROLE_DEV','ROLE_REPORT_ACCESSOR','ROLE_REPORT_DESIGNER'],
      },
      loadChildren: () => import('./report-requisition/report-requisition.module')
        .then(m => m.ReportRequisitionModule),
    },
    {
      path: 'report-content-type',
      data: {
        pageTitle: 'ERP | Report Content Type',
        authorities: ['ROLE_DEV'],
      },
      loadChildren: () => import('./report-content-type/report-content-type.module')
        .then(m => m.ReportContentTypeModule),
    },
    {
      path: 'system-content-type',
      data: {
        pageTitle: 'ERP | System Content Type',
        authorities: ['ROLE_DEV'],
      },
      loadChildren: () => import('./system-content-type/system-content-type.module')
        .then(m => m.SystemContentTypeModule),
    },
    {
      path: 'reports/view/:slug',
      data: {
        pageTitle: 'ERP | Dynamic report summary',
        authorities: ['ROLE_DEV','ROLE_REPORT_ACCESSOR','ROLE_REPORT_DESIGNER'],
      },
      canActivate: [UserRouteAccessService],
      loadChildren: () =>
        import('./report-summary-view/report-summary-view.module').then(m => m.ReportSummaryViewModule),
    },
    {
      path: 'work-in-progress-overview',
      data: {
        pageTitle: 'ERP | WIP Overview',
        authorities: ['ROLE_FIXED_ASSETS_USER'],
      },
      canActivate: [UserRouteAccessService],
      loadChildren: () =>
        import('./work-in-progress-overview/work-in-progress-overview.module').then(
          m => m.WorkInProgressOverviewModule
        ),
    },
    {
      path: 'autonomous-report',
      data: {
        pageTitle: 'ERP | Auto Reports',
      },
      canActivate: [UserRouteAccessService],
      loadChildren: () =>
        import('./autonomous-report/autonomous-report.module').then(
          m => m.AutonomousReportModule
        ),
    },
  ])
  ]
})
export class ErpReportsModule {}
