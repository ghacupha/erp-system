import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BankBranchCodeComponent } from '../list/bank-branch-code.component';
import { BankBranchCodeDetailComponent } from '../detail/bank-branch-code-detail.component';
import { BankBranchCodeUpdateComponent } from '../update/bank-branch-code-update.component';
import { BankBranchCodeRoutingResolveService } from './bank-branch-code-routing-resolve.service';

const bankBranchCodeRoute: Routes = [
  {
    path: '',
    component: BankBranchCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankBranchCodeDetailComponent,
    resolve: {
      bankBranchCode: BankBranchCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BankBranchCodeUpdateComponent,
    resolve: {
      bankBranchCode: BankBranchCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BankBranchCodeUpdateComponent,
    resolve: {
      bankBranchCode: BankBranchCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bankBranchCodeRoute)],
  exports: [RouterModule],
})
export class BankBranchCodeRoutingModule {}
