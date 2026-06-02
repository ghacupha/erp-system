import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExecutiveCategoryTypeComponent } from './list/executive-category-type.component';
import { ExecutiveCategoryTypeDetailComponent } from './detail/executive-category-type-detail.component';
import { ExecutiveCategoryTypeUpdateComponent } from './update/executive-category-type-update.component';
import { ExecutiveCategoryTypeDeleteDialogComponent } from './delete/executive-category-type-delete-dialog.component';
import { ExecutiveCategoryTypeRoutingModule } from './route/executive-category-type-routing.module';

@NgModule({
  imports: [SharedModule, ExecutiveCategoryTypeRoutingModule],
  declarations: [
    ExecutiveCategoryTypeComponent,
    ExecutiveCategoryTypeDetailComponent,
    ExecutiveCategoryTypeUpdateComponent,
    ExecutiveCategoryTypeDeleteDialogComponent,
  ],
  entryComponents: [ExecutiveCategoryTypeDeleteDialogComponent],
})
export class ExecutiveCategoryTypeModule {}
