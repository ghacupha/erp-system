import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SecurityClearanceComponent } from './list/security-clearance.component';
import { SecurityClearanceDetailComponent } from './detail/security-clearance-detail.component';
import { SecurityClearanceUpdateComponent } from './update/security-clearance-update.component';
import { SecurityClearanceDeleteDialogComponent } from './delete/security-clearance-delete-dialog.component';
import { SecurityClearanceRoutingModule } from './route/security-clearance-routing.module';

@NgModule({
  imports: [SharedModule, SecurityClearanceRoutingModule],
  declarations: [
    SecurityClearanceComponent,
    SecurityClearanceDetailComponent,
    SecurityClearanceUpdateComponent,
    SecurityClearanceDeleteDialogComponent,
  ],
  entryComponents: [SecurityClearanceDeleteDialogComponent],
})
export class SecurityClearanceModule {}
