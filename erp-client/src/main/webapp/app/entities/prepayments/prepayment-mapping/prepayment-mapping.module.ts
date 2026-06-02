import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentMappingComponent } from './list/prepayment-mapping.component';
import { PrepaymentMappingDetailComponent } from './detail/prepayment-mapping-detail.component';
import { PrepaymentMappingUpdateComponent } from './update/prepayment-mapping-update.component';
import { PrepaymentMappingDeleteDialogComponent } from './delete/prepayment-mapping-delete-dialog.component';
import { PrepaymentMappingRoutingModule } from './route/prepayment-mapping-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentMappingRoutingModule],
  declarations: [
    PrepaymentMappingComponent,
    PrepaymentMappingDetailComponent,
    PrepaymentMappingUpdateComponent,
    PrepaymentMappingDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentMappingDeleteDialogComponent],
})
export class PrepaymentMappingModule {}
