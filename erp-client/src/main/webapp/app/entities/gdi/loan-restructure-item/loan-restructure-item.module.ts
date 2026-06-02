import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanRestructureItemComponent } from './list/loan-restructure-item.component';
import { LoanRestructureItemDetailComponent } from './detail/loan-restructure-item-detail.component';
import { LoanRestructureItemUpdateComponent } from './update/loan-restructure-item-update.component';
import { LoanRestructureItemDeleteDialogComponent } from './delete/loan-restructure-item-delete-dialog.component';
import { LoanRestructureItemRoutingModule } from './route/loan-restructure-item-routing.module';

@NgModule({
  imports: [SharedModule, LoanRestructureItemRoutingModule],
  declarations: [
    LoanRestructureItemComponent,
    LoanRestructureItemDetailComponent,
    LoanRestructureItemUpdateComponent,
    LoanRestructureItemDeleteDialogComponent,
  ],
  entryComponents: [LoanRestructureItemDeleteDialogComponent],
})
export class LoanRestructureItemModule {}
