import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SystemModuleComponent } from '../list/system-module.component';
import { SystemModuleDetailComponent } from '../detail/system-module-detail.component';
import { SystemModuleUpdateComponent } from '../update/system-module-update.component';
import { SystemModuleRoutingResolveService } from './system-module-routing-resolve.service';

const systemModuleRoute: Routes = [
  {
    path: '',
    component: SystemModuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SystemModuleDetailComponent,
    resolve: {
      systemModule: SystemModuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SystemModuleUpdateComponent,
    resolve: {
      systemModule: SystemModuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SystemModuleUpdateComponent,
    resolve: {
      systemModule: SystemModuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(systemModuleRoute)],
  exports: [RouterModule],
})
export class SystemModuleRoutingModule {}
