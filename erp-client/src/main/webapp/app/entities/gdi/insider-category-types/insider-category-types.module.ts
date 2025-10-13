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
import { InsiderCategoryTypesComponent } from './list/insider-category-types.component';
import { InsiderCategoryTypesDetailComponent } from './detail/insider-category-types-detail.component';
import { InsiderCategoryTypesUpdateComponent } from './update/insider-category-types-update.component';
import { InsiderCategoryTypesDeleteDialogComponent } from './delete/insider-category-types-delete-dialog.component';
import { InsiderCategoryTypesRoutingModule } from './route/insider-category-types-routing.module';

@NgModule({
  imports: [SharedModule, InsiderCategoryTypesRoutingModule],
  declarations: [
    InsiderCategoryTypesComponent,
    InsiderCategoryTypesDetailComponent,
    InsiderCategoryTypesUpdateComponent,
    InsiderCategoryTypesDeleteDialogComponent,
  ],
  entryComponents: [InsiderCategoryTypesDeleteDialogComponent],
})
export class InsiderCategoryTypesModule {}
