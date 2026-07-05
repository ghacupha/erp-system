import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FiscalMonthComponent } from './list/fiscal-month.component';
import { FiscalMonthDetailComponent } from './detail/fiscal-month-detail.component';
import { FiscalMonthUpdateComponent } from './update/fiscal-month-update.component';
import { FiscalMonthDeleteDialogComponent } from './delete/fiscal-month-delete-dialog.component';
import { FiscalMonthRoutingModule } from './route/fiscal-month-routing.module';

@NgModule({
  imports: [SharedModule, FiscalMonthRoutingModule],
  declarations: [FiscalMonthComponent, FiscalMonthDetailComponent, FiscalMonthUpdateComponent, FiscalMonthDeleteDialogComponent],
  entryComponents: [FiscalMonthDeleteDialogComponent],
})
export class FiscalMonthModule {}
