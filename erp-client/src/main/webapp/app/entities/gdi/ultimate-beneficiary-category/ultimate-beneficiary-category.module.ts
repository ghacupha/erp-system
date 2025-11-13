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
import { UltimateBeneficiaryCategoryComponent } from './list/ultimate-beneficiary-category.component';
import { UltimateBeneficiaryCategoryDetailComponent } from './detail/ultimate-beneficiary-category-detail.component';
import { UltimateBeneficiaryCategoryUpdateComponent } from './update/ultimate-beneficiary-category-update.component';
import { UltimateBeneficiaryCategoryDeleteDialogComponent } from './delete/ultimate-beneficiary-category-delete-dialog.component';
import { UltimateBeneficiaryCategoryRoutingModule } from './route/ultimate-beneficiary-category-routing.module';

@NgModule({
  imports: [SharedModule, UltimateBeneficiaryCategoryRoutingModule],
  declarations: [
    UltimateBeneficiaryCategoryComponent,
    UltimateBeneficiaryCategoryDetailComponent,
    UltimateBeneficiaryCategoryUpdateComponent,
    UltimateBeneficiaryCategoryDeleteDialogComponent,
  ],
  entryComponents: [UltimateBeneficiaryCategoryDeleteDialogComponent],
})
export class UltimateBeneficiaryCategoryModule {}
