import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardFraudIncidentCategoryComponent } from './list/card-fraud-incident-category.component';
import { CardFraudIncidentCategoryDetailComponent } from './detail/card-fraud-incident-category-detail.component';
import { CardFraudIncidentCategoryUpdateComponent } from './update/card-fraud-incident-category-update.component';
import { CardFraudIncidentCategoryDeleteDialogComponent } from './delete/card-fraud-incident-category-delete-dialog.component';
import { CardFraudIncidentCategoryRoutingModule } from './route/card-fraud-incident-category-routing.module';

@NgModule({
  imports: [SharedModule, CardFraudIncidentCategoryRoutingModule],
  declarations: [
    CardFraudIncidentCategoryComponent,
    CardFraudIncidentCategoryDetailComponent,
    CardFraudIncidentCategoryUpdateComponent,
    CardFraudIncidentCategoryDeleteDialogComponent,
  ],
  entryComponents: [CardFraudIncidentCategoryDeleteDialogComponent],
})
export class CardFraudIncidentCategoryModule {}
