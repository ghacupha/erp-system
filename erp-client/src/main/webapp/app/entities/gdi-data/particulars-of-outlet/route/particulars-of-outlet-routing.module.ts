import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParticularsOfOutletComponent } from '../list/particulars-of-outlet.component';
import { ParticularsOfOutletDetailComponent } from '../detail/particulars-of-outlet-detail.component';
import { ParticularsOfOutletUpdateComponent } from '../update/particulars-of-outlet-update.component';
import { ParticularsOfOutletRoutingResolveService } from './particulars-of-outlet-routing-resolve.service';

const particularsOfOutletRoute: Routes = [
  {
    path: '',
    component: ParticularsOfOutletComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParticularsOfOutletDetailComponent,
    resolve: {
      particularsOfOutlet: ParticularsOfOutletRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParticularsOfOutletUpdateComponent,
    resolve: {
      particularsOfOutlet: ParticularsOfOutletRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParticularsOfOutletUpdateComponent,
    resolve: {
      particularsOfOutlet: ParticularsOfOutletRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(particularsOfOutletRoute)],
  exports: [RouterModule],
})
export class ParticularsOfOutletRoutingModule {}
