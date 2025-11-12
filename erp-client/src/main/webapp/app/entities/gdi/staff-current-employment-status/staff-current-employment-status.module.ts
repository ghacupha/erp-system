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
import { StaffCurrentEmploymentStatusComponent } from './list/staff-current-employment-status.component';
import { StaffCurrentEmploymentStatusDetailComponent } from './detail/staff-current-employment-status-detail.component';
import { StaffCurrentEmploymentStatusUpdateComponent } from './update/staff-current-employment-status-update.component';
import { StaffCurrentEmploymentStatusDeleteDialogComponent } from './delete/staff-current-employment-status-delete-dialog.component';
import { StaffCurrentEmploymentStatusRoutingModule } from './route/staff-current-employment-status-routing.module';

@NgModule({
  imports: [SharedModule, StaffCurrentEmploymentStatusRoutingModule],
  declarations: [
    StaffCurrentEmploymentStatusComponent,
    StaffCurrentEmploymentStatusDetailComponent,
    StaffCurrentEmploymentStatusUpdateComponent,
    StaffCurrentEmploymentStatusDeleteDialogComponent,
  ],
  entryComponents: [StaffCurrentEmploymentStatusDeleteDialogComponent],
})
export class StaffCurrentEmploymentStatusModule {}
