import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsicEconomicActivityComponent } from './list/isic-economic-activity.component';
import { IsicEconomicActivityDetailComponent } from './detail/isic-economic-activity-detail.component';
import { IsicEconomicActivityUpdateComponent } from './update/isic-economic-activity-update.component';
import { IsicEconomicActivityDeleteDialogComponent } from './delete/isic-economic-activity-delete-dialog.component';
import { IsicEconomicActivityRoutingModule } from './route/isic-economic-activity-routing.module';

@NgModule({
  imports: [SharedModule, IsicEconomicActivityRoutingModule],
  declarations: [
    IsicEconomicActivityComponent,
    IsicEconomicActivityDetailComponent,
    IsicEconomicActivityUpdateComponent,
    IsicEconomicActivityDeleteDialogComponent,
  ],
  entryComponents: [IsicEconomicActivityDeleteDialogComponent],
})
export class IsicEconomicActivityModule {}
