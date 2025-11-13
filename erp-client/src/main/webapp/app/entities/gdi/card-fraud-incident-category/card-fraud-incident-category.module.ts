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
import { CardFraudIncidentCategoryComponent } from './list/card-fraud-incident-category.component';
import { CardFraudIncidentCategoryDetailComponent } from './detail/card-fraud-incident-category-detail.component';
import { CardFraudIncidentCategoryUpdateComponent } from './update/card-fraud-incident-category-update.component';
import { CardFraudIncidentCategoryDeleteDialogComponent } from './delete/card-fraud-incident-category-delete-dialog.component';
import { CardFraudIncidentCategoryRoutingModule } from './route/card-fraud-incident-category-routing.module';

@NgModule({
  imports: [SharedModule, CardFraudIncidentCategoryRoutingModule],
  declarations: [
    CardFraudIncidentCategoryComponent,
    CardFraudIncidentCategoryDetailComponent,
    CardFraudIncidentCategoryUpdateComponent,
    CardFraudIncidentCategoryDeleteDialogComponent,
  ],
  entryComponents: [CardFraudIncidentCategoryDeleteDialogComponent],
})
export class CardFraudIncidentCategoryModule {}
