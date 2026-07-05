import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportDesignComponent } from './list/report-design.component';
import { ReportDesignDetailComponent } from './detail/report-design-detail.component';
import { ReportDesignUpdateComponent } from './update/report-design-update.component';
import { ReportDesignDeleteDialogComponent } from './delete/report-design-delete-dialog.component';
import { ReportDesignRoutingModule } from './route/report-design-routing.module';

@NgModule({
  imports: [SharedModule, ReportDesignRoutingModule],
  declarations: [ReportDesignComponent, ReportDesignDetailComponent, ReportDesignUpdateComponent, ReportDesignDeleteDialogComponent],
  entryComponents: [ReportDesignDeleteDialogComponent],
})
export class ReportDesignModule {}
