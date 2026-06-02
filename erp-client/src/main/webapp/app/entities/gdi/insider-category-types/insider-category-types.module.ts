import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InsiderCategoryTypesComponent } from './list/insider-category-types.component';
import { InsiderCategoryTypesDetailComponent } from './detail/insider-category-types-detail.component';
import { InsiderCategoryTypesUpdateComponent } from './update/insider-category-types-update.component';
import { InsiderCategoryTypesDeleteDialogComponent } from './delete/insider-category-types-delete-dialog.component';
import { InsiderCategoryTypesRoutingModule } from './route/insider-category-types-routing.module';

@NgModule({
  imports: [SharedModule, InsiderCategoryTypesRoutingModule],
  declarations: [
    InsiderCategoryTypesComponent,
    InsiderCategoryTypesDetailComponent,
    InsiderCategoryTypesUpdateComponent,
    InsiderCategoryTypesDeleteDialogComponent,
  ],
  entryComponents: [InsiderCategoryTypesDeleteDialogComponent],
})
export class InsiderCategoryTypesModule {}
