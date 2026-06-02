import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShareHoldingFlagComponent } from './list/share-holding-flag.component';
import { ShareHoldingFlagDetailComponent } from './detail/share-holding-flag-detail.component';
import { ShareHoldingFlagUpdateComponent } from './update/share-holding-flag-update.component';
import { ShareHoldingFlagDeleteDialogComponent } from './delete/share-holding-flag-delete-dialog.component';
import { ShareHoldingFlagRoutingModule } from './route/share-holding-flag-routing.module';

@NgModule({
  imports: [SharedModule, ShareHoldingFlagRoutingModule],
  declarations: [
    ShareHoldingFlagComponent,
    ShareHoldingFlagDetailComponent,
    ShareHoldingFlagUpdateComponent,
    ShareHoldingFlagDeleteDialogComponent,
  ],
  entryComponents: [ShareHoldingFlagDeleteDialogComponent],
})
export class ShareHoldingFlagModule {}
