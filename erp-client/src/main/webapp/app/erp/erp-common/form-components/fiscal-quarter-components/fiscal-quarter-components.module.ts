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
import { SharedModule } from '../../../../shared/shared.module';
import { FiscalQuarterOptionViewComponent } from './fiscal-quarter-option-view.component';
import { FormatFiscalQuarterPipe } from './format-fiscal-quarter.pipe';
import { M21FiscalQuarterFormControlComponent } from './m21-fiscal-quarter-form-control.component';

@NgModule({
  declarations: [
    FiscalQuarterOptionViewComponent,
    FormatFiscalQuarterPipe,
    M21FiscalQuarterFormControlComponent
  ],
  imports: [SharedModule],
  exports: [
    FiscalQuarterOptionViewComponent,
    FormatFiscalQuarterPipe,
    M21FiscalQuarterFormControlComponent
  ]
})
export class FiscalQuarterComponentsModule {
}
