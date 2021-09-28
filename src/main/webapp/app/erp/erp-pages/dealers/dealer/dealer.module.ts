import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DealerComponent } from './list/dealer.component';
import { DealerDetailComponent } from './detail/dealer-detail.component';
import { DealerUpdateComponent } from './update/dealer-update.component';
import { DealerDeleteDialogComponent } from './delete/dealer-delete-dialog.component';
import { DealerRoutingModule } from './route/dealer-routing.module';
import {StoreModule} from '@ngrx/store';
import {
  dealerWorkflowStateReducer,
  paymentToDealerWorkflowStateSelector
} from '../../../store/reducers/dealer-workflows-status.reducer';
import {paymentUpdateFormStateSelector, paymentUpdateStateReducer} from "../../../store/reducers/update-menu-status.reducer";

@NgModule({
  imports: [
    SharedModule,
    DealerRoutingModule,
  ],
  declarations: [DealerComponent, DealerDetailComponent, DealerUpdateComponent, DealerDeleteDialogComponent],
  entryComponents: [DealerDeleteDialogComponent],
})
export class ErpServiceDealerModule {}
