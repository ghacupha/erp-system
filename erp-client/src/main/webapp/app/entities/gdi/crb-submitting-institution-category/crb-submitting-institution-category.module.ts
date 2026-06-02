import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbSubmittingInstitutionCategoryComponent } from './list/crb-submitting-institution-category.component';
import { CrbSubmittingInstitutionCategoryDetailComponent } from './detail/crb-submitting-institution-category-detail.component';
import { CrbSubmittingInstitutionCategoryUpdateComponent } from './update/crb-submitting-institution-category-update.component';
import { CrbSubmittingInstitutionCategoryDeleteDialogComponent } from './delete/crb-submitting-institution-category-delete-dialog.component';
import { CrbSubmittingInstitutionCategoryRoutingModule } from './route/crb-submitting-institution-category-routing.module';

@NgModule({
  imports: [SharedModule, CrbSubmittingInstitutionCategoryRoutingModule],
  declarations: [
    CrbSubmittingInstitutionCategoryComponent,
    CrbSubmittingInstitutionCategoryDetailComponent,
    CrbSubmittingInstitutionCategoryUpdateComponent,
    CrbSubmittingInstitutionCategoryDeleteDialogComponent,
  ],
  entryComponents: [CrbSubmittingInstitutionCategoryDeleteDialogComponent],
})
export class CrbSubmittingInstitutionCategoryModule {}
