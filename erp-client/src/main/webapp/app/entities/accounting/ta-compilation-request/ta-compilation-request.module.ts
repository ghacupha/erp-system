import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TACompilationRequestComponent } from './list/ta-compilation-request.component';
import { TACompilationRequestDetailComponent } from './detail/ta-compilation-request-detail.component';
import { TACompilationRequestUpdateComponent } from './update/ta-compilation-request-update.component';
import { TACompilationRequestDeleteDialogComponent } from './delete/ta-compilation-request-delete-dialog.component';
import { TACompilationRequestRoutingModule } from './route/ta-compilation-request-routing.module';

@NgModule({
  imports: [SharedModule, TACompilationRequestRoutingModule],
  declarations: [
    TACompilationRequestComponent,
    TACompilationRequestDetailComponent,
    TACompilationRequestUpdateComponent,
    TACompilationRequestDeleteDialogComponent,
  ],
  entryComponents: [TACompilationRequestDeleteDialogComponent],
})
export class TACompilationRequestModule {}
