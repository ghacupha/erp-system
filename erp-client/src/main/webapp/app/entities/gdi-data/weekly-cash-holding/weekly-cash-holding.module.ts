import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WeeklyCashHoldingComponent } from './list/weekly-cash-holding.component';
import { WeeklyCashHoldingDetailComponent } from './detail/weekly-cash-holding-detail.component';
import { WeeklyCashHoldingUpdateComponent } from './update/weekly-cash-holding-update.component';
import { WeeklyCashHoldingDeleteDialogComponent } from './delete/weekly-cash-holding-delete-dialog.component';
import { WeeklyCashHoldingRoutingModule } from './route/weekly-cash-holding-routing.module';

@NgModule({
  imports: [SharedModule, WeeklyCashHoldingRoutingModule],
  declarations: [
    WeeklyCashHoldingComponent,
    WeeklyCashHoldingDetailComponent,
    WeeklyCashHoldingUpdateComponent,
    WeeklyCashHoldingDeleteDialogComponent,
  ],
  entryComponents: [WeeklyCashHoldingDeleteDialogComponent],
})
export class WeeklyCashHoldingModule {}
