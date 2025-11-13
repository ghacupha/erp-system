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
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationJobComponent } from './list/depreciation-job.component';
import { DepreciationJobDetailComponent } from './detail/depreciation-job-detail.component';
import { DepreciationJobUpdateComponent } from './update/depreciation-job-update.component';
import { DepreciationJobDeleteDialogComponent } from './delete/depreciation-job-delete-dialog.component';
import { DepreciationJobRoutingModule } from './route/depreciation-job-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationJobRoutingModule],
  declarations: [
    DepreciationJobComponent,
    DepreciationJobDetailComponent,
    DepreciationJobUpdateComponent,
    DepreciationJobDeleteDialogComponent,
  ],
  entryComponents: [DepreciationJobDeleteDialogComponent],
})
export class DepreciationJobModule {}
