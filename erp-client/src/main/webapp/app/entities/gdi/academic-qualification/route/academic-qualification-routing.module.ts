import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AcademicQualificationComponent } from '../list/academic-qualification.component';
import { AcademicQualificationDetailComponent } from '../detail/academic-qualification-detail.component';
import { AcademicQualificationUpdateComponent } from '../update/academic-qualification-update.component';
import { AcademicQualificationRoutingResolveService } from './academic-qualification-routing-resolve.service';

const academicQualificationRoute: Routes = [
  {
    path: '',
    component: AcademicQualificationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AcademicQualificationDetailComponent,
    resolve: {
      academicQualification: AcademicQualificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AcademicQualificationUpdateComponent,
    resolve: {
      academicQualification: AcademicQualificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AcademicQualificationUpdateComponent,
    resolve: {
      academicQualification: AcademicQualificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(academicQualificationRoute)],
  exports: [RouterModule],
})
export class AcademicQualificationRoutingModule {}
