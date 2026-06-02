import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DerivativeSubTypeComponent } from './list/derivative-sub-type.component';
import { DerivativeSubTypeDetailComponent } from './detail/derivative-sub-type-detail.component';
import { DerivativeSubTypeUpdateComponent } from './update/derivative-sub-type-update.component';
import { DerivativeSubTypeDeleteDialogComponent } from './delete/derivative-sub-type-delete-dialog.component';
import { DerivativeSubTypeRoutingModule } from './route/derivative-sub-type-routing.module';

@NgModule({
  imports: [SharedModule, DerivativeSubTypeRoutingModule],
  declarations: [
    DerivativeSubTypeComponent,
    DerivativeSubTypeDetailComponent,
    DerivativeSubTypeUpdateComponent,
    DerivativeSubTypeDeleteDialogComponent,
  ],
  entryComponents: [DerivativeSubTypeDeleteDialogComponent],
})
export class DerivativeSubTypeModule {}
