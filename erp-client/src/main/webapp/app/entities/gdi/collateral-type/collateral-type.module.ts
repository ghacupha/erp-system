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
import { CollateralTypeComponent } from './list/collateral-type.component';
import { CollateralTypeDetailComponent } from './detail/collateral-type-detail.component';
import { CollateralTypeUpdateComponent } from './update/collateral-type-update.component';
import { CollateralTypeDeleteDialogComponent } from './delete/collateral-type-delete-dialog.component';
import { CollateralTypeRoutingModule } from './route/collateral-type-routing.module';

@NgModule({
  imports: [SharedModule, CollateralTypeRoutingModule],
  declarations: [
    CollateralTypeComponent,
    CollateralTypeDetailComponent,
    CollateralTypeUpdateComponent,
    CollateralTypeDeleteDialogComponent,
  ],
  entryComponents: [CollateralTypeDeleteDialogComponent],
})
export class CollateralTypeModule {}
