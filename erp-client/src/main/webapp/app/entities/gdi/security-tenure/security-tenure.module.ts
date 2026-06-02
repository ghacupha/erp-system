import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SecurityTenureComponent } from './list/security-tenure.component';
import { SecurityTenureDetailComponent } from './detail/security-tenure-detail.component';
import { SecurityTenureUpdateComponent } from './update/security-tenure-update.component';
import { SecurityTenureDeleteDialogComponent } from './delete/security-tenure-delete-dialog.component';
import { SecurityTenureRoutingModule } from './route/security-tenure-routing.module';

@NgModule({
  imports: [SharedModule, SecurityTenureRoutingModule],
  declarations: [
    SecurityTenureComponent,
    SecurityTenureDetailComponent,
    SecurityTenureUpdateComponent,
    SecurityTenureDeleteDialogComponent,
  ],
  entryComponents: [SecurityTenureDeleteDialogComponent],
})
export class SecurityTenureModule {}
