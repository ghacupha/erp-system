import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetRevaluation } from '../asset-revaluation.model';

@Component({
  selector: 'jhi-asset-revaluation-detail',
  templateUrl: './asset-revaluation-detail.component.html',
})
export class AssetRevaluationDetailComponent implements OnInit {
  assetRevaluation: IAssetRevaluation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetRevaluation }) => {
      this.assetRevaluation = assetRevaluation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
