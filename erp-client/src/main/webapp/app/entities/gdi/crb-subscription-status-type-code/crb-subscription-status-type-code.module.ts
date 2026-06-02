import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbSubscriptionStatusTypeCodeComponent } from './list/crb-subscription-status-type-code.component';
import { CrbSubscriptionStatusTypeCodeDetailComponent } from './detail/crb-subscription-status-type-code-detail.component';
import { CrbSubscriptionStatusTypeCodeUpdateComponent } from './update/crb-subscription-status-type-code-update.component';
import { CrbSubscriptionStatusTypeCodeDeleteDialogComponent } from './delete/crb-subscription-status-type-code-delete-dialog.component';
import { CrbSubscriptionStatusTypeCodeRoutingModule } from './route/crb-subscription-status-type-code-routing.module';

@NgModule({
  imports: [SharedModule, CrbSubscriptionStatusTypeCodeRoutingModule],
  declarations: [
    CrbSubscriptionStatusTypeCodeComponent,
    CrbSubscriptionStatusTypeCodeDetailComponent,
    CrbSubscriptionStatusTypeCodeUpdateComponent,
    CrbSubscriptionStatusTypeCodeDeleteDialogComponent,
  ],
  entryComponents: [CrbSubscriptionStatusTypeCodeDeleteDialogComponent],
})
export class CrbSubscriptionStatusTypeCodeModule {}
