import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CounterPartyDealTypeComponent } from './list/counter-party-deal-type.component';
import { CounterPartyDealTypeDetailComponent } from './detail/counter-party-deal-type-detail.component';
import { CounterPartyDealTypeUpdateComponent } from './update/counter-party-deal-type-update.component';
import { CounterPartyDealTypeDeleteDialogComponent } from './delete/counter-party-deal-type-delete-dialog.component';
import { CounterPartyDealTypeRoutingModule } from './route/counter-party-deal-type-routing.module';

@NgModule({
  imports: [SharedModule, CounterPartyDealTypeRoutingModule],
  declarations: [
    CounterPartyDealTypeComponent,
    CounterPartyDealTypeDetailComponent,
    CounterPartyDealTypeUpdateComponent,
    CounterPartyDealTypeDeleteDialogComponent,
  ],
  entryComponents: [CounterPartyDealTypeDeleteDialogComponent],
})
export class CounterPartyDealTypeModule {}
