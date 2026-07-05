import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentMarshallingComponent } from './list/prepayment-marshalling.component';
import { PrepaymentMarshallingDetailComponent } from './detail/prepayment-marshalling-detail.component';
import { PrepaymentMarshallingUpdateComponent } from './update/prepayment-marshalling-update.component';
import { PrepaymentMarshallingDeleteDialogComponent } from './delete/prepayment-marshalling-delete-dialog.component';
import { PrepaymentMarshallingRoutingModule } from './route/prepayment-marshalling-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentMarshallingRoutingModule],
  declarations: [
    PrepaymentMarshallingComponent,
    PrepaymentMarshallingDetailComponent,
    PrepaymentMarshallingUpdateComponent,
    PrepaymentMarshallingDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentMarshallingDeleteDialogComponent],
})
export class PrepaymentMarshallingModule {}
