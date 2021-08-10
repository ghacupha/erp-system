import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetAcquisition } from '../fixed-asset-acquisition.model';

@Component({
  selector: 'gha-fixed-asset-acquisition-detail',
  templateUrl: './fixed-asset-acquisition-detail.component.html',
})
export class FixedAssetAcquisitionDetailComponent implements OnInit {
  fixedAssetAcquisition: IFixedAssetAcquisition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetAcquisition }) => {
      this.fixedAssetAcquisition = fixedAssetAcquisition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
