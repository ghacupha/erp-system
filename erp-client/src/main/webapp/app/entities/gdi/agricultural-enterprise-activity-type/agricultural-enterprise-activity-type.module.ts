import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AgriculturalEnterpriseActivityTypeComponent } from './list/agricultural-enterprise-activity-type.component';
import { AgriculturalEnterpriseActivityTypeDetailComponent } from './detail/agricultural-enterprise-activity-type-detail.component';
import { AgriculturalEnterpriseActivityTypeUpdateComponent } from './update/agricultural-enterprise-activity-type-update.component';
import { AgriculturalEnterpriseActivityTypeDeleteDialogComponent } from './delete/agricultural-enterprise-activity-type-delete-dialog.component';
import { AgriculturalEnterpriseActivityTypeRoutingModule } from './route/agricultural-enterprise-activity-type-routing.module';

@NgModule({
  imports: [SharedModule, AgriculturalEnterpriseActivityTypeRoutingModule],
  declarations: [
    AgriculturalEnterpriseActivityTypeComponent,
    AgriculturalEnterpriseActivityTypeDetailComponent,
    AgriculturalEnterpriseActivityTypeUpdateComponent,
    AgriculturalEnterpriseActivityTypeDeleteDialogComponent,
  ],
  entryComponents: [AgriculturalEnterpriseActivityTypeDeleteDialogComponent],
})
export class AgriculturalEnterpriseActivityTypeModule {}
