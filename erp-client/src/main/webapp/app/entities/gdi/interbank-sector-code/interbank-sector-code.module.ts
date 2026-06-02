import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InterbankSectorCodeComponent } from './list/interbank-sector-code.component';
import { InterbankSectorCodeDetailComponent } from './detail/interbank-sector-code-detail.component';
import { InterbankSectorCodeUpdateComponent } from './update/interbank-sector-code-update.component';
import { InterbankSectorCodeDeleteDialogComponent } from './delete/interbank-sector-code-delete-dialog.component';
import { InterbankSectorCodeRoutingModule } from './route/interbank-sector-code-routing.module';

@NgModule({
  imports: [SharedModule, InterbankSectorCodeRoutingModule],
  declarations: [
    InterbankSectorCodeComponent,
    InterbankSectorCodeDetailComponent,
    InterbankSectorCodeUpdateComponent,
    InterbankSectorCodeDeleteDialogComponent,
  ],
  entryComponents: [InterbankSectorCodeDeleteDialogComponent],
})
export class InterbankSectorCodeModule {}
