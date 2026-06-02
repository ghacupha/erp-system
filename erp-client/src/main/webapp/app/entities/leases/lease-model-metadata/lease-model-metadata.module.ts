import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseModelMetadataComponent } from './list/lease-model-metadata.component';
import { LeaseModelMetadataDetailComponent } from './detail/lease-model-metadata-detail.component';
import { LeaseModelMetadataUpdateComponent } from './update/lease-model-metadata-update.component';
import { LeaseModelMetadataDeleteDialogComponent } from './delete/lease-model-metadata-delete-dialog.component';
import { LeaseModelMetadataRoutingModule } from './route/lease-model-metadata-routing.module';

@NgModule({
  imports: [SharedModule, LeaseModelMetadataRoutingModule],
  declarations: [
    LeaseModelMetadataComponent,
    LeaseModelMetadataDetailComponent,
    LeaseModelMetadataUpdateComponent,
    LeaseModelMetadataDeleteDialogComponent,
  ],
  entryComponents: [LeaseModelMetadataDeleteDialogComponent],
})
export class LeaseModelMetadataModule {}
