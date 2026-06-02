import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TerminalTypesComponent } from '../list/terminal-types.component';
import { TerminalTypesDetailComponent } from '../detail/terminal-types-detail.component';
import { TerminalTypesUpdateComponent } from '../update/terminal-types-update.component';
import { TerminalTypesRoutingResolveService } from './terminal-types-routing-resolve.service';

const terminalTypesRoute: Routes = [
  {
    path: '',
    component: TerminalTypesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TerminalTypesDetailComponent,
    resolve: {
      terminalTypes: TerminalTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TerminalTypesUpdateComponent,
    resolve: {
      terminalTypes: TerminalTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TerminalTypesUpdateComponent,
    resolve: {
      terminalTypes: TerminalTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(terminalTypesRoute)],
  exports: [RouterModule],
})
export class TerminalTypesRoutingModule {}
