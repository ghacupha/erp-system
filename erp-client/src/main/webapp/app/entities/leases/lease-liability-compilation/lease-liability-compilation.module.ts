import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityCompilationComponent } from './list/lease-liability-compilation.component';
import { LeaseLiabilityCompilationDetailComponent } from './detail/lease-liability-compilation-detail.component';
import { LeaseLiabilityCompilationUpdateComponent } from './update/lease-liability-compilation-update.component';
import { LeaseLiabilityCompilationDeleteDialogComponent } from './delete/lease-liability-compilation-delete-dialog.component';
import { LeaseLiabilityCompilationRoutingModule } from './route/lease-liability-compilation-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityCompilationRoutingModule],
  declarations: [
    LeaseLiabilityCompilationComponent,
    LeaseLiabilityCompilationDetailComponent,
    LeaseLiabilityCompilationUpdateComponent,
    LeaseLiabilityCompilationDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityCompilationDeleteDialogComponent],
})
export class LeaseLiabilityCompilationModule {}
