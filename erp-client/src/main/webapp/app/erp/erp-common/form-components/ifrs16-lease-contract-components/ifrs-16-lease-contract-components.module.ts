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
import { M21Ifrs16LeaseFormControlComponent } from './m21-ifrs16-lease-form-control.component';
import {
  FormatIfrs16LeaseContractPipe
} from './format-ifrs16-lease-contract.pipe';
import { Ifrs16LeaseContractOptionViewComponent } from './ifrs16-lease-contract-option-view.component';

@NgModule({
  imports: [CommonModule, SharedModule],
  exports: [
    M21Ifrs16LeaseFormControlComponent,
    FormatIfrs16LeaseContractPipe,
    Ifrs16LeaseContractOptionViewComponent
  ],
  declarations: [
    M21Ifrs16LeaseFormControlComponent,
    FormatIfrs16LeaseContractPipe,
    Ifrs16LeaseContractOptionViewComponent
  ],
})
export class Ifrs16LeaseContractComponentsModule {
}
