import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { TaxRuleComponent } from './tax-rule.component';
import { TaxRuleDetailComponent } from './tax-rule-detail.component';
import { TaxRuleUpdateComponent } from './tax-rule-update.component';
import { TaxRuleDeleteDialogComponent } from './tax-rule-delete-dialog.component';
import { taxRuleRoute } from './tax-rule.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(taxRuleRoute)],
  declarations: [TaxRuleComponent, TaxRuleDetailComponent, TaxRuleUpdateComponent, TaxRuleDeleteDialogComponent],
  entryComponents: [TaxRuleDeleteDialogComponent],
})
export class ErpServiceTaxRuleModule {}
