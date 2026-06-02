import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BouncedChequeCategoriesComponent } from '../list/bounced-cheque-categories.component';
import { BouncedChequeCategoriesDetailComponent } from '../detail/bounced-cheque-categories-detail.component';
import { BouncedChequeCategoriesUpdateComponent } from '../update/bounced-cheque-categories-update.component';
import { BouncedChequeCategoriesRoutingResolveService } from './bounced-cheque-categories-routing-resolve.service';

const bouncedChequeCategoriesRoute: Routes = [
  {
    path: '',
    component: BouncedChequeCategoriesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BouncedChequeCategoriesDetailComponent,
    resolve: {
      bouncedChequeCategories: BouncedChequeCategoriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BouncedChequeCategoriesUpdateComponent,
    resolve: {
      bouncedChequeCategories: BouncedChequeCategoriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BouncedChequeCategoriesUpdateComponent,
    resolve: {
      bouncedChequeCategories: BouncedChequeCategoriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bouncedChequeCategoriesRoute)],
  exports: [RouterModule],
})
export class BouncedChequeCategoriesRoutingModule {}
