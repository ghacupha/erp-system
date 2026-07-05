import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WIPListItemComponent } from './list/wip-list-item.component';
import { WIPListItemDetailComponent } from './detail/wip-list-item-detail.component';
import { WIPListItemRoutingModule } from './route/wip-list-item-routing.module';

@NgModule({
  imports: [SharedModule, WIPListItemRoutingModule],
  declarations: [WIPListItemComponent, WIPListItemDetailComponent],
})
export class WIPListItemModule {}
