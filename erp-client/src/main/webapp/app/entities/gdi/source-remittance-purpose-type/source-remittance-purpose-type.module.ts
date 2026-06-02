import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SourceRemittancePurposeTypeComponent } from './list/source-remittance-purpose-type.component';
import { SourceRemittancePurposeTypeDetailComponent } from './detail/source-remittance-purpose-type-detail.component';
import { SourceRemittancePurposeTypeUpdateComponent } from './update/source-remittance-purpose-type-update.component';
import { SourceRemittancePurposeTypeDeleteDialogComponent } from './delete/source-remittance-purpose-type-delete-dialog.component';
import { SourceRemittancePurposeTypeRoutingModule } from './route/source-remittance-purpose-type-routing.module';

@NgModule({
  imports: [SharedModule, SourceRemittancePurposeTypeRoutingModule],
  declarations: [
    SourceRemittancePurposeTypeComponent,
    SourceRemittancePurposeTypeDetailComponent,
    SourceRemittancePurposeTypeUpdateComponent,
    SourceRemittancePurposeTypeDeleteDialogComponent,
  ],
  entryComponents: [SourceRemittancePurposeTypeDeleteDialogComponent],
})
export class SourceRemittancePurposeTypeModule {}
