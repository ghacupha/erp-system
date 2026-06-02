import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TALeaseRecognitionRuleComponent } from '../list/ta-lease-recognition-rule.component';
import { TALeaseRecognitionRuleDetailComponent } from '../detail/ta-lease-recognition-rule-detail.component';
import { TALeaseRecognitionRuleUpdateComponent } from '../update/ta-lease-recognition-rule-update.component';
import { TALeaseRecognitionRuleRoutingResolveService } from './ta-lease-recognition-rule-routing-resolve.service';

const tALeaseRecognitionRuleRoute: Routes = [
  {
    path: '',
    component: TALeaseRecognitionRuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TALeaseRecognitionRuleDetailComponent,
    resolve: {
      tALeaseRecognitionRule: TALeaseRecognitionRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TALeaseRecognitionRuleUpdateComponent,
    resolve: {
      tALeaseRecognitionRule: TALeaseRecognitionRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TALeaseRecognitionRuleUpdateComponent,
    resolve: {
      tALeaseRecognitionRule: TALeaseRecognitionRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tALeaseRecognitionRuleRoute)],
  exports: [RouterModule],
})
export class TALeaseRecognitionRuleRoutingModule {}
