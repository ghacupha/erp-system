import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountBalanceComponent } from './list/account-balance.component';
import { AccountBalanceDetailComponent } from './detail/account-balance-detail.component';
import { AccountBalanceUpdateComponent } from './update/account-balance-update.component';
import { AccountBalanceDeleteDialogComponent } from './delete/account-balance-delete-dialog.component';
import { AccountBalanceRoutingModule } from './route/account-balance-routing.module';

@NgModule({
  imports: [SharedModule, AccountBalanceRoutingModule],
  declarations: [
    AccountBalanceComponent,
    AccountBalanceDetailComponent,
    AccountBalanceUpdateComponent,
    AccountBalanceDeleteDialogComponent,
  ],
  entryComponents: [AccountBalanceDeleteDialogComponent],
})
export class AccountBalanceModule {}
