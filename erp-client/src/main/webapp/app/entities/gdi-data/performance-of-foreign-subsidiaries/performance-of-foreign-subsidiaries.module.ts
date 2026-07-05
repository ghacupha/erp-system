import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PerformanceOfForeignSubsidiariesComponent } from './list/performance-of-foreign-subsidiaries.component';
import { PerformanceOfForeignSubsidiariesDetailComponent } from './detail/performance-of-foreign-subsidiaries-detail.component';
import { PerformanceOfForeignSubsidiariesUpdateComponent } from './update/performance-of-foreign-subsidiaries-update.component';
import { PerformanceOfForeignSubsidiariesDeleteDialogComponent } from './delete/performance-of-foreign-subsidiaries-delete-dialog.component';
import { PerformanceOfForeignSubsidiariesRoutingModule } from './route/performance-of-foreign-subsidiaries-routing.module';

@NgModule({
  imports: [SharedModule, PerformanceOfForeignSubsidiariesRoutingModule],
  declarations: [
    PerformanceOfForeignSubsidiariesComponent,
    PerformanceOfForeignSubsidiariesDetailComponent,
    PerformanceOfForeignSubsidiariesUpdateComponent,
    PerformanceOfForeignSubsidiariesDeleteDialogComponent,
  ],
  entryComponents: [PerformanceOfForeignSubsidiariesDeleteDialogComponent],
})
export class PerformanceOfForeignSubsidiariesModule {}
