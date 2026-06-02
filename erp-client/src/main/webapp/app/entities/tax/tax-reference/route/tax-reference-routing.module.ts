import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaxReferenceComponent } from '../list/tax-reference.component';
import { TaxReferenceDetailComponent } from '../detail/tax-reference-detail.component';
import { TaxReferenceUpdateComponent } from '../update/tax-reference-update.component';
import { TaxReferenceRoutingResolveService } from './tax-reference-routing-resolve.service';

const taxReferenceRoute: Routes = [
  {
    path: '',
    component: TaxReferenceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaxReferenceDetailComponent,
    resolve: {
      taxReference: TaxReferenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaxReferenceUpdateComponent,
    resolve: {
      taxReference: TaxReferenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaxReferenceUpdateComponent,
    resolve: {
      taxReference: TaxReferenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(taxReferenceRoute)],
  exports: [RouterModule],
})
export class TaxReferenceRoutingModule {}
