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
import { SharedModule } from '../../../../shared/shared.module';
import { ServiceOutletOptionViewComponent } from './service-outlet-option-view.component';
import { FormatServiceOutletPipe } from './format-service-outlet.pipe';
import { M21ServiceOutletFormControlComponent } from './m21-service-outlet-form-control.component';
import { M2mServiceOutletFormControlComponent } from './m2m-service-outlet-form-control.component';

@NgModule({
  declarations: [
    ServiceOutletOptionViewComponent,
    FormatServiceOutletPipe,
    M21ServiceOutletFormControlComponent,
    M2mServiceOutletFormControlComponent,
  ],
  imports: [SharedModule],
  exports: [
    M21ServiceOutletFormControlComponent,
    M2mServiceOutletFormControlComponent,
  ]
})
export class ServiceOutletFormComponentsModule {
}
