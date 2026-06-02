import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbReportViewBandComponent } from './list/crb-report-view-band.component';
import { CrbReportViewBandDetailComponent } from './detail/crb-report-view-band-detail.component';
import { CrbReportViewBandUpdateComponent } from './update/crb-report-view-band-update.component';
import { CrbReportViewBandDeleteDialogComponent } from './delete/crb-report-view-band-delete-dialog.component';
import { CrbReportViewBandRoutingModule } from './route/crb-report-view-band-routing.module';

@NgModule({
  imports: [SharedModule, CrbReportViewBandRoutingModule],
  declarations: [
    CrbReportViewBandComponent,
    CrbReportViewBandDetailComponent,
    CrbReportViewBandUpdateComponent,
    CrbReportViewBandDeleteDialogComponent,
  ],
  entryComponents: [CrbReportViewBandDeleteDialogComponent],
})
export class CrbReportViewBandModule {}
