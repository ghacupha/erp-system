import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardCategoryTypeComponent } from './list/card-category-type.component';
import { CardCategoryTypeDetailComponent } from './detail/card-category-type-detail.component';
import { CardCategoryTypeUpdateComponent } from './update/card-category-type-update.component';
import { CardCategoryTypeDeleteDialogComponent } from './delete/card-category-type-delete-dialog.component';
import { CardCategoryTypeRoutingModule } from './route/card-category-type-routing.module';

@NgModule({
  imports: [SharedModule, CardCategoryTypeRoutingModule],
  declarations: [
    CardCategoryTypeComponent,
    CardCategoryTypeDetailComponent,
    CardCategoryTypeUpdateComponent,
    CardCategoryTypeDeleteDialogComponent,
  ],
  entryComponents: [CardCategoryTypeDeleteDialogComponent],
})
export class CardCategoryTypeModule {}
