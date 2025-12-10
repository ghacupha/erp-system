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
