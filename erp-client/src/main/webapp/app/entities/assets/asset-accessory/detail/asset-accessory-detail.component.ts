import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetAccessory } from '../asset-accessory.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-asset-accessory-detail',
  templateUrl: './asset-accessory-detail.component.html',
})
export class AssetAccessoryDetailComponent implements OnInit {
  assetAccessory: IAssetAccessory | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetAccessory }) => {
      this.assetAccessory = assetAccessory;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
