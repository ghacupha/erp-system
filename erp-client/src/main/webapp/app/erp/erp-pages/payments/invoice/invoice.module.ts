///
/// Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
/// Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
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
import { InvoiceComponent } from './list/invoice.component';
import { InvoiceDetailComponent } from './detail/invoice-detail.component';
import { InvoiceUpdateComponent } from './update/invoice-update.component';
import { InvoiceDeleteDialogComponent } from './delete/invoice-delete-dialog.component';
import { InvoiceRoutingModule } from './route/invoice-routing.module';
import { ErpCommonModule } from '../../../erp-common/erp-common.module';

@NgModule({
  imports: [SharedModule, InvoiceRoutingModule, ErpCommonModule],
  declarations: [InvoiceComponent, InvoiceDetailComponent, InvoiceUpdateComponent, InvoiceDeleteDialogComponent],
  entryComponents: [InvoiceDeleteDialogComponent],
})
export class ErpServiceInvoiceModule {}
