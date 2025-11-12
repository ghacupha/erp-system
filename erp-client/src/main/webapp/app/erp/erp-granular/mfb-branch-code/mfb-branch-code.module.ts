///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { MfbBranchCodeComponent } from './list/mfb-branch-code.component';
import { MfbBranchCodeDetailComponent } from './detail/mfb-branch-code-detail.component';
import { MfbBranchCodeUpdateComponent } from './update/mfb-branch-code-update.component';
import { MfbBranchCodeDeleteDialogComponent } from './delete/mfb-branch-code-delete-dialog.component';
import { MfbBranchCodeRoutingModule } from './route/mfb-branch-code-routing.module';

@NgModule({
  imports: [SharedModule, MfbBranchCodeRoutingModule],
  declarations: [MfbBranchCodeComponent, MfbBranchCodeDetailComponent, MfbBranchCodeUpdateComponent, MfbBranchCodeDeleteDialogComponent],
  entryComponents: [MfbBranchCodeDeleteDialogComponent],
})
export class MfbBranchCodeModule {}
