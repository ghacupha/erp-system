import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbSourceOfInformationTypeComponent } from './list/crb-source-of-information-type.component';
import { CrbSourceOfInformationTypeDetailComponent } from './detail/crb-source-of-information-type-detail.component';
import { CrbSourceOfInformationTypeUpdateComponent } from './update/crb-source-of-information-type-update.component';
import { CrbSourceOfInformationTypeDeleteDialogComponent } from './delete/crb-source-of-information-type-delete-dialog.component';
import { CrbSourceOfInformationTypeRoutingModule } from './route/crb-source-of-information-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbSourceOfInformationTypeRoutingModule],
  declarations: [
    CrbSourceOfInformationTypeComponent,
    CrbSourceOfInformationTypeDetailComponent,
    CrbSourceOfInformationTypeUpdateComponent,
    CrbSourceOfInformationTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbSourceOfInformationTypeDeleteDialogComponent],
})
export class CrbSourceOfInformationTypeModule {}
