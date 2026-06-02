import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContractMetadataComponent } from './list/contract-metadata.component';
import { ContractMetadataDetailComponent } from './detail/contract-metadata-detail.component';
import { ContractMetadataUpdateComponent } from './update/contract-metadata-update.component';
import { ContractMetadataDeleteDialogComponent } from './delete/contract-metadata-delete-dialog.component';
import { ContractMetadataRoutingModule } from './route/contract-metadata-routing.module';

@NgModule({
  imports: [SharedModule, ContractMetadataRoutingModule],
  declarations: [
    ContractMetadataComponent,
    ContractMetadataDetailComponent,
    ContractMetadataUpdateComponent,
    ContractMetadataDeleteDialogComponent,
  ],
  entryComponents: [ContractMetadataDeleteDialogComponent],
})
export class ContractMetadataModule {}
