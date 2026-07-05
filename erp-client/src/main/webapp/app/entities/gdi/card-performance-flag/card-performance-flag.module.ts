import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardPerformanceFlagComponent } from './list/card-performance-flag.component';
import { CardPerformanceFlagDetailComponent } from './detail/card-performance-flag-detail.component';
import { CardPerformanceFlagUpdateComponent } from './update/card-performance-flag-update.component';
import { CardPerformanceFlagDeleteDialogComponent } from './delete/card-performance-flag-delete-dialog.component';
import { CardPerformanceFlagRoutingModule } from './route/card-performance-flag-routing.module';

@NgModule({
  imports: [SharedModule, CardPerformanceFlagRoutingModule],
  declarations: [
    CardPerformanceFlagComponent,
    CardPerformanceFlagDetailComponent,
    CardPerformanceFlagUpdateComponent,
    CardPerformanceFlagDeleteDialogComponent,
  ],
  entryComponents: [CardPerformanceFlagDeleteDialogComponent],
})
export class CardPerformanceFlagModule {}
