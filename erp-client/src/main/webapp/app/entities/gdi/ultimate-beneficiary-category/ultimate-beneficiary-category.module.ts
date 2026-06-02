import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UltimateBeneficiaryCategoryComponent } from './list/ultimate-beneficiary-category.component';
import { UltimateBeneficiaryCategoryDetailComponent } from './detail/ultimate-beneficiary-category-detail.component';
import { UltimateBeneficiaryCategoryUpdateComponent } from './update/ultimate-beneficiary-category-update.component';
import { UltimateBeneficiaryCategoryDeleteDialogComponent } from './delete/ultimate-beneficiary-category-delete-dialog.component';
import { UltimateBeneficiaryCategoryRoutingModule } from './route/ultimate-beneficiary-category-routing.module';

@NgModule({
  imports: [SharedModule, UltimateBeneficiaryCategoryRoutingModule],
  declarations: [
    UltimateBeneficiaryCategoryComponent,
    UltimateBeneficiaryCategoryDetailComponent,
    UltimateBeneficiaryCategoryUpdateComponent,
    UltimateBeneficiaryCategoryDeleteDialogComponent,
  ],
  entryComponents: [UltimateBeneficiaryCategoryDeleteDialogComponent],
})
export class UltimateBeneficiaryCategoryModule {}
