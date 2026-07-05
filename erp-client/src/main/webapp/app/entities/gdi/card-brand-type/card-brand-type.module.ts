import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardBrandTypeComponent } from './list/card-brand-type.component';
import { CardBrandTypeDetailComponent } from './detail/card-brand-type-detail.component';
import { CardBrandTypeUpdateComponent } from './update/card-brand-type-update.component';
import { CardBrandTypeDeleteDialogComponent } from './delete/card-brand-type-delete-dialog.component';
import { CardBrandTypeRoutingModule } from './route/card-brand-type-routing.module';

@NgModule({
  imports: [SharedModule, CardBrandTypeRoutingModule],
  declarations: [CardBrandTypeComponent, CardBrandTypeDetailComponent, CardBrandTypeUpdateComponent, CardBrandTypeDeleteDialogComponent],
  entryComponents: [CardBrandTypeDeleteDialogComponent],
})
export class CardBrandTypeModule {}
