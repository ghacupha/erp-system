import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TALeaseInterestAccrualRuleComponent } from './list/ta-lease-interest-accrual-rule.component';
import { TALeaseInterestAccrualRuleDetailComponent } from './detail/ta-lease-interest-accrual-rule-detail.component';
import { TALeaseInterestAccrualRuleUpdateComponent } from './update/ta-lease-interest-accrual-rule-update.component';
import { TALeaseInterestAccrualRuleDeleteDialogComponent } from './delete/ta-lease-interest-accrual-rule-delete-dialog.component';
import { TALeaseInterestAccrualRuleRoutingModule } from './route/ta-lease-interest-accrual-rule-routing.module';

@NgModule({
  imports: [SharedModule, TALeaseInterestAccrualRuleRoutingModule],
  declarations: [
    TALeaseInterestAccrualRuleComponent,
    TALeaseInterestAccrualRuleDetailComponent,
    TALeaseInterestAccrualRuleUpdateComponent,
    TALeaseInterestAccrualRuleDeleteDialogComponent,
  ],
  entryComponents: [TALeaseInterestAccrualRuleDeleteDialogComponent],
})
export class TALeaseInterestAccrualRuleModule {}
