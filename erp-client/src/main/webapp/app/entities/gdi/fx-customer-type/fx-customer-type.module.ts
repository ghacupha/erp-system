import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FxCustomerTypeComponent } from './list/fx-customer-type.component';
import { FxCustomerTypeDetailComponent } from './detail/fx-customer-type-detail.component';
import { FxCustomerTypeUpdateComponent } from './update/fx-customer-type-update.component';
import { FxCustomerTypeDeleteDialogComponent } from './delete/fx-customer-type-delete-dialog.component';
import { FxCustomerTypeRoutingModule } from './route/fx-customer-type-routing.module';

@NgModule({
  imports: [SharedModule, FxCustomerTypeRoutingModule],
  declarations: [
    FxCustomerTypeComponent,
    FxCustomerTypeDetailComponent,
    FxCustomerTypeUpdateComponent,
    FxCustomerTypeDeleteDialogComponent,
  ],
  entryComponents: [FxCustomerTypeDeleteDialogComponent],
})
export class FxCustomerTypeModule {}
