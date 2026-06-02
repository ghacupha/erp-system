import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountTypeComponent } from './list/account-type.component';
import { AccountTypeDetailComponent } from './detail/account-type-detail.component';
import { AccountTypeUpdateComponent } from './update/account-type-update.component';
import { AccountTypeDeleteDialogComponent } from './delete/account-type-delete-dialog.component';
import { AccountTypeRoutingModule } from './route/account-type-routing.module';

@NgModule({
  imports: [SharedModule, AccountTypeRoutingModule],
  declarations: [AccountTypeComponent, AccountTypeDetailComponent, AccountTypeUpdateComponent, AccountTypeDeleteDialogComponent],
  entryComponents: [AccountTypeDeleteDialogComponent],
})
export class AccountTypeModule {}
