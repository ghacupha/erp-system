import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbAccountHolderTypeComponent } from './list/crb-account-holder-type.component';
import { CrbAccountHolderTypeDetailComponent } from './detail/crb-account-holder-type-detail.component';
import { CrbAccountHolderTypeUpdateComponent } from './update/crb-account-holder-type-update.component';
import { CrbAccountHolderTypeDeleteDialogComponent } from './delete/crb-account-holder-type-delete-dialog.component';
import { CrbAccountHolderTypeRoutingModule } from './route/crb-account-holder-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbAccountHolderTypeRoutingModule],
  declarations: [
    CrbAccountHolderTypeComponent,
    CrbAccountHolderTypeDetailComponent,
    CrbAccountHolderTypeUpdateComponent,
    CrbAccountHolderTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbAccountHolderTypeDeleteDialogComponent],
})
export class CrbAccountHolderTypeModule {}
