import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetWriteOff } from '../asset-write-off.model';

@Component({
  selector: 'jhi-asset-write-off-detail',
  templateUrl: './asset-write-off-detail.component.html',
})
export class AssetWriteOffDetailComponent implements OnInit {
  assetWriteOff: IAssetWriteOff | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetWriteOff }) => {
      this.assetWriteOff = assetWriteOff;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
