import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationMethodComponent } from './list/depreciation-method.component';
import { DepreciationMethodDetailComponent } from './detail/depreciation-method-detail.component';
import { DepreciationMethodUpdateComponent } from './update/depreciation-method-update.component';
import { DepreciationMethodDeleteDialogComponent } from './delete/depreciation-method-delete-dialog.component';
import { DepreciationMethodRoutingModule } from './route/depreciation-method-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationMethodRoutingModule],
  declarations: [
    DepreciationMethodComponent,
    DepreciationMethodDetailComponent,
    DepreciationMethodUpdateComponent,
    DepreciationMethodDeleteDialogComponent,
  ],
  entryComponents: [DepreciationMethodDeleteDialogComponent],
})
export class DepreciationMethodModule {}
