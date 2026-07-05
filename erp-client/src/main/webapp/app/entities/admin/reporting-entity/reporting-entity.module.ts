import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportingEntityComponent } from './list/reporting-entity.component';
import { ReportingEntityDetailComponent } from './detail/reporting-entity-detail.component';
import { ReportingEntityUpdateComponent } from './update/reporting-entity-update.component';
import { ReportingEntityDeleteDialogComponent } from './delete/reporting-entity-delete-dialog.component';
import { ReportingEntityRoutingModule } from './route/reporting-entity-routing.module';

@NgModule({
  imports: [SharedModule, ReportingEntityRoutingModule],
  declarations: [
    ReportingEntityComponent,
    ReportingEntityDetailComponent,
    ReportingEntityUpdateComponent,
    ReportingEntityDeleteDialogComponent,
  ],
  entryComponents: [ReportingEntityDeleteDialogComponent],
})
export class ReportingEntityModule {}
