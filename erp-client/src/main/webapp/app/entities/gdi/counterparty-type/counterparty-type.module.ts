import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CounterpartyTypeComponent } from './list/counterparty-type.component';
import { CounterpartyTypeDetailComponent } from './detail/counterparty-type-detail.component';
import { CounterpartyTypeUpdateComponent } from './update/counterparty-type-update.component';
import { CounterpartyTypeDeleteDialogComponent } from './delete/counterparty-type-delete-dialog.component';
import { CounterpartyTypeRoutingModule } from './route/counterparty-type-routing.module';

@NgModule({
  imports: [SharedModule, CounterpartyTypeRoutingModule],
  declarations: [
    CounterpartyTypeComponent,
    CounterpartyTypeDetailComponent,
    CounterpartyTypeUpdateComponent,
    CounterpartyTypeDeleteDialogComponent,
  ],
  entryComponents: [CounterpartyTypeDeleteDialogComponent],
})
export class CounterpartyTypeModule {}
