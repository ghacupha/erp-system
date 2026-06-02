import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GdiMasterDataIndexComponent } from '../list/gdi-master-data-index.component';
import { GdiMasterDataIndexDetailComponent } from '../detail/gdi-master-data-index-detail.component';
import { GdiMasterDataIndexUpdateComponent } from '../update/gdi-master-data-index-update.component';
import { GdiMasterDataIndexRoutingResolveService } from './gdi-master-data-index-routing-resolve.service';

const gdiMasterDataIndexRoute: Routes = [
  {
    path: '',
    component: GdiMasterDataIndexComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GdiMasterDataIndexDetailComponent,
    resolve: {
      gdiMasterDataIndex: GdiMasterDataIndexRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GdiMasterDataIndexUpdateComponent,
    resolve: {
      gdiMasterDataIndex: GdiMasterDataIndexRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GdiMasterDataIndexUpdateComponent,
    resolve: {
      gdiMasterDataIndex: GdiMasterDataIndexRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gdiMasterDataIndexRoute)],
  exports: [RouterModule],
})
export class GdiMasterDataIndexRoutingModule {}
