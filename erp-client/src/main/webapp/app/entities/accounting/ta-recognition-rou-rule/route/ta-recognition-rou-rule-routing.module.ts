import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TARecognitionROURuleComponent } from '../list/ta-recognition-rou-rule.component';
import { TARecognitionROURuleDetailComponent } from '../detail/ta-recognition-rou-rule-detail.component';
import { TARecognitionROURuleUpdateComponent } from '../update/ta-recognition-rou-rule-update.component';
import { TARecognitionROURuleRoutingResolveService } from './ta-recognition-rou-rule-routing-resolve.service';

const tARecognitionROURuleRoute: Routes = [
  {
    path: '',
    component: TARecognitionROURuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TARecognitionROURuleDetailComponent,
    resolve: {
      tARecognitionROURule: TARecognitionROURuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TARecognitionROURuleUpdateComponent,
    resolve: {
      tARecognitionROURule: TARecognitionROURuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TARecognitionROURuleUpdateComponent,
    resolve: {
      tARecognitionROURule: TARecognitionROURuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tARecognitionROURuleRoute)],
  exports: [RouterModule],
})
export class TARecognitionROURuleRoutingModule {}
