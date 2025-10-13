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
import { WorkInProgressTransferComponent } from './list/work-in-progress-transfer.component';
import { WorkInProgressTransferDetailComponent } from './detail/work-in-progress-transfer-detail.component';
import { WorkInProgressTransferUpdateComponent } from './update/work-in-progress-transfer-update.component';
import { WorkInProgressTransferDeleteDialogComponent } from './delete/work-in-progress-transfer-delete-dialog.component';
import { WorkInProgressTransferRoutingModule } from './route/work-in-progress-transfer-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';

@NgModule({
  imports: [SharedModule, WorkInProgressTransferRoutingModule, ErpCommonModule],
  declarations: [
    WorkInProgressTransferComponent,
    WorkInProgressTransferDetailComponent,
    WorkInProgressTransferUpdateComponent,
    WorkInProgressTransferDeleteDialogComponent,
  ],
  entryComponents: [WorkInProgressTransferDeleteDialogComponent],
})
export class WorkInProgressTransferModule {}
