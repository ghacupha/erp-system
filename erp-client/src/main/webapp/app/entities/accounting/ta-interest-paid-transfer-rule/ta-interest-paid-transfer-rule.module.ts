import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TAInterestPaidTransferRuleComponent } from './list/ta-interest-paid-transfer-rule.component';
import { TAInterestPaidTransferRuleDetailComponent } from './detail/ta-interest-paid-transfer-rule-detail.component';
import { TAInterestPaidTransferRuleUpdateComponent } from './update/ta-interest-paid-transfer-rule-update.component';
import { TAInterestPaidTransferRuleDeleteDialogComponent } from './delete/ta-interest-paid-transfer-rule-delete-dialog.component';
import { TAInterestPaidTransferRuleRoutingModule } from './route/ta-interest-paid-transfer-rule-routing.module';

@NgModule({
  imports: [SharedModule, TAInterestPaidTransferRuleRoutingModule],
  declarations: [
    TAInterestPaidTransferRuleComponent,
    TAInterestPaidTransferRuleDetailComponent,
    TAInterestPaidTransferRuleUpdateComponent,
    TAInterestPaidTransferRuleDeleteDialogComponent,
  ],
  entryComponents: [TAInterestPaidTransferRuleDeleteDialogComponent],
})
export class TAInterestPaidTransferRuleModule {}
