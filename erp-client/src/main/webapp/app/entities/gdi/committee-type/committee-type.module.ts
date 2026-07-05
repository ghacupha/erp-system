import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommitteeTypeComponent } from './list/committee-type.component';
import { CommitteeTypeDetailComponent } from './detail/committee-type-detail.component';
import { CommitteeTypeUpdateComponent } from './update/committee-type-update.component';
import { CommitteeTypeDeleteDialogComponent } from './delete/committee-type-delete-dialog.component';
import { CommitteeTypeRoutingModule } from './route/committee-type-routing.module';

@NgModule({
  imports: [SharedModule, CommitteeTypeRoutingModule],
  declarations: [CommitteeTypeComponent, CommitteeTypeDetailComponent, CommitteeTypeUpdateComponent, CommitteeTypeDeleteDialogComponent],
  entryComponents: [CommitteeTypeDeleteDialogComponent],
})
export class CommitteeTypeModule {}
