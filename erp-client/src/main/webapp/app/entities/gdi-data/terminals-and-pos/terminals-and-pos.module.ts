import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TerminalsAndPOSComponent } from './list/terminals-and-pos.component';
import { TerminalsAndPOSDetailComponent } from './detail/terminals-and-pos-detail.component';
import { TerminalsAndPOSUpdateComponent } from './update/terminals-and-pos-update.component';
import { TerminalsAndPOSDeleteDialogComponent } from './delete/terminals-and-pos-delete-dialog.component';
import { TerminalsAndPOSRoutingModule } from './route/terminals-and-pos-routing.module';

@NgModule({
  imports: [SharedModule, TerminalsAndPOSRoutingModule],
  declarations: [
    TerminalsAndPOSComponent,
    TerminalsAndPOSDetailComponent,
    TerminalsAndPOSUpdateComponent,
    TerminalsAndPOSDeleteDialogComponent,
  ],
  entryComponents: [TerminalsAndPOSDeleteDialogComponent],
})
export class TerminalsAndPOSModule {}
