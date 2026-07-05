import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TALeaseRepaymentRuleComponent } from './list/ta-lease-repayment-rule.component';
import { TALeaseRepaymentRuleDetailComponent } from './detail/ta-lease-repayment-rule-detail.component';
import { TALeaseRepaymentRuleUpdateComponent } from './update/ta-lease-repayment-rule-update.component';
import { TALeaseRepaymentRuleDeleteDialogComponent } from './delete/ta-lease-repayment-rule-delete-dialog.component';
import { TALeaseRepaymentRuleRoutingModule } from './route/ta-lease-repayment-rule-routing.module';

@NgModule({
  imports: [SharedModule, TALeaseRepaymentRuleRoutingModule],
  declarations: [
    TALeaseRepaymentRuleComponent,
    TALeaseRepaymentRuleDetailComponent,
    TALeaseRepaymentRuleUpdateComponent,
    TALeaseRepaymentRuleDeleteDialogComponent,
  ],
  entryComponents: [TALeaseRepaymentRuleDeleteDialogComponent],
})
export class TALeaseRepaymentRuleModule {}
