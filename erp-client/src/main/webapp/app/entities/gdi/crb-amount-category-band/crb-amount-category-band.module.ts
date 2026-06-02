import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbAmountCategoryBandComponent } from './list/crb-amount-category-band.component';
import { CrbAmountCategoryBandDetailComponent } from './detail/crb-amount-category-band-detail.component';
import { CrbAmountCategoryBandUpdateComponent } from './update/crb-amount-category-band-update.component';
import { CrbAmountCategoryBandDeleteDialogComponent } from './delete/crb-amount-category-band-delete-dialog.component';
import { CrbAmountCategoryBandRoutingModule } from './route/crb-amount-category-band-routing.module';

@NgModule({
  imports: [SharedModule, CrbAmountCategoryBandRoutingModule],
  declarations: [
    CrbAmountCategoryBandComponent,
    CrbAmountCategoryBandDetailComponent,
    CrbAmountCategoryBandUpdateComponent,
    CrbAmountCategoryBandDeleteDialogComponent,
  ],
  entryComponents: [CrbAmountCategoryBandDeleteDialogComponent],
})
export class CrbAmountCategoryBandModule {}
