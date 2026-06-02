import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbFileTransmissionStatusComponent } from '../list/crb-file-transmission-status.component';
import { CrbFileTransmissionStatusDetailComponent } from '../detail/crb-file-transmission-status-detail.component';
import { CrbFileTransmissionStatusUpdateComponent } from '../update/crb-file-transmission-status-update.component';
import { CrbFileTransmissionStatusRoutingResolveService } from './crb-file-transmission-status-routing-resolve.service';

const crbFileTransmissionStatusRoute: Routes = [
  {
    path: '',
    component: CrbFileTransmissionStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbFileTransmissionStatusDetailComponent,
    resolve: {
      crbFileTransmissionStatus: CrbFileTransmissionStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbFileTransmissionStatusUpdateComponent,
    resolve: {
      crbFileTransmissionStatus: CrbFileTransmissionStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbFileTransmissionStatusUpdateComponent,
    resolve: {
      crbFileTransmissionStatus: CrbFileTransmissionStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbFileTransmissionStatusRoute)],
  exports: [RouterModule],
})
export class CrbFileTransmissionStatusRoutingModule {}
