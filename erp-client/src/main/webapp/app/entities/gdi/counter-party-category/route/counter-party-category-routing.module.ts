import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CounterPartyCategoryComponent } from '../list/counter-party-category.component';
import { CounterPartyCategoryDetailComponent } from '../detail/counter-party-category-detail.component';
import { CounterPartyCategoryUpdateComponent } from '../update/counter-party-category-update.component';
import { CounterPartyCategoryRoutingResolveService } from './counter-party-category-routing-resolve.service';

const counterPartyCategoryRoute: Routes = [
  {
    path: '',
    component: CounterPartyCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CounterPartyCategoryDetailComponent,
    resolve: {
      counterPartyCategory: CounterPartyCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CounterPartyCategoryUpdateComponent,
    resolve: {
      counterPartyCategory: CounterPartyCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CounterPartyCategoryUpdateComponent,
    resolve: {
      counterPartyCategory: CounterPartyCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(counterPartyCategoryRoute)],
  exports: [RouterModule],
})
export class CounterPartyCategoryRoutingModule {}
