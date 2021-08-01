import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetDepreciation } from '../fixed-asset-depreciation.model';

@Component({
  selector: 'jhi-fixed-asset-depreciation-detail',
  templateUrl: './fixed-asset-depreciation-detail.component.html',
})
export class FixedAssetDepreciationDetailComponent implements OnInit {
  fixedAssetDepreciation: IFixedAssetDepreciation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetDepreciation }) => {
      this.fixedAssetDepreciation = fixedAssetDepreciation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
