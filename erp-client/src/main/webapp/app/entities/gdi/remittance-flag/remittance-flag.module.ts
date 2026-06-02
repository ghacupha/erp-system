import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RemittanceFlagComponent } from './list/remittance-flag.component';
import { RemittanceFlagDetailComponent } from './detail/remittance-flag-detail.component';
import { RemittanceFlagUpdateComponent } from './update/remittance-flag-update.component';
import { RemittanceFlagDeleteDialogComponent } from './delete/remittance-flag-delete-dialog.component';
import { RemittanceFlagRoutingModule } from './route/remittance-flag-routing.module';

@NgModule({
  imports: [SharedModule, RemittanceFlagRoutingModule],
  declarations: [
    RemittanceFlagComponent,
    RemittanceFlagDetailComponent,
    RemittanceFlagUpdateComponent,
    RemittanceFlagDeleteDialogComponent,
  ],
  entryComponents: [RemittanceFlagDeleteDialogComponent],
})
export class RemittanceFlagModule {}
