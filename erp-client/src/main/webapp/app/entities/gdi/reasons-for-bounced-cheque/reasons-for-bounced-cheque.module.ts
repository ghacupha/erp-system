import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReasonsForBouncedChequeComponent } from './list/reasons-for-bounced-cheque.component';
import { ReasonsForBouncedChequeDetailComponent } from './detail/reasons-for-bounced-cheque-detail.component';
import { ReasonsForBouncedChequeUpdateComponent } from './update/reasons-for-bounced-cheque-update.component';
import { ReasonsForBouncedChequeDeleteDialogComponent } from './delete/reasons-for-bounced-cheque-delete-dialog.component';
import { ReasonsForBouncedChequeRoutingModule } from './route/reasons-for-bounced-cheque-routing.module';

@NgModule({
  imports: [SharedModule, ReasonsForBouncedChequeRoutingModule],
  declarations: [
    ReasonsForBouncedChequeComponent,
    ReasonsForBouncedChequeDetailComponent,
    ReasonsForBouncedChequeUpdateComponent,
    ReasonsForBouncedChequeDeleteDialogComponent,
  ],
  entryComponents: [ReasonsForBouncedChequeDeleteDialogComponent],
})
export class ReasonsForBouncedChequeModule {}
