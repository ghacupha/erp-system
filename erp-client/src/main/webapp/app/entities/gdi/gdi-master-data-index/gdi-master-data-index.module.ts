///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { SharedModule } from 'app/shared/shared.module';
import { GdiMasterDataIndexComponent } from './list/gdi-master-data-index.component';
import { GdiMasterDataIndexDetailComponent } from './detail/gdi-master-data-index-detail.component';
import { GdiMasterDataIndexUpdateComponent } from './update/gdi-master-data-index-update.component';
import { GdiMasterDataIndexDeleteDialogComponent } from './delete/gdi-master-data-index-delete-dialog.component';
import { GdiMasterDataIndexRoutingModule } from './route/gdi-master-data-index-routing.module';

@NgModule({
  imports: [SharedModule, GdiMasterDataIndexRoutingModule],
  declarations: [
    GdiMasterDataIndexComponent,
    GdiMasterDataIndexDetailComponent,
    GdiMasterDataIndexUpdateComponent,
    GdiMasterDataIndexDeleteDialogComponent,
  ],
  entryComponents: [GdiMasterDataIndexDeleteDialogComponent],
})
export class GdiMasterDataIndexModule {}
