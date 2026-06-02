import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentCompilationRequestComponent } from './list/prepayment-compilation-request.component';
import { PrepaymentCompilationRequestDetailComponent } from './detail/prepayment-compilation-request-detail.component';
import { PrepaymentCompilationRequestUpdateComponent } from './update/prepayment-compilation-request-update.component';
import { PrepaymentCompilationRequestDeleteDialogComponent } from './delete/prepayment-compilation-request-delete-dialog.component';
import { PrepaymentCompilationRequestRoutingModule } from './route/prepayment-compilation-request-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentCompilationRequestRoutingModule],
  declarations: [
    PrepaymentCompilationRequestComponent,
    PrepaymentCompilationRequestDetailComponent,
    PrepaymentCompilationRequestUpdateComponent,
    PrepaymentCompilationRequestDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentCompilationRequestDeleteDialogComponent],
})
export class PrepaymentCompilationRequestModule {}
