import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionBaseComponent } from '../list/question-base.component';
import { QuestionBaseDetailComponent } from '../detail/question-base-detail.component';
import { QuestionBaseUpdateComponent } from '../update/question-base-update.component';
import { QuestionBaseRoutingResolveService } from './question-base-routing-resolve.service';

const questionBaseRoute: Routes = [
  {
    path: '',
    component: QuestionBaseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionBaseDetailComponent,
    resolve: {
      questionBase: QuestionBaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionBaseUpdateComponent,
    resolve: {
      questionBase: QuestionBaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionBaseUpdateComponent,
    resolve: {
      questionBase: QuestionBaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionBaseRoute)],
  exports: [RouterModule],
})
export class QuestionBaseRoutingModule {}
