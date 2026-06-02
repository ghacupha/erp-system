import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FinancialDerivativeTypeCodeComponent } from '../list/financial-derivative-type-code.component';
import { FinancialDerivativeTypeCodeDetailComponent } from '../detail/financial-derivative-type-code-detail.component';
import { FinancialDerivativeTypeCodeUpdateComponent } from '../update/financial-derivative-type-code-update.component';
import { FinancialDerivativeTypeCodeRoutingResolveService } from './financial-derivative-type-code-routing-resolve.service';

const financialDerivativeTypeCodeRoute: Routes = [
  {
    path: '',
    component: FinancialDerivativeTypeCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FinancialDerivativeTypeCodeDetailComponent,
    resolve: {
      financialDerivativeTypeCode: FinancialDerivativeTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FinancialDerivativeTypeCodeUpdateComponent,
    resolve: {
      financialDerivativeTypeCode: FinancialDerivativeTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FinancialDerivativeTypeCodeUpdateComponent,
    resolve: {
      financialDerivativeTypeCode: FinancialDerivativeTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(financialDerivativeTypeCodeRoute)],
  exports: [RouterModule],
})
export class FinancialDerivativeTypeCodeRoutingModule {}
