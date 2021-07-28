///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFileType, FileType } from 'app/shared/model/files/file-type.model';
import { FileTypeService } from './file-type.service';
import { FileTypeComponent } from './file-type.component';
import { FileTypeDetailComponent } from './file-type-detail.component';
import { FileTypeUpdateComponent } from './file-type-update.component';

@Injectable({ providedIn: 'root' })
export class FileTypeResolve implements Resolve<IFileType> {
  constructor(private service: FileTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fileType: HttpResponse<FileType>) => {
          if (fileType.body) {
            return of(fileType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileType());
  }
}

export const fileTypeRoute: Routes = [
  {
    path: '',
    component: FileTypeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'FileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileTypeDetailComponent,
    resolve: {
      fileType: FileTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileTypeUpdateComponent,
    resolve: {
      fileType: FileTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileTypeUpdateComponent,
    resolve: {
      fileType: FileTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
];
