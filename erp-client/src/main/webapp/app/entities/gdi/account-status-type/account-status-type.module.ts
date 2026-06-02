import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountStatusTypeComponent } from './list/account-status-type.component';
import { AccountStatusTypeDetailComponent } from './detail/account-status-type-detail.component';
import { AccountStatusTypeUpdateComponent } from './update/account-status-type-update.component';
import { AccountStatusTypeDeleteDialogComponent } from './delete/account-status-type-delete-dialog.component';
import { AccountStatusTypeRoutingModule } from './route/account-status-type-routing.module';

@NgModule({
  imports: [SharedModule, AccountStatusTypeRoutingModule],
  declarations: [
    AccountStatusTypeComponent,
    AccountStatusTypeDetailComponent,
    AccountStatusTypeUpdateComponent,
    AccountStatusTypeDeleteDialogComponent,
  ],
  entryComponents: [AccountStatusTypeDeleteDialogComponent],
})
export class AccountStatusTypeModule {}
