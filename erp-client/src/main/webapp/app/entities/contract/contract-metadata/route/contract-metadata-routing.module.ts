import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContractMetadataComponent } from '../list/contract-metadata.component';
import { ContractMetadataDetailComponent } from '../detail/contract-metadata-detail.component';
import { ContractMetadataUpdateComponent } from '../update/contract-metadata-update.component';
import { ContractMetadataRoutingResolveService } from './contract-metadata-routing-resolve.service';

const contractMetadataRoute: Routes = [
  {
    path: '',
    component: ContractMetadataComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractMetadataDetailComponent,
    resolve: {
      contractMetadata: ContractMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractMetadataUpdateComponent,
    resolve: {
      contractMetadata: ContractMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractMetadataUpdateComponent,
    resolve: {
      contractMetadata: ContractMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contractMetadataRoute)],
  exports: [RouterModule],
})
export class ContractMetadataRoutingModule {}
