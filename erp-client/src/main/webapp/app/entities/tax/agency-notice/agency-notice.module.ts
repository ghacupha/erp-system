import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AgencyNoticeComponent } from './list/agency-notice.component';
import { AgencyNoticeDetailComponent } from './detail/agency-notice-detail.component';
import { AgencyNoticeUpdateComponent } from './update/agency-notice-update.component';
import { AgencyNoticeDeleteDialogComponent } from './delete/agency-notice-delete-dialog.component';
import { AgencyNoticeRoutingModule } from './route/agency-notice-routing.module';

@NgModule({
  imports: [SharedModule, AgencyNoticeRoutingModule],
  declarations: [AgencyNoticeComponent, AgencyNoticeDetailComponent, AgencyNoticeUpdateComponent, AgencyNoticeDeleteDialogComponent],
  entryComponents: [AgencyNoticeDeleteDialogComponent],
})
export class AgencyNoticeModule {}
