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
import { IFRS16LeaseContractComponent } from './list/ifrs-16-lease-contract.component';
import { IFRS16LeaseContractDetailComponent } from './detail/ifrs-16-lease-contract-detail.component';
import { IFRS16LeaseContractUpdateComponent } from './update/ifrs-16-lease-contract-update.component';
import { IFRS16LeaseContractDeleteDialogComponent } from './delete/ifrs-16-lease-contract-delete-dialog.component';
import { IFRS16LeaseContractRoutingModule } from './route/ifrs-16-lease-contract-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';
import { Ifrs16LeaseContractRoutingCustomModule } from './route/ifrs-16-lease-contract-routing-custom.module';

@NgModule({
  imports: [
    SharedModule,
    IFRS16LeaseContractRoutingModule,
    Ifrs16LeaseContractRoutingCustomModule,
    ErpCommonModule],
  declarations: [
    IFRS16LeaseContractComponent,
    IFRS16LeaseContractDetailComponent,
    IFRS16LeaseContractUpdateComponent,
    IFRS16LeaseContractDeleteDialogComponent,
  ],
  entryComponents: [IFRS16LeaseContractDeleteDialogComponent],
})
export class IFRS16LeaseContractModule {}
