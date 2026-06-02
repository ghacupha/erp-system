import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountOwnershipTypeComponent } from './list/account-ownership-type.component';
import { AccountOwnershipTypeDetailComponent } from './detail/account-ownership-type-detail.component';
import { AccountOwnershipTypeRoutingModule } from './route/account-ownership-type-routing.module';

@NgModule({
  imports: [SharedModule, AccountOwnershipTypeRoutingModule],
  declarations: [AccountOwnershipTypeComponent, AccountOwnershipTypeDetailComponent],
})
export class AccountOwnershipTypeModule {}
