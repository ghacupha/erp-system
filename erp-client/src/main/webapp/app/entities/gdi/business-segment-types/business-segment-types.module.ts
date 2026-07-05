import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BusinessSegmentTypesComponent } from './list/business-segment-types.component';
import { BusinessSegmentTypesDetailComponent } from './detail/business-segment-types-detail.component';
import { BusinessSegmentTypesUpdateComponent } from './update/business-segment-types-update.component';
import { BusinessSegmentTypesDeleteDialogComponent } from './delete/business-segment-types-delete-dialog.component';
import { BusinessSegmentTypesRoutingModule } from './route/business-segment-types-routing.module';

@NgModule({
  imports: [SharedModule, BusinessSegmentTypesRoutingModule],
  declarations: [
    BusinessSegmentTypesComponent,
    BusinessSegmentTypesDetailComponent,
    BusinessSegmentTypesUpdateComponent,
    BusinessSegmentTypesDeleteDialogComponent,
  ],
  entryComponents: [BusinessSegmentTypesDeleteDialogComponent],
})
export class BusinessSegmentTypesModule {}
