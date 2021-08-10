import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetNetBookValue } from '../fixed-asset-net-book-value.model';

@Component({
  selector: 'gha-fixed-asset-net-book-value-detail',
  templateUrl: './fixed-asset-net-book-value-detail.component.html',
})
export class FixedAssetNetBookValueDetailComponent implements OnInit {
  fixedAssetNetBookValue: IFixedAssetNetBookValue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetNetBookValue }) => {
      this.fixedAssetNetBookValue = fixedAssetNetBookValue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
