import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GlMappingComponent } from './list/gl-mapping.component';
import { GlMappingDetailComponent } from './detail/gl-mapping-detail.component';
import { GlMappingUpdateComponent } from './update/gl-mapping-update.component';
import { GlMappingDeleteDialogComponent } from './delete/gl-mapping-delete-dialog.component';
import { GlMappingRoutingModule } from './route/gl-mapping-routing.module';

@NgModule({
  imports: [SharedModule, GlMappingRoutingModule],
  declarations: [GlMappingComponent, GlMappingDetailComponent, GlMappingUpdateComponent, GlMappingDeleteDialogComponent],
  entryComponents: [GlMappingDeleteDialogComponent],
})
export class GlMappingModule {}
