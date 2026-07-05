import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardClassTypeComponent } from './list/card-class-type.component';
import { CardClassTypeDetailComponent } from './detail/card-class-type-detail.component';
import { CardClassTypeUpdateComponent } from './update/card-class-type-update.component';
import { CardClassTypeDeleteDialogComponent } from './delete/card-class-type-delete-dialog.component';
import { CardClassTypeRoutingModule } from './route/card-class-type-routing.module';

@NgModule({
  imports: [SharedModule, CardClassTypeRoutingModule],
  declarations: [CardClassTypeComponent, CardClassTypeDetailComponent, CardClassTypeUpdateComponent, CardClassTypeDeleteDialogComponent],
  entryComponents: [CardClassTypeDeleteDialogComponent],
})
export class CardClassTypeModule {}
