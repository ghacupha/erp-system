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
import { LeaseLiabilityComponent } from './list/lease-liability.component';
import { LeaseLiabilityDetailComponent } from './detail/lease-liability-detail.component';
import { LeaseLiabilityUpdateComponent } from './update/lease-liability-update.component';
import { LeaseLiabilityDeleteDialogComponent } from './delete/lease-liability-delete-dialog.component';
import { LeaseLiabilityRoutingModule } from './route/lease-liability-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';
import { LeaseLiabilityRoutingCustomModule } from './route/lease-liability-routing-custom.module';

@NgModule({
  imports: [
    SharedModule,
    LeaseLiabilityRoutingModule,
    LeaseLiabilityRoutingCustomModule,
    ErpCommonModule
  ],
  declarations: [
    LeaseLiabilityComponent,
    LeaseLiabilityDetailComponent,
    LeaseLiabilityUpdateComponent,
    LeaseLiabilityDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityDeleteDialogComponent],
})
export class LeaseLiabilityModule {}
