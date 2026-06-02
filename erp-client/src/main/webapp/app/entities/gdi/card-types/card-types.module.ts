import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardTypesComponent } from './list/card-types.component';
import { CardTypesDetailComponent } from './detail/card-types-detail.component';
import { CardTypesUpdateComponent } from './update/card-types-update.component';
import { CardTypesDeleteDialogComponent } from './delete/card-types-delete-dialog.component';
import { CardTypesRoutingModule } from './route/card-types-routing.module';

@NgModule({
  imports: [SharedModule, CardTypesRoutingModule],
  declarations: [CardTypesComponent, CardTypesDetailComponent, CardTypesUpdateComponent, CardTypesDeleteDialogComponent],
  entryComponents: [CardTypesDeleteDialogComponent],
})
export class CardTypesModule {}
