import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AcquiringIssuingFlagComponent } from './list/acquiring-issuing-flag.component';
import { AcquiringIssuingFlagDetailComponent } from './detail/acquiring-issuing-flag-detail.component';
import { AcquiringIssuingFlagUpdateComponent } from './update/acquiring-issuing-flag-update.component';
import { AcquiringIssuingFlagDeleteDialogComponent } from './delete/acquiring-issuing-flag-delete-dialog.component';
import { AcquiringIssuingFlagRoutingModule } from './route/acquiring-issuing-flag-routing.module';

@NgModule({
  imports: [SharedModule, AcquiringIssuingFlagRoutingModule],
  declarations: [
    AcquiringIssuingFlagComponent,
    AcquiringIssuingFlagDetailComponent,
    AcquiringIssuingFlagUpdateComponent,
    AcquiringIssuingFlagDeleteDialogComponent,
  ],
  entryComponents: [AcquiringIssuingFlagDeleteDialogComponent],
})
export class AcquiringIssuingFlagModule {}
