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
import { SubCountyCodeComponent } from './list/sub-county-code.component';
import { SubCountyCodeDetailComponent } from './detail/sub-county-code-detail.component';
import { SubCountyCodeUpdateComponent } from './update/sub-county-code-update.component';
import { SubCountyCodeDeleteDialogComponent } from './delete/sub-county-code-delete-dialog.component';
import { SubCountyCodeRoutingModule } from './route/sub-county-code-routing.module';

@NgModule({
  imports: [SharedModule, SubCountyCodeRoutingModule],
  declarations: [SubCountyCodeComponent, SubCountyCodeDetailComponent, SubCountyCodeUpdateComponent, SubCountyCodeDeleteDialogComponent],
  entryComponents: [SubCountyCodeDeleteDialogComponent],
})
export class SubCountyCodeModule {}
