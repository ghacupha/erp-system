import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MerchantTypeComponent } from './list/merchant-type.component';
import { MerchantTypeDetailComponent } from './detail/merchant-type-detail.component';
import { MerchantTypeUpdateComponent } from './update/merchant-type-update.component';
import { MerchantTypeDeleteDialogComponent } from './delete/merchant-type-delete-dialog.component';
import { MerchantTypeRoutingModule } from './route/merchant-type-routing.module';

@NgModule({
  imports: [SharedModule, MerchantTypeRoutingModule],
  declarations: [MerchantTypeComponent, MerchantTypeDetailComponent, MerchantTypeUpdateComponent, MerchantTypeDeleteDialogComponent],
  entryComponents: [MerchantTypeDeleteDialogComponent],
})
export class MerchantTypeModule {}
