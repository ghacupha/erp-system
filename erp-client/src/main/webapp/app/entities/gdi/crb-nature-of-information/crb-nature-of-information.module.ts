import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbNatureOfInformationComponent } from './list/crb-nature-of-information.component';
import { CrbNatureOfInformationDetailComponent } from './detail/crb-nature-of-information-detail.component';
import { CrbNatureOfInformationUpdateComponent } from './update/crb-nature-of-information-update.component';
import { CrbNatureOfInformationDeleteDialogComponent } from './delete/crb-nature-of-information-delete-dialog.component';
import { CrbNatureOfInformationRoutingModule } from './route/crb-nature-of-information-routing.module';

@NgModule({
  imports: [SharedModule, CrbNatureOfInformationRoutingModule],
  declarations: [
    CrbNatureOfInformationComponent,
    CrbNatureOfInformationDetailComponent,
    CrbNatureOfInformationUpdateComponent,
    CrbNatureOfInformationDeleteDialogComponent,
  ],
  entryComponents: [CrbNatureOfInformationDeleteDialogComponent],
})
export class CrbNatureOfInformationModule {}
