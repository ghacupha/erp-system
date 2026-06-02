import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentAccountComponent } from './list/prepayment-account.component';
import { PrepaymentAccountDetailComponent } from './detail/prepayment-account-detail.component';
import { PrepaymentAccountUpdateComponent } from './update/prepayment-account-update.component';
import { PrepaymentAccountDeleteDialogComponent } from './delete/prepayment-account-delete-dialog.component';
import { PrepaymentAccountRoutingModule } from './route/prepayment-account-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentAccountRoutingModule],
  declarations: [
    PrepaymentAccountComponent,
    PrepaymentAccountDetailComponent,
    PrepaymentAccountUpdateComponent,
    PrepaymentAccountDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentAccountDeleteDialogComponent],
})
export class PrepaymentAccountModule {}
