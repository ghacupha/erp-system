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
import { CrbAmountCategoryBandComponent } from './list/crb-amount-category-band.component';
import { CrbAmountCategoryBandDetailComponent } from './detail/crb-amount-category-band-detail.component';
import { CrbAmountCategoryBandUpdateComponent } from './update/crb-amount-category-band-update.component';
import { CrbAmountCategoryBandDeleteDialogComponent } from './delete/crb-amount-category-band-delete-dialog.component';
import { CrbAmountCategoryBandRoutingModule } from './route/crb-amount-category-band-routing.module';

@NgModule({
  imports: [SharedModule, CrbAmountCategoryBandRoutingModule],
  declarations: [
    CrbAmountCategoryBandComponent,
    CrbAmountCategoryBandDetailComponent,
    CrbAmountCategoryBandUpdateComponent,
    CrbAmountCategoryBandDeleteDialogComponent,
  ],
  entryComponents: [CrbAmountCategoryBandDeleteDialogComponent],
})
export class CrbAmountCategoryBandModule {}
