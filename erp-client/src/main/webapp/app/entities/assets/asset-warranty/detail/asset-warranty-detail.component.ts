import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetWarranty } from '../asset-warranty.model';

@Component({
  selector: 'jhi-asset-warranty-detail',
  templateUrl: './asset-warranty-detail.component.html',
})
export class AssetWarrantyDetailComponent implements OnInit {
  assetWarranty: IAssetWarranty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetWarranty }) => {
      this.assetWarranty = assetWarranty;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
