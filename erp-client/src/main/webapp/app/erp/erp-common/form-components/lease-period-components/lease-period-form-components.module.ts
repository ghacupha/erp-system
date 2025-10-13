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
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../../../shared/shared.module';
import { LeasePeriodOptionViewComponent } from './lease-period-option-view.component';
import { FormatLeasePeriodPipe } from './format-lease-period.pipe';
import { M21LeasePeriodFormControlComponent } from './m21-lease-period-form-control.component';

@NgModule({
  declarations: [
    LeasePeriodOptionViewComponent,
    M21LeasePeriodFormControlComponent,
    FormatLeasePeriodPipe
  ],
  imports: [
    CommonModule,
    SharedModule
  ],
  exports: [
    LeasePeriodOptionViewComponent,
    M21LeasePeriodFormControlComponent,
    FormatLeasePeriodPipe
  ]
})
export class LeasePeriodFormComponentsModule {

}
