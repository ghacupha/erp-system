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
import { LeaseAmortizationCalculationComponent } from './list/lease-amortization-calculation.component';
import { LeaseAmortizationCalculationDetailComponent } from './detail/lease-amortization-calculation-detail.component';
import { LeaseAmortizationCalculationUpdateComponent } from './update/lease-amortization-calculation-update.component';
import { LeaseAmortizationCalculationDeleteDialogComponent } from './delete/lease-amortization-calculation-delete-dialog.component';
import { LeaseAmortizationCalculationRoutingModule } from './route/lease-amortization-calculation-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';
import { LeaseAmortizationCalculationRoutingCustomModule } from './route/lease-amortization-calculation-routing-custom.module';

@NgModule({
  imports: [
    SharedModule,
    LeaseAmortizationCalculationRoutingModule,
    LeaseAmortizationCalculationRoutingCustomModule,
    ErpCommonModule
  ],
  declarations: [
    LeaseAmortizationCalculationComponent,
    LeaseAmortizationCalculationDetailComponent,
    LeaseAmortizationCalculationUpdateComponent,
    LeaseAmortizationCalculationDeleteDialogComponent,
  ],
  entryComponents: [LeaseAmortizationCalculationDeleteDialogComponent],
})
export class LeaseAmortizationCalculationModule {}
