import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CreditNoteComponent } from '../list/credit-note.component';
import { CreditNoteDetailComponent } from '../detail/credit-note-detail.component';
import { CreditNoteUpdateComponent } from '../update/credit-note-update.component';
import { CreditNoteRoutingResolveService } from './credit-note-routing-resolve.service';

const creditNoteRoute: Routes = [
  {
    path: '',
    component: CreditNoteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CreditNoteDetailComponent,
    resolve: {
      creditNote: CreditNoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CreditNoteUpdateComponent,
    resolve: {
      creditNote: CreditNoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CreditNoteUpdateComponent,
    resolve: {
      creditNote: CreditNoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(creditNoteRoute)],
  exports: [RouterModule],
})
export class CreditNoteRoutingModule {}
