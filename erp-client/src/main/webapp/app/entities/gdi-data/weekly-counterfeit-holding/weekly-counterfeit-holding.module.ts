import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WeeklyCounterfeitHoldingComponent } from './list/weekly-counterfeit-holding.component';
import { WeeklyCounterfeitHoldingDetailComponent } from './detail/weekly-counterfeit-holding-detail.component';
import { WeeklyCounterfeitHoldingUpdateComponent } from './update/weekly-counterfeit-holding-update.component';
import { WeeklyCounterfeitHoldingDeleteDialogComponent } from './delete/weekly-counterfeit-holding-delete-dialog.component';
import { WeeklyCounterfeitHoldingRoutingModule } from './route/weekly-counterfeit-holding-routing.module';

@NgModule({
  imports: [SharedModule, WeeklyCounterfeitHoldingRoutingModule],
  declarations: [
    WeeklyCounterfeitHoldingComponent,
    WeeklyCounterfeitHoldingDetailComponent,
    WeeklyCounterfeitHoldingUpdateComponent,
    WeeklyCounterfeitHoldingDeleteDialogComponent,
  ],
  entryComponents: [WeeklyCounterfeitHoldingDeleteDialogComponent],
})
export class WeeklyCounterfeitHoldingModule {}
