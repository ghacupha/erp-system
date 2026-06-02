import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProfessionalQualificationComponent } from '../list/professional-qualification.component';
import { ProfessionalQualificationDetailComponent } from '../detail/professional-qualification-detail.component';
import { ProfessionalQualificationUpdateComponent } from '../update/professional-qualification-update.component';
import { ProfessionalQualificationRoutingResolveService } from './professional-qualification-routing-resolve.service';

const professionalQualificationRoute: Routes = [
  {
    path: '',
    component: ProfessionalQualificationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProfessionalQualificationDetailComponent,
    resolve: {
      professionalQualification: ProfessionalQualificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProfessionalQualificationUpdateComponent,
    resolve: {
      professionalQualification: ProfessionalQualificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProfessionalQualificationUpdateComponent,
    resolve: {
      professionalQualification: ProfessionalQualificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(professionalQualificationRoute)],
  exports: [RouterModule],
})
export class ProfessionalQualificationRoutingModule {}
