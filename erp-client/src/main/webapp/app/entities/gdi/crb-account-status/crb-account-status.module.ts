import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbAccountStatusComponent } from './list/crb-account-status.component';
import { CrbAccountStatusDetailComponent } from './detail/crb-account-status-detail.component';
import { CrbAccountStatusUpdateComponent } from './update/crb-account-status-update.component';
import { CrbAccountStatusDeleteDialogComponent } from './delete/crb-account-status-delete-dialog.component';
import { CrbAccountStatusRoutingModule } from './route/crb-account-status-routing.module';

@NgModule({
  imports: [SharedModule, CrbAccountStatusRoutingModule],
  declarations: [
    CrbAccountStatusComponent,
    CrbAccountStatusDetailComponent,
    CrbAccountStatusUpdateComponent,
    CrbAccountStatusDeleteDialogComponent,
  ],
  entryComponents: [CrbAccountStatusDeleteDialogComponent],
})
export class CrbAccountStatusModule {}
