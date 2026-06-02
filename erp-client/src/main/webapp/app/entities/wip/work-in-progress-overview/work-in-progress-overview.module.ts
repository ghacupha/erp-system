import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkInProgressOverviewComponent } from './list/work-in-progress-overview.component';
import { WorkInProgressOverviewDetailComponent } from './detail/work-in-progress-overview-detail.component';
import { WorkInProgressOverviewRoutingModule } from './route/work-in-progress-overview-routing.module';

@NgModule({
  imports: [SharedModule, WorkInProgressOverviewRoutingModule],
  declarations: [WorkInProgressOverviewComponent, WorkInProgressOverviewDetailComponent],
})
export class WorkInProgressOverviewModule {}
