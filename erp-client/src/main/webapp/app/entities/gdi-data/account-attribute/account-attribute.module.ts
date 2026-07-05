import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountAttributeComponent } from './list/account-attribute.component';
import { AccountAttributeDetailComponent } from './detail/account-attribute-detail.component';
import { AccountAttributeUpdateComponent } from './update/account-attribute-update.component';
import { AccountAttributeDeleteDialogComponent } from './delete/account-attribute-delete-dialog.component';
import { AccountAttributeRoutingModule } from './route/account-attribute-routing.module';

@NgModule({
  imports: [SharedModule, AccountAttributeRoutingModule],
  declarations: [
    AccountAttributeComponent,
    AccountAttributeDetailComponent,
    AccountAttributeUpdateComponent,
    AccountAttributeDeleteDialogComponent,
  ],
  entryComponents: [AccountAttributeDeleteDialogComponent],
})
export class AccountAttributeModule {}
