import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StringQuestionBaseComponent } from '../list/string-question-base.component';
import { StringQuestionBaseDetailComponent } from '../detail/string-question-base-detail.component';
import { StringQuestionBaseUpdateComponent } from '../update/string-question-base-update.component';
import { StringQuestionBaseRoutingResolveService } from './string-question-base-routing-resolve.service';

const stringQuestionBaseRoute: Routes = [
  {
    path: '',
    component: StringQuestionBaseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StringQuestionBaseDetailComponent,
    resolve: {
      stringQuestionBase: StringQuestionBaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StringQuestionBaseUpdateComponent,
    resolve: {
      stringQuestionBase: StringQuestionBaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StringQuestionBaseUpdateComponent,
    resolve: {
      stringQuestionBase: StringQuestionBaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(stringQuestionBaseRoute)],
  exports: [RouterModule],
})
export class StringQuestionBaseRoutingModule {}
