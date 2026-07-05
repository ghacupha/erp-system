import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbRecordFileTypeComponent } from '../list/crb-record-file-type.component';
import { CrbRecordFileTypeDetailComponent } from '../detail/crb-record-file-type-detail.component';
import { CrbRecordFileTypeUpdateComponent } from '../update/crb-record-file-type-update.component';
import { CrbRecordFileTypeRoutingResolveService } from './crb-record-file-type-routing-resolve.service';

const crbRecordFileTypeRoute: Routes = [
  {
    path: '',
    component: CrbRecordFileTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbRecordFileTypeDetailComponent,
    resolve: {
      crbRecordFileType: CrbRecordFileTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbRecordFileTypeUpdateComponent,
    resolve: {
      crbRecordFileType: CrbRecordFileTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbRecordFileTypeUpdateComponent,
    resolve: {
      crbRecordFileType: CrbRecordFileTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbRecordFileTypeRoute)],
  exports: [RouterModule],
})
export class CrbRecordFileTypeRoutingModule {}
