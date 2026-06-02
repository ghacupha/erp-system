import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LegalStatusComponent } from './list/legal-status.component';
import { LegalStatusDetailComponent } from './detail/legal-status-detail.component';
import { LegalStatusUpdateComponent } from './update/legal-status-update.component';
import { LegalStatusDeleteDialogComponent } from './delete/legal-status-delete-dialog.component';
import { LegalStatusRoutingModule } from './route/legal-status-routing.module';

@NgModule({
  imports: [SharedModule, LegalStatusRoutingModule],
  declarations: [LegalStatusComponent, LegalStatusDetailComponent, LegalStatusUpdateComponent, LegalStatusDeleteDialogComponent],
  entryComponents: [LegalStatusDeleteDialogComponent],
})
export class LegalStatusModule {}
