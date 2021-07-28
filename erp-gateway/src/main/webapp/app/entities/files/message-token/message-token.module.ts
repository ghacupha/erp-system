import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { MessageTokenComponent } from './message-token.component';
import { MessageTokenDetailComponent } from './message-token-detail.component';
import { MessageTokenUpdateComponent } from './message-token-update.component';
import { MessageTokenDeleteDialogComponent } from './message-token-delete-dialog.component';
import { messageTokenRoute } from './message-token.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(messageTokenRoute)],
  declarations: [MessageTokenComponent, MessageTokenDetailComponent, MessageTokenUpdateComponent, MessageTokenDeleteDialogComponent],
  entryComponents: [MessageTokenDeleteDialogComponent],
})
export class ErpServiceMessageTokenModule {}
