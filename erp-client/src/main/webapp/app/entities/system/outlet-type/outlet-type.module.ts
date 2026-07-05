import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OutletTypeComponent } from './list/outlet-type.component';
import { OutletTypeDetailComponent } from './detail/outlet-type-detail.component';
import { OutletTypeUpdateComponent } from './update/outlet-type-update.component';
import { OutletTypeDeleteDialogComponent } from './delete/outlet-type-delete-dialog.component';
import { OutletTypeRoutingModule } from './route/outlet-type-routing.module';

@NgModule({
  imports: [SharedModule, OutletTypeRoutingModule],
  declarations: [OutletTypeComponent, OutletTypeDetailComponent, OutletTypeUpdateComponent, OutletTypeDeleteDialogComponent],
  entryComponents: [OutletTypeDeleteDialogComponent],
})
export class OutletTypeModule {}
