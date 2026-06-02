import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FraudTypeComponent } from './list/fraud-type.component';
import { FraudTypeDetailComponent } from './detail/fraud-type-detail.component';
import { FraudTypeUpdateComponent } from './update/fraud-type-update.component';
import { FraudTypeDeleteDialogComponent } from './delete/fraud-type-delete-dialog.component';
import { FraudTypeRoutingModule } from './route/fraud-type-routing.module';

@NgModule({
  imports: [SharedModule, FraudTypeRoutingModule],
  declarations: [FraudTypeComponent, FraudTypeDetailComponent, FraudTypeUpdateComponent, FraudTypeDeleteDialogComponent],
  entryComponents: [FraudTypeDeleteDialogComponent],
})
export class FraudTypeModule {}
