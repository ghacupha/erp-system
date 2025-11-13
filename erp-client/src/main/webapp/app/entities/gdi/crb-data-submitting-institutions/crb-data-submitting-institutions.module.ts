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
import { CrbDataSubmittingInstitutionsComponent } from './list/crb-data-submitting-institutions.component';
import { CrbDataSubmittingInstitutionsDetailComponent } from './detail/crb-data-submitting-institutions-detail.component';
import { CrbDataSubmittingInstitutionsUpdateComponent } from './update/crb-data-submitting-institutions-update.component';
import { CrbDataSubmittingInstitutionsDeleteDialogComponent } from './delete/crb-data-submitting-institutions-delete-dialog.component';
import { CrbDataSubmittingInstitutionsRoutingModule } from './route/crb-data-submitting-institutions-routing.module';

@NgModule({
  imports: [SharedModule, CrbDataSubmittingInstitutionsRoutingModule],
  declarations: [
    CrbDataSubmittingInstitutionsComponent,
    CrbDataSubmittingInstitutionsDetailComponent,
    CrbDataSubmittingInstitutionsUpdateComponent,
    CrbDataSubmittingInstitutionsDeleteDialogComponent,
  ],
  entryComponents: [CrbDataSubmittingInstitutionsDeleteDialogComponent],
})
export class CrbDataSubmittingInstitutionsModule {}
