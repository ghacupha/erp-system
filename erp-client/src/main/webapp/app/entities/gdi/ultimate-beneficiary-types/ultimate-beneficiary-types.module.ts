import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UltimateBeneficiaryTypesComponent } from './list/ultimate-beneficiary-types.component';
import { UltimateBeneficiaryTypesDetailComponent } from './detail/ultimate-beneficiary-types-detail.component';
import { UltimateBeneficiaryTypesUpdateComponent } from './update/ultimate-beneficiary-types-update.component';
import { UltimateBeneficiaryTypesDeleteDialogComponent } from './delete/ultimate-beneficiary-types-delete-dialog.component';
import { UltimateBeneficiaryTypesRoutingModule } from './route/ultimate-beneficiary-types-routing.module';

@NgModule({
  imports: [SharedModule, UltimateBeneficiaryTypesRoutingModule],
  declarations: [
    UltimateBeneficiaryTypesComponent,
    UltimateBeneficiaryTypesDetailComponent,
    UltimateBeneficiaryTypesUpdateComponent,
    UltimateBeneficiaryTypesDeleteDialogComponent,
  ],
  entryComponents: [UltimateBeneficiaryTypesDeleteDialogComponent],
})
export class UltimateBeneficiaryTypesModule {}
