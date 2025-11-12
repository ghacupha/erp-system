///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
