import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SourceRemittancePurposeTypeComponent } from '../list/source-remittance-purpose-type.component';
import { SourceRemittancePurposeTypeDetailComponent } from '../detail/source-remittance-purpose-type-detail.component';
import { SourceRemittancePurposeTypeUpdateComponent } from '../update/source-remittance-purpose-type-update.component';
import { SourceRemittancePurposeTypeRoutingResolveService } from './source-remittance-purpose-type-routing-resolve.service';

const sourceRemittancePurposeTypeRoute: Routes = [
  {
    path: '',
    component: SourceRemittancePurposeTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SourceRemittancePurposeTypeDetailComponent,
    resolve: {
      sourceRemittancePurposeType: SourceRemittancePurposeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SourceRemittancePurposeTypeUpdateComponent,
    resolve: {
      sourceRemittancePurposeType: SourceRemittancePurposeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SourceRemittancePurposeTypeUpdateComponent,
    resolve: {
      sourceRemittancePurposeType: SourceRemittancePurposeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sourceRemittancePurposeTypeRoute)],
  exports: [RouterModule],
})
export class SourceRemittancePurposeTypeRoutingModule {}
