import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationJobNoticeComponent } from './list/depreciation-job-notice.component';
import { DepreciationJobNoticeDetailComponent } from './detail/depreciation-job-notice-detail.component';
import { DepreciationJobNoticeUpdateComponent } from './update/depreciation-job-notice-update.component';
import { DepreciationJobNoticeDeleteDialogComponent } from './delete/depreciation-job-notice-delete-dialog.component';
import { DepreciationJobNoticeRoutingModule } from './route/depreciation-job-notice-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationJobNoticeRoutingModule],
  declarations: [
    DepreciationJobNoticeComponent,
    DepreciationJobNoticeDetailComponent,
    DepreciationJobNoticeUpdateComponent,
    DepreciationJobNoticeDeleteDialogComponent,
  ],
  entryComponents: [DepreciationJobNoticeDeleteDialogComponent],
})
export class DepreciationJobNoticeModule {}
