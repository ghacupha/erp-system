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
import { CreditCardFacilityComponent } from './list/credit-card-facility.component';
import { CreditCardFacilityDetailComponent } from './detail/credit-card-facility-detail.component';
import { CreditCardFacilityUpdateComponent } from './update/credit-card-facility-update.component';
import { CreditCardFacilityDeleteDialogComponent } from './delete/credit-card-facility-delete-dialog.component';
import { CreditCardFacilityRoutingModule } from './route/credit-card-facility-routing.module';

@NgModule({
  imports: [SharedModule, CreditCardFacilityRoutingModule],
  declarations: [
    CreditCardFacilityComponent,
    CreditCardFacilityDetailComponent,
    CreditCardFacilityUpdateComponent,
    CreditCardFacilityDeleteDialogComponent,
  ],
  entryComponents: [CreditCardFacilityDeleteDialogComponent],
})
export class CreditCardFacilityModule {}
