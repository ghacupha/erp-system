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
import { StoreModule } from '@ngrx/store';
import { SharedModule } from 'app/shared/shared.module';
import { Ifrs16LeaseContractComponentsModule } from '../../erp-common/form-components/ifrs16-lease-contract-components/ifrs-16-lease-contract-components.module';
import { LeasePaymentUploadFormComponentsModule } from '../../erp-common/form-components/lease-payment-upload-components/lease-payment-upload-form-components.module';
import { LiabilityEnumerationComponent } from './list/liability-enumeration.component';
import { LiabilityEnumerationUpdateComponent } from './update/liability-enumeration-update.component';
import { LiabilityEnumerationRoutingModule } from './route/liability-enumeration-routing.module';
import { PresentValueEnumerationComponent } from './present-values/present-value-enumeration.component';
import { liabilityEnumerationFeatureKey, liabilityEnumerationReducer } from './state/liability-enumeration.reducer';

@NgModule({
  imports: [
    SharedModule,
    LiabilityEnumerationRoutingModule,
    Ifrs16LeaseContractComponentsModule,
    LeasePaymentUploadFormComponentsModule,
    StoreModule.forFeature(liabilityEnumerationFeatureKey, liabilityEnumerationReducer),
  ],
  declarations: [LiabilityEnumerationComponent, LiabilityEnumerationUpdateComponent, PresentValueEnumerationComponent],
})
export class LiabilityEnumerationModule {}
