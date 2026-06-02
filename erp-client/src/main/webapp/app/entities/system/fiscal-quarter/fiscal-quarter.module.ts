import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FiscalQuarterComponent } from './list/fiscal-quarter.component';
import { FiscalQuarterDetailComponent } from './detail/fiscal-quarter-detail.component';
import { FiscalQuarterUpdateComponent } from './update/fiscal-quarter-update.component';
import { FiscalQuarterDeleteDialogComponent } from './delete/fiscal-quarter-delete-dialog.component';
import { FiscalQuarterRoutingModule } from './route/fiscal-quarter-routing.module';

@NgModule({
  imports: [SharedModule, FiscalQuarterRoutingModule],
  declarations: [FiscalQuarterComponent, FiscalQuarterDetailComponent, FiscalQuarterUpdateComponent, FiscalQuarterDeleteDialogComponent],
  entryComponents: [FiscalQuarterDeleteDialogComponent],
})
export class FiscalQuarterModule {}
