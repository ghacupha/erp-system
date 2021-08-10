import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MessageTokenComponent } from '../list/message-token.component';
import { MessageTokenDetailComponent } from '../detail/message-token-detail.component';
import { MessageTokenUpdateComponent } from '../update/message-token-update.component';
import { MessageTokenRoutingResolveService } from './message-token-routing-resolve.service';

const messageTokenRoute: Routes = [
  {
    path: '',
    component: MessageTokenComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MessageTokenDetailComponent,
    resolve: {
      messageToken: MessageTokenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MessageTokenUpdateComponent,
    resolve: {
      messageToken: MessageTokenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MessageTokenUpdateComponent,
    resolve: {
      messageToken: MessageTokenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(messageTokenRoute)],
  exports: [RouterModule],
})
export class MessageTokenRoutingModule {}
