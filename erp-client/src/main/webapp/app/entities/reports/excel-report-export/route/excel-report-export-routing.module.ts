import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExcelReportExportComponent } from '../list/excel-report-export.component';
import { ExcelReportExportDetailComponent } from '../detail/excel-report-export-detail.component';
import { ExcelReportExportUpdateComponent } from '../update/excel-report-export-update.component';
import { ExcelReportExportRoutingResolveService } from './excel-report-export-routing-resolve.service';

const excelReportExportRoute: Routes = [
  {
    path: '',
    component: ExcelReportExportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExcelReportExportDetailComponent,
    resolve: {
      excelReportExport: ExcelReportExportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExcelReportExportUpdateComponent,
    resolve: {
      excelReportExport: ExcelReportExportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExcelReportExportUpdateComponent,
    resolve: {
      excelReportExport: ExcelReportExportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(excelReportExportRoute)],
  exports: [RouterModule],
})
export class ExcelReportExportRoutingModule {}
