import {NgModule} from "@angular/core";
import {EffectsModule} from "@ngrx/effects";
import {StoreModule} from "@ngrx/store";
import * as fromDealerInvoiceWorkflows from "./reducers/dealer-invoice-workflows-status.reducer";
import * as fromDealerWorkflows from "./reducers/dealer-workflows-status.reducer";
import * as fromPaymentUpdates from "./reducers/update-menu-status.reducer";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {DealerPaymentsEffects} from "../erp-pages/dealers/dealer/service/dealer-payments.effects";

@NgModule({
  imports: [
    EffectsModule.forRoot([DealerPaymentsEffects]),
    StoreModule.forFeature('recordDealerInvoiceWorkflows', fromDealerInvoiceWorkflows.dealerInvoiceWorkflowStateReducer),
    StoreModule.forFeature('paymentToDealerWorkflows', fromDealerWorkflows.dealerWorkflowStateReducer),
    StoreModule.forFeature('paymentUpdateForm', fromPaymentUpdates.paymentUpdateStateReducer),
    StoreModule.forRoot({}, {runtimeChecks: {
        strictStateImmutability: true,
        strictActionImmutability: true,
        strictStateSerializability: false,
        strictActionSerializability: false,
        strictActionWithinNgZone: true,
        strictActionTypeUniqueness: true,
      }}),
    StoreDevtoolsModule.instrument({
      name: 'ERP App States',
      maxAge: 25, // Retains last 25 states
    }),
  ],
  exports: [
    EffectsModule,
    StoreModule,
    StoreDevtoolsModule,
  ]
})
export class ErpStoreModule {}
