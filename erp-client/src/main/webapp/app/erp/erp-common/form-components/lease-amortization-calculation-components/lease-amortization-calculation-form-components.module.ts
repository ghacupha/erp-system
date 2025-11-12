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
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../../../shared/shared.module';
import { FormatLeaseAmortizationCalculationPipe } from './format-lease-amortization-calculation.pipe';
import { M21LeaseAmortizationCalculationFormControlComponent } from './m21-lease-amortization-calculation-form-control.component';
import { LeaseAmortizationCalculationOptionViewComponent } from './lease-amortization-calculation-option-view.component';

@NgModule({
  declarations: [
    LeaseAmortizationCalculationOptionViewComponent,
    FormatLeaseAmortizationCalculationPipe,
    M21LeaseAmortizationCalculationFormControlComponent
  ],
  imports: [
    CommonModule,
    SharedModule
  ],
  exports: [
    LeaseAmortizationCalculationOptionViewComponent,
    M21LeaseAmortizationCalculationFormControlComponent,
    FormatLeaseAmortizationCalculationPipe
  ]
})
export class LeaseAmortizationCalculationFormComponentsModule {
}
