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
import { CollateralInformationComponent } from './list/collateral-information.component';
import { CollateralInformationDetailComponent } from './detail/collateral-information-detail.component';
import { CollateralInformationUpdateComponent } from './update/collateral-information-update.component';
import { CollateralInformationDeleteDialogComponent } from './delete/collateral-information-delete-dialog.component';
import { CollateralInformationRoutingModule } from './route/collateral-information-routing.module';

@NgModule({
  imports: [SharedModule, CollateralInformationRoutingModule],
  declarations: [
    CollateralInformationComponent,
    CollateralInformationDetailComponent,
    CollateralInformationUpdateComponent,
    CollateralInformationDeleteDialogComponent,
  ],
  entryComponents: [CollateralInformationDeleteDialogComponent],
})
export class CollateralInformationModule {}
