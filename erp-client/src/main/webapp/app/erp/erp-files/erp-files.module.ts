///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'erp/file-type',
        data: {
          pageTitle: 'ERP | File Types',
          authorities: ['ROLE_DBA'],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./file-type/file-type.module').then(m => m.ErpServiceFileTypeModule),
      },
      {
        path: 'erp/file-upload',
        data: {
          pageTitle: 'ERP | File Uploads',
          authorities: ['ROLE_DBA'],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./file-upload/file-upload.module').then(m => m.ErpServiceFileUploadModule),
      },
      {
        path: 'erp/message-token',
        data: {
          pageTitle: 'ERP | Message Tokens',
          authorities: ['ROLE_DBA'],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./message-token/message-token.module').then(m => m.ErpServiceMessageTokenModule),
      }
    ])
  ]
})
export class ErpFilesModule {

}
