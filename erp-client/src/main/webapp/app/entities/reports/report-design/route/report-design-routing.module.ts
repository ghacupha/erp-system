import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportDesignComponent } from '../list/report-design.component';
import { ReportDesignDetailComponent } from '../detail/report-design-detail.component';
import { ReportDesignUpdateComponent } from '../update/report-design-update.component';
import { ReportDesignRoutingResolveService } from './report-design-routing-resolve.service';

const reportDesignRoute: Routes = [
  {
    path: '',
    component: ReportDesignComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportDesignDetailComponent,
    resolve: {
      reportDesign: ReportDesignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportDesignUpdateComponent,
    resolve: {
      reportDesign: ReportDesignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportDesignUpdateComponent,
    resolve: {
      reportDesign: ReportDesignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportDesignRoute)],
  exports: [RouterModule],
})
export class ReportDesignRoutingModule {}
