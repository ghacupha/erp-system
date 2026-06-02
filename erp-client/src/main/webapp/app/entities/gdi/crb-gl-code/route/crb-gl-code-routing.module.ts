import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbGlCodeComponent } from '../list/crb-gl-code.component';
import { CrbGlCodeDetailComponent } from '../detail/crb-gl-code-detail.component';
import { CrbGlCodeUpdateComponent } from '../update/crb-gl-code-update.component';
import { CrbGlCodeRoutingResolveService } from './crb-gl-code-routing-resolve.service';

const crbGlCodeRoute: Routes = [
  {
    path: '',
    component: CrbGlCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbGlCodeDetailComponent,
    resolve: {
      crbGlCode: CrbGlCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbGlCodeUpdateComponent,
    resolve: {
      crbGlCode: CrbGlCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbGlCodeUpdateComponent,
    resolve: {
      crbGlCode: CrbGlCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbGlCodeRoute)],
  exports: [RouterModule],
})
export class CrbGlCodeRoutingModule {}
