import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TaxRuleComponent } from './list/tax-rule.component';
import { TaxRuleDetailComponent } from './detail/tax-rule-detail.component';
import { TaxRuleUpdateComponent } from './update/tax-rule-update.component';
import { TaxRuleDeleteDialogComponent } from './delete/tax-rule-delete-dialog.component';
import { TaxRuleRoutingModule } from './route/tax-rule-routing.module';

@NgModule({
  imports: [SharedModule, TaxRuleRoutingModule],
  declarations: [TaxRuleComponent, TaxRuleDetailComponent, TaxRuleUpdateComponent, TaxRuleDeleteDialogComponent],
  entryComponents: [TaxRuleDeleteDialogComponent],
})
export class ErpServiceTaxRuleModule {}
