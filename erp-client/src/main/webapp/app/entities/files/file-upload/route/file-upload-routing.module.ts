import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileUploadComponent } from '../list/file-upload.component';
import { FileUploadDetailComponent } from '../detail/file-upload-detail.component';
import { FileUploadUpdateComponent } from '../update/file-upload-update.component';
import { FileUploadRoutingResolveService } from './file-upload-routing-resolve.service';

const fileUploadRoute: Routes = [
  {
    path: '',
    component: FileUploadComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileUploadDetailComponent,
    resolve: {
      fileUpload: FileUploadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileUploadUpdateComponent,
    resolve: {
      fileUpload: FileUploadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileUploadUpdateComponent,
    resolve: {
      fileUpload: FileUploadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileUploadRoute)],
  exports: [RouterModule],
})
export class FileUploadRoutingModule {}
