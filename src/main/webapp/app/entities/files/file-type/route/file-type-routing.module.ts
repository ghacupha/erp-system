import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileTypeComponent } from '../list/file-type.component';
import { FileTypeDetailComponent } from '../detail/file-type-detail.component';
import { FileTypeUpdateComponent } from '../update/file-type-update.component';
import { FileTypeRoutingResolveService } from './file-type-routing-resolve.service';

const fileTypeRoute: Routes = [
  {
    path: '',
    component: FileTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileTypeDetailComponent,
    resolve: {
      fileType: FileTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileTypeUpdateComponent,
    resolve: {
      fileType: FileTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileTypeUpdateComponent,
    resolve: {
      fileType: FileTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileTypeRoute)],
  exports: [RouterModule],
})
export class FileTypeRoutingModule {}
