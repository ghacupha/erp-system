import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'fixed-asset-acquisition',
        data: { pageTitle: 'FixedAssetAcquisitions' },
        loadChildren: () =>
          import('./assets/fixed-asset-acquisition/fixed-asset-acquisition.module').then(m => m.ErpServiceFixedAssetAcquisitionModule),
      },
      {
        path: 'fixed-asset-net-book-value',
        data: { pageTitle: 'FixedAssetNetBookValues' },
        loadChildren: () =>
          import('./assets/fixed-asset-net-book-value/fixed-asset-net-book-value.module').then(
            m => m.ErpServiceFixedAssetNetBookValueModule
          ),
      },
      {
        path: 'fixed-asset-depreciation',
        data: { pageTitle: 'FixedAssetDepreciations' },
        loadChildren: () =>
          import('./assets/fixed-asset-depreciation/fixed-asset-depreciation.module').then(m => m.ErpServiceFixedAssetDepreciationModule),
      },
      {
        path: 'dealer',
        data: { pageTitle: 'Dealers' },
        loadChildren: () => import('./dealers/dealer/dealer.module').then(m => m.ErpServiceDealerModule),
      },
      {
        path: 'placeholder',
        data: { pageTitle: 'Placeholders' },
        loadChildren: () => import('./erpService/placeholder/placeholder.module').then(m => m.ErpServicePlaceholderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
