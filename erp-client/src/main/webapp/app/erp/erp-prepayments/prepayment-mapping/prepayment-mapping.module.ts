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
import { PrepaymentMappingComponent } from './list/prepayment-mapping.component';
import { PrepaymentMappingDetailComponent } from './detail/prepayment-mapping-detail.component';
import { PrepaymentMappingUpdateComponent } from './update/prepayment-mapping-update.component';
import { PrepaymentMappingDeleteDialogComponent } from './delete/prepayment-mapping-delete-dialog.component';
import { PrepaymentMappingRoutingModule } from './route/prepayment-mapping-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentMappingRoutingModule],
  declarations: [
    PrepaymentMappingComponent,
    PrepaymentMappingDetailComponent,
    PrepaymentMappingUpdateComponent,
    PrepaymentMappingDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentMappingDeleteDialogComponent],
})
export class PrepaymentMappingModule {}
