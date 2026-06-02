import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkProjectRegisterComponent } from '../list/work-project-register.component';
import { WorkProjectRegisterDetailComponent } from '../detail/work-project-register-detail.component';
import { WorkProjectRegisterUpdateComponent } from '../update/work-project-register-update.component';
import { WorkProjectRegisterRoutingResolveService } from './work-project-register-routing-resolve.service';

const workProjectRegisterRoute: Routes = [
  {
    path: '',
    component: WorkProjectRegisterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkProjectRegisterDetailComponent,
    resolve: {
      workProjectRegister: WorkProjectRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkProjectRegisterUpdateComponent,
    resolve: {
      workProjectRegister: WorkProjectRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkProjectRegisterUpdateComponent,
    resolve: {
      workProjectRegister: WorkProjectRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workProjectRegisterRoute)],
  exports: [RouterModule],
})
export class WorkProjectRegisterRoutingModule {}
