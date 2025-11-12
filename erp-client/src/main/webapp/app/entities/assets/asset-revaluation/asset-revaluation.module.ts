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
import { AssetRevaluationComponent } from './list/asset-revaluation.component';
import { AssetRevaluationDetailComponent } from './detail/asset-revaluation-detail.component';
import { AssetRevaluationUpdateComponent } from './update/asset-revaluation-update.component';
import { AssetRevaluationDeleteDialogComponent } from './delete/asset-revaluation-delete-dialog.component';
import { AssetRevaluationRoutingModule } from './route/asset-revaluation-routing.module';

@NgModule({
  imports: [SharedModule, AssetRevaluationRoutingModule],
  declarations: [
    AssetRevaluationComponent,
    AssetRevaluationDetailComponent,
    AssetRevaluationUpdateComponent,
    AssetRevaluationDeleteDialogComponent,
  ],
  entryComponents: [AssetRevaluationDeleteDialogComponent],
})
export class AssetRevaluationModule {}
