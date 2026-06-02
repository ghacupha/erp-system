import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ServiceOutletComponent } from './list/service-outlet.component';
import { ServiceOutletDetailComponent } from './detail/service-outlet-detail.component';
import { ServiceOutletUpdateComponent } from './update/service-outlet-update.component';
import { ServiceOutletDeleteDialogComponent } from './delete/service-outlet-delete-dialog.component';
import { ServiceOutletRoutingModule } from './route/service-outlet-routing.module';

@NgModule({
  imports: [SharedModule, ServiceOutletRoutingModule],
  declarations: [ServiceOutletComponent, ServiceOutletDetailComponent, ServiceOutletUpdateComponent, ServiceOutletDeleteDialogComponent],
  entryComponents: [ServiceOutletDeleteDialogComponent],
})
export class ServiceOutletModule {}
