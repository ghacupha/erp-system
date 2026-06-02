import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouModelMetadataComponent } from './list/rou-model-metadata.component';
import { RouModelMetadataDetailComponent } from './detail/rou-model-metadata-detail.component';
import { RouModelMetadataUpdateComponent } from './update/rou-model-metadata-update.component';
import { RouModelMetadataDeleteDialogComponent } from './delete/rou-model-metadata-delete-dialog.component';
import { RouModelMetadataRoutingModule } from './route/rou-model-metadata-routing.module';

@NgModule({
  imports: [SharedModule, RouModelMetadataRoutingModule],
  declarations: [
    RouModelMetadataComponent,
    RouModelMetadataDetailComponent,
    RouModelMetadataUpdateComponent,
    RouModelMetadataDeleteDialogComponent,
  ],
  entryComponents: [RouModelMetadataDeleteDialogComponent],
})
export class RouModelMetadataModule {}
