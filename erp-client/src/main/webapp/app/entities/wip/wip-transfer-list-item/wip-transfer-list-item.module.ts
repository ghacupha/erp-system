import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WIPTransferListItemComponent } from './list/wip-transfer-list-item.component';
import { WIPTransferListItemDetailComponent } from './detail/wip-transfer-list-item-detail.component';
import { WIPTransferListItemRoutingModule } from './route/wip-transfer-list-item-routing.module';

@NgModule({
  imports: [SharedModule, WIPTransferListItemRoutingModule],
  declarations: [WIPTransferListItemComponent, WIPTransferListItemDetailComponent],
})
export class WIPTransferListItemModule {}
