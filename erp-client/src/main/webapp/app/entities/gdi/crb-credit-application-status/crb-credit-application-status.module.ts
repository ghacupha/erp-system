import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbCreditApplicationStatusComponent } from './list/crb-credit-application-status.component';
import { CrbCreditApplicationStatusDetailComponent } from './detail/crb-credit-application-status-detail.component';
import { CrbCreditApplicationStatusUpdateComponent } from './update/crb-credit-application-status-update.component';
import { CrbCreditApplicationStatusDeleteDialogComponent } from './delete/crb-credit-application-status-delete-dialog.component';
import { CrbCreditApplicationStatusRoutingModule } from './route/crb-credit-application-status-routing.module';

@NgModule({
  imports: [SharedModule, CrbCreditApplicationStatusRoutingModule],
  declarations: [
    CrbCreditApplicationStatusComponent,
    CrbCreditApplicationStatusDetailComponent,
    CrbCreditApplicationStatusUpdateComponent,
    CrbCreditApplicationStatusDeleteDialogComponent,
  ],
  entryComponents: [CrbCreditApplicationStatusDeleteDialogComponent],
})
export class CrbCreditApplicationStatusModule {}
