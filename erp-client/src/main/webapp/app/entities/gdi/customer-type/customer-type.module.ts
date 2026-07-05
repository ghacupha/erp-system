import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerTypeComponent } from './list/customer-type.component';
import { CustomerTypeDetailComponent } from './detail/customer-type-detail.component';
import { CustomerTypeUpdateComponent } from './update/customer-type-update.component';
import { CustomerTypeDeleteDialogComponent } from './delete/customer-type-delete-dialog.component';
import { CustomerTypeRoutingModule } from './route/customer-type-routing.module';

@NgModule({
  imports: [SharedModule, CustomerTypeRoutingModule],
  declarations: [CustomerTypeComponent, CustomerTypeDetailComponent, CustomerTypeUpdateComponent, CustomerTypeDeleteDialogComponent],
  entryComponents: [CustomerTypeDeleteDialogComponent],
})
export class CustomerTypeModule {}
