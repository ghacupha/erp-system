import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TAAmortizationRuleComponent } from './list/ta-amortization-rule.component';
import { TAAmortizationRuleDetailComponent } from './detail/ta-amortization-rule-detail.component';
import { TAAmortizationRuleUpdateComponent } from './update/ta-amortization-rule-update.component';
import { TAAmortizationRuleDeleteDialogComponent } from './delete/ta-amortization-rule-delete-dialog.component';
import { TAAmortizationRuleRoutingModule } from './route/ta-amortization-rule-routing.module';

@NgModule({
  imports: [SharedModule, TAAmortizationRuleRoutingModule],
  declarations: [
    TAAmortizationRuleComponent,
    TAAmortizationRuleDetailComponent,
    TAAmortizationRuleUpdateComponent,
    TAAmortizationRuleDeleteDialogComponent,
  ],
  entryComponents: [TAAmortizationRuleDeleteDialogComponent],
})
export class TAAmortizationRuleModule {}
