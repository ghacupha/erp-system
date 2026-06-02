import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChannelTypeComponent } from './list/channel-type.component';
import { ChannelTypeDetailComponent } from './detail/channel-type-detail.component';
import { ChannelTypeUpdateComponent } from './update/channel-type-update.component';
import { ChannelTypeDeleteDialogComponent } from './delete/channel-type-delete-dialog.component';
import { ChannelTypeRoutingModule } from './route/channel-type-routing.module';

@NgModule({
  imports: [SharedModule, ChannelTypeRoutingModule],
  declarations: [ChannelTypeComponent, ChannelTypeDetailComponent, ChannelTypeUpdateComponent, ChannelTypeDeleteDialogComponent],
  entryComponents: [ChannelTypeDeleteDialogComponent],
})
export class ChannelTypeModule {}
