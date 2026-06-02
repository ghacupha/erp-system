import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AnticipatedMaturityPerioodComponent } from './list/anticipated-maturity-periood.component';
import { AnticipatedMaturityPerioodDetailComponent } from './detail/anticipated-maturity-periood-detail.component';
import { AnticipatedMaturityPerioodUpdateComponent } from './update/anticipated-maturity-periood-update.component';
import { AnticipatedMaturityPerioodDeleteDialogComponent } from './delete/anticipated-maturity-periood-delete-dialog.component';
import { AnticipatedMaturityPerioodRoutingModule } from './route/anticipated-maturity-periood-routing.module';

@NgModule({
  imports: [SharedModule, AnticipatedMaturityPerioodRoutingModule],
  declarations: [
    AnticipatedMaturityPerioodComponent,
    AnticipatedMaturityPerioodDetailComponent,
    AnticipatedMaturityPerioodUpdateComponent,
    AnticipatedMaturityPerioodDeleteDialogComponent,
  ],
  entryComponents: [AnticipatedMaturityPerioodDeleteDialogComponent],
})
export class AnticipatedMaturityPerioodModule {}
