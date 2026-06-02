import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FiscalYearComponent } from './list/fiscal-year.component';
import { FiscalYearDetailComponent } from './detail/fiscal-year-detail.component';
import { FiscalYearUpdateComponent } from './update/fiscal-year-update.component';
import { FiscalYearDeleteDialogComponent } from './delete/fiscal-year-delete-dialog.component';
import { FiscalYearRoutingModule } from './route/fiscal-year-routing.module';

@NgModule({
  imports: [SharedModule, FiscalYearRoutingModule],
  declarations: [FiscalYearComponent, FiscalYearDetailComponent, FiscalYearUpdateComponent, FiscalYearDeleteDialogComponent],
  entryComponents: [FiscalYearDeleteDialogComponent],
})
export class FiscalYearModule {}
