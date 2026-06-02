import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanPerformanceClassificationComponent } from './list/loan-performance-classification.component';
import { LoanPerformanceClassificationDetailComponent } from './detail/loan-performance-classification-detail.component';
import { LoanPerformanceClassificationUpdateComponent } from './update/loan-performance-classification-update.component';
import { LoanPerformanceClassificationDeleteDialogComponent } from './delete/loan-performance-classification-delete-dialog.component';
import { LoanPerformanceClassificationRoutingModule } from './route/loan-performance-classification-routing.module';

@NgModule({
  imports: [SharedModule, LoanPerformanceClassificationRoutingModule],
  declarations: [
    LoanPerformanceClassificationComponent,
    LoanPerformanceClassificationDetailComponent,
    LoanPerformanceClassificationUpdateComponent,
    LoanPerformanceClassificationDeleteDialogComponent,
  ],
  entryComponents: [LoanPerformanceClassificationDeleteDialogComponent],
})
export class LoanPerformanceClassificationModule {}
