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
import { SourcesOfFundsTypeCodeComponent } from './list/sources-of-funds-type-code.component';
import { SourcesOfFundsTypeCodeDetailComponent } from './detail/sources-of-funds-type-code-detail.component';
import { SourcesOfFundsTypeCodeUpdateComponent } from './update/sources-of-funds-type-code-update.component';
import { SourcesOfFundsTypeCodeDeleteDialogComponent } from './delete/sources-of-funds-type-code-delete-dialog.component';
import { SourcesOfFundsTypeCodeRoutingModule } from './route/sources-of-funds-type-code-routing.module';

@NgModule({
  imports: [SharedModule, SourcesOfFundsTypeCodeRoutingModule],
  declarations: [
    SourcesOfFundsTypeCodeComponent,
    SourcesOfFundsTypeCodeDetailComponent,
    SourcesOfFundsTypeCodeUpdateComponent,
    SourcesOfFundsTypeCodeDeleteDialogComponent,
  ],
  entryComponents: [SourcesOfFundsTypeCodeDeleteDialogComponent],
})
export class SourcesOfFundsTypeCodeModule {}
