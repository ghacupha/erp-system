import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetDisposal } from '../asset-disposal.model';

@Component({
  selector: 'jhi-asset-disposal-detail',
  templateUrl: './asset-disposal-detail.component.html',
})
export class AssetDisposalDetailComponent implements OnInit {
  assetDisposal: IAssetDisposal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetDisposal }) => {
      this.assetDisposal = assetDisposal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
