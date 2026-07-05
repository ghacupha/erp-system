import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardStatusFlagComponent } from './list/card-status-flag.component';
import { CardStatusFlagDetailComponent } from './detail/card-status-flag-detail.component';
import { CardStatusFlagUpdateComponent } from './update/card-status-flag-update.component';
import { CardStatusFlagDeleteDialogComponent } from './delete/card-status-flag-delete-dialog.component';
import { CardStatusFlagRoutingModule } from './route/card-status-flag-routing.module';

@NgModule({
  imports: [SharedModule, CardStatusFlagRoutingModule],
  declarations: [
    CardStatusFlagComponent,
    CardStatusFlagDetailComponent,
    CardStatusFlagUpdateComponent,
    CardStatusFlagDeleteDialogComponent,
  ],
  entryComponents: [CardStatusFlagDeleteDialogComponent],
})
export class CardStatusFlagModule {}
