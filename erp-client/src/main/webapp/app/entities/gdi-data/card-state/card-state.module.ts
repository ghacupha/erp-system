import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardStateComponent } from './list/card-state.component';
import { CardStateDetailComponent } from './detail/card-state-detail.component';
import { CardStateUpdateComponent } from './update/card-state-update.component';
import { CardStateDeleteDialogComponent } from './delete/card-state-delete-dialog.component';
import { CardStateRoutingModule } from './route/card-state-routing.module';

@NgModule({
  imports: [SharedModule, CardStateRoutingModule],
  declarations: [CardStateComponent, CardStateDetailComponent, CardStateUpdateComponent, CardStateDeleteDialogComponent],
  entryComponents: [CardStateDeleteDialogComponent],
})
export class CardStateModule {}
