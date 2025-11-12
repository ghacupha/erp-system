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
import { EmploymentTermsComponent } from './list/employment-terms.component';
import { EmploymentTermsDetailComponent } from './detail/employment-terms-detail.component';
import { EmploymentTermsUpdateComponent } from './update/employment-terms-update.component';
import { EmploymentTermsDeleteDialogComponent } from './delete/employment-terms-delete-dialog.component';
import { EmploymentTermsRoutingModule } from './route/employment-terms-routing.module';

@NgModule({
  imports: [SharedModule, EmploymentTermsRoutingModule],
  declarations: [
    EmploymentTermsComponent,
    EmploymentTermsDetailComponent,
    EmploymentTermsUpdateComponent,
    EmploymentTermsDeleteDialogComponent,
  ],
  entryComponents: [EmploymentTermsDeleteDialogComponent],
})
export class EmploymentTermsModule {}
