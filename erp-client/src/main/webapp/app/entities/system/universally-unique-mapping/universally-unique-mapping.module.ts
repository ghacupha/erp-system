import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UniversallyUniqueMappingComponent } from './list/universally-unique-mapping.component';
import { UniversallyUniqueMappingDetailComponent } from './detail/universally-unique-mapping-detail.component';
import { UniversallyUniqueMappingUpdateComponent } from './update/universally-unique-mapping-update.component';
import { UniversallyUniqueMappingDeleteDialogComponent } from './delete/universally-unique-mapping-delete-dialog.component';
import { UniversallyUniqueMappingRoutingModule } from './route/universally-unique-mapping-routing.module';

@NgModule({
  imports: [SharedModule, UniversallyUniqueMappingRoutingModule],
  declarations: [
    UniversallyUniqueMappingComponent,
    UniversallyUniqueMappingDetailComponent,
    UniversallyUniqueMappingUpdateComponent,
    UniversallyUniqueMappingDeleteDialogComponent,
  ],
  entryComponents: [UniversallyUniqueMappingDeleteDialogComponent],
})
export class UniversallyUniqueMappingModule {}
