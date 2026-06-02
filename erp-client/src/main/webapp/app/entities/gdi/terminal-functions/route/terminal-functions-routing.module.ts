import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TerminalFunctionsComponent } from '../list/terminal-functions.component';
import { TerminalFunctionsDetailComponent } from '../detail/terminal-functions-detail.component';
import { TerminalFunctionsUpdateComponent } from '../update/terminal-functions-update.component';
import { TerminalFunctionsRoutingResolveService } from './terminal-functions-routing-resolve.service';

const terminalFunctionsRoute: Routes = [
  {
    path: '',
    component: TerminalFunctionsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TerminalFunctionsDetailComponent,
    resolve: {
      terminalFunctions: TerminalFunctionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TerminalFunctionsUpdateComponent,
    resolve: {
      terminalFunctions: TerminalFunctionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TerminalFunctionsUpdateComponent,
    resolve: {
      terminalFunctions: TerminalFunctionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(terminalFunctionsRoute)],
  exports: [RouterModule],
})
export class TerminalFunctionsRoutingModule {}
