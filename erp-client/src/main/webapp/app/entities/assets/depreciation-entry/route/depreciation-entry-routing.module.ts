import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepreciationEntryComponent } from '../list/depreciation-entry.component';
import { DepreciationEntryDetailComponent } from '../detail/depreciation-entry-detail.component';
import { DepreciationEntryUpdateComponent } from '../update/depreciation-entry-update.component';
import { DepreciationEntryRoutingResolveService } from './depreciation-entry-routing-resolve.service';

const depreciationEntryRoute: Routes = [
  {
    path: '',
    component: DepreciationEntryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepreciationEntryDetailComponent,
    resolve: {
      depreciationEntry: DepreciationEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepreciationEntryUpdateComponent,
    resolve: {
      depreciationEntry: DepreciationEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepreciationEntryUpdateComponent,
    resolve: {
      depreciationEntry: DepreciationEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depreciationEntryRoute)],
  exports: [RouterModule],
})
export class DepreciationEntryRoutingModule {}
