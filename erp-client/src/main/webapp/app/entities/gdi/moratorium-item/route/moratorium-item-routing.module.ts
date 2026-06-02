import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MoratoriumItemComponent } from '../list/moratorium-item.component';
import { MoratoriumItemDetailComponent } from '../detail/moratorium-item-detail.component';
import { MoratoriumItemUpdateComponent } from '../update/moratorium-item-update.component';
import { MoratoriumItemRoutingResolveService } from './moratorium-item-routing-resolve.service';

const moratoriumItemRoute: Routes = [
  {
    path: '',
    component: MoratoriumItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MoratoriumItemDetailComponent,
    resolve: {
      moratoriumItem: MoratoriumItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MoratoriumItemUpdateComponent,
    resolve: {
      moratoriumItem: MoratoriumItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MoratoriumItemUpdateComponent,
    resolve: {
      moratoriumItem: MoratoriumItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(moratoriumItemRoute)],
  exports: [RouterModule],
})
export class MoratoriumItemRoutingModule {}
