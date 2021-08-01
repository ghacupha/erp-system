import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MessageTokenComponent } from './list/message-token.component';
import { MessageTokenDetailComponent } from './detail/message-token-detail.component';
import { MessageTokenUpdateComponent } from './update/message-token-update.component';
import { MessageTokenDeleteDialogComponent } from './delete/message-token-delete-dialog.component';
import { MessageTokenRoutingModule } from './route/message-token-routing.module';

@NgModule({
  imports: [SharedModule, MessageTokenRoutingModule],
  declarations: [MessageTokenComponent, MessageTokenDetailComponent, MessageTokenUpdateComponent, MessageTokenDeleteDialogComponent],
  entryComponents: [MessageTokenDeleteDialogComponent],
})
export class ErpServiceMessageTokenModule {}
