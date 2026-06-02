import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountAttributeMetadataComponent } from './list/account-attribute-metadata.component';
import { AccountAttributeMetadataDetailComponent } from './detail/account-attribute-metadata-detail.component';
import { AccountAttributeMetadataUpdateComponent } from './update/account-attribute-metadata-update.component';
import { AccountAttributeMetadataDeleteDialogComponent } from './delete/account-attribute-metadata-delete-dialog.component';
import { AccountAttributeMetadataRoutingModule } from './route/account-attribute-metadata-routing.module';

@NgModule({
  imports: [SharedModule, AccountAttributeMetadataRoutingModule],
  declarations: [
    AccountAttributeMetadataComponent,
    AccountAttributeMetadataDetailComponent,
    AccountAttributeMetadataUpdateComponent,
    AccountAttributeMetadataDeleteDialogComponent,
  ],
  entryComponents: [AccountAttributeMetadataDeleteDialogComponent],
})
export class AccountAttributeMetadataModule {}
