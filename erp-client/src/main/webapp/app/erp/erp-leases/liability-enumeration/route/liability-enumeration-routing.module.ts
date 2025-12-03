import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LiabilityEnumerationComponent } from '../list/liability-enumeration.component';
import { LiabilityEnumerationUpdateComponent } from '../update/liability-enumeration-update.component';
import { PresentValueEnumerationComponent } from '../present-values/present-value-enumeration.component';

const routes: Routes = [
  {
    path: '',
    component: LiabilityEnumerationComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_LEASE_MANAGER'] },
  },
  {
    path: 'create',
    component: LiabilityEnumerationUpdateComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_LEASE_MANAGER'] },
  },
  {
    path: ':id/present-values',
    component: PresentValueEnumerationComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_LEASE_MANAGER'] },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LiabilityEnumerationRoutingModule {}
