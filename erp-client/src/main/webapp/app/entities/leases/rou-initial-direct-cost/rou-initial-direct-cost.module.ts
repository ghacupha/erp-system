import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouInitialDirectCostComponent } from './list/rou-initial-direct-cost.component';
import { RouInitialDirectCostDetailComponent } from './detail/rou-initial-direct-cost-detail.component';
import { RouInitialDirectCostUpdateComponent } from './update/rou-initial-direct-cost-update.component';
import { RouInitialDirectCostDeleteDialogComponent } from './delete/rou-initial-direct-cost-delete-dialog.component';
import { RouInitialDirectCostRoutingModule } from './route/rou-initial-direct-cost-routing.module';

@NgModule({
  imports: [SharedModule, RouInitialDirectCostRoutingModule],
  declarations: [
    RouInitialDirectCostComponent,
    RouInitialDirectCostDetailComponent,
    RouInitialDirectCostUpdateComponent,
    RouInitialDirectCostDeleteDialogComponent,
  ],
  entryComponents: [RouInitialDirectCostDeleteDialogComponent],
})
export class RouInitialDirectCostModule {}
