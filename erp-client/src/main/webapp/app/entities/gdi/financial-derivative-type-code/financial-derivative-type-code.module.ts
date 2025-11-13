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
import { SharedModule } from 'app/shared/shared.module';
import { FinancialDerivativeTypeCodeComponent } from './list/financial-derivative-type-code.component';
import { FinancialDerivativeTypeCodeDetailComponent } from './detail/financial-derivative-type-code-detail.component';
import { FinancialDerivativeTypeCodeUpdateComponent } from './update/financial-derivative-type-code-update.component';
import { FinancialDerivativeTypeCodeDeleteDialogComponent } from './delete/financial-derivative-type-code-delete-dialog.component';
import { FinancialDerivativeTypeCodeRoutingModule } from './route/financial-derivative-type-code-routing.module';

@NgModule({
  imports: [SharedModule, FinancialDerivativeTypeCodeRoutingModule],
  declarations: [
    FinancialDerivativeTypeCodeComponent,
    FinancialDerivativeTypeCodeDetailComponent,
    FinancialDerivativeTypeCodeUpdateComponent,
    FinancialDerivativeTypeCodeDeleteDialogComponent,
  ],
  entryComponents: [FinancialDerivativeTypeCodeDeleteDialogComponent],
})
export class FinancialDerivativeTypeCodeModule {}
