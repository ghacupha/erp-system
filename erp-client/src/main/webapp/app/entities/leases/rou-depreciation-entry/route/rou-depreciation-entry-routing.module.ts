import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouDepreciationEntryComponent } from '../list/rou-depreciation-entry.component';
import { RouDepreciationEntryDetailComponent } from '../detail/rou-depreciation-entry-detail.component';
import { RouDepreciationEntryUpdateComponent } from '../update/rou-depreciation-entry-update.component';
import { RouDepreciationEntryRoutingResolveService } from './rou-depreciation-entry-routing-resolve.service';

const rouDepreciationEntryRoute: Routes = [
  {
    path: '',
    component: RouDepreciationEntryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouDepreciationEntryDetailComponent,
    resolve: {
      rouDepreciationEntry: RouDepreciationEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouDepreciationEntryUpdateComponent,
    resolve: {
      rouDepreciationEntry: RouDepreciationEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouDepreciationEntryUpdateComponent,
    resolve: {
      rouDepreciationEntry: RouDepreciationEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouDepreciationEntryRoute)],
  exports: [RouterModule],
})
export class RouDepreciationEntryRoutingModule {}
