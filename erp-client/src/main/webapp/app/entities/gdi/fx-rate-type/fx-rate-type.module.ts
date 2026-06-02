import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FxRateTypeComponent } from './list/fx-rate-type.component';
import { FxRateTypeDetailComponent } from './detail/fx-rate-type-detail.component';
import { FxRateTypeUpdateComponent } from './update/fx-rate-type-update.component';
import { FxRateTypeDeleteDialogComponent } from './delete/fx-rate-type-delete-dialog.component';
import { FxRateTypeRoutingModule } from './route/fx-rate-type-routing.module';

@NgModule({
  imports: [SharedModule, FxRateTypeRoutingModule],
  declarations: [FxRateTypeComponent, FxRateTypeDetailComponent, FxRateTypeUpdateComponent, FxRateTypeDeleteDialogComponent],
  entryComponents: [FxRateTypeDeleteDialogComponent],
})
export class FxRateTypeModule {}
