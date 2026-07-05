import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbAgingBandsComponent } from './list/crb-aging-bands.component';
import { CrbAgingBandsDetailComponent } from './detail/crb-aging-bands-detail.component';
import { CrbAgingBandsUpdateComponent } from './update/crb-aging-bands-update.component';
import { CrbAgingBandsDeleteDialogComponent } from './delete/crb-aging-bands-delete-dialog.component';
import { CrbAgingBandsRoutingModule } from './route/crb-aging-bands-routing.module';

@NgModule({
  imports: [SharedModule, CrbAgingBandsRoutingModule],
  declarations: [CrbAgingBandsComponent, CrbAgingBandsDetailComponent, CrbAgingBandsUpdateComponent, CrbAgingBandsDeleteDialogComponent],
  entryComponents: [CrbAgingBandsDeleteDialogComponent],
})
export class CrbAgingBandsModule {}
