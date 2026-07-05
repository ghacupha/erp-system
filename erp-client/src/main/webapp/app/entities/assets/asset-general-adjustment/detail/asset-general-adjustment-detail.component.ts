import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetGeneralAdjustment } from '../asset-general-adjustment.model';

@Component({
  selector: 'jhi-asset-general-adjustment-detail',
  templateUrl: './asset-general-adjustment-detail.component.html',
})
export class AssetGeneralAdjustmentDetailComponent implements OnInit {
  assetGeneralAdjustment: IAssetGeneralAdjustment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetGeneralAdjustment }) => {
      this.assetGeneralAdjustment = assetGeneralAdjustment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
