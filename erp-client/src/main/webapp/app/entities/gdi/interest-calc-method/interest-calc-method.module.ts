import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InterestCalcMethodComponent } from './list/interest-calc-method.component';
import { InterestCalcMethodDetailComponent } from './detail/interest-calc-method-detail.component';
import { InterestCalcMethodUpdateComponent } from './update/interest-calc-method-update.component';
import { InterestCalcMethodDeleteDialogComponent } from './delete/interest-calc-method-delete-dialog.component';
import { InterestCalcMethodRoutingModule } from './route/interest-calc-method-routing.module';

@NgModule({
  imports: [SharedModule, InterestCalcMethodRoutingModule],
  declarations: [
    InterestCalcMethodComponent,
    InterestCalcMethodDetailComponent,
    InterestCalcMethodUpdateComponent,
    InterestCalcMethodDeleteDialogComponent,
  ],
  entryComponents: [InterestCalcMethodDeleteDialogComponent],
})
export class InterestCalcMethodModule {}
