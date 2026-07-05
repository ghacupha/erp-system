import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BouncedChequeCategoriesComponent } from './list/bounced-cheque-categories.component';
import { BouncedChequeCategoriesDetailComponent } from './detail/bounced-cheque-categories-detail.component';
import { BouncedChequeCategoriesUpdateComponent } from './update/bounced-cheque-categories-update.component';
import { BouncedChequeCategoriesDeleteDialogComponent } from './delete/bounced-cheque-categories-delete-dialog.component';
import { BouncedChequeCategoriesRoutingModule } from './route/bounced-cheque-categories-routing.module';

@NgModule({
  imports: [SharedModule, BouncedChequeCategoriesRoutingModule],
  declarations: [
    BouncedChequeCategoriesComponent,
    BouncedChequeCategoriesDetailComponent,
    BouncedChequeCategoriesUpdateComponent,
    BouncedChequeCategoriesDeleteDialogComponent,
  ],
  entryComponents: [BouncedChequeCategoriesDeleteDialogComponent],
})
export class BouncedChequeCategoriesModule {}
