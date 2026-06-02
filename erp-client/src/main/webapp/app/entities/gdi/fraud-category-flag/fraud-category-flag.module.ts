import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FraudCategoryFlagComponent } from './list/fraud-category-flag.component';
import { FraudCategoryFlagDetailComponent } from './detail/fraud-category-flag-detail.component';
import { FraudCategoryFlagUpdateComponent } from './update/fraud-category-flag-update.component';
import { FraudCategoryFlagDeleteDialogComponent } from './delete/fraud-category-flag-delete-dialog.component';
import { FraudCategoryFlagRoutingModule } from './route/fraud-category-flag-routing.module';

@NgModule({
  imports: [SharedModule, FraudCategoryFlagRoutingModule],
  declarations: [
    FraudCategoryFlagComponent,
    FraudCategoryFlagDetailComponent,
    FraudCategoryFlagUpdateComponent,
    FraudCategoryFlagDeleteDialogComponent,
  ],
  entryComponents: [FraudCategoryFlagDeleteDialogComponent],
})
export class FraudCategoryFlagModule {}
