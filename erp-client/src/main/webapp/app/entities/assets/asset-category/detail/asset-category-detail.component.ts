import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetCategory } from '../asset-category.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-asset-category-detail',
  templateUrl: './asset-category-detail.component.html',
})
export class AssetCategoryDetailComponent implements OnInit {
  assetCategory: IAssetCategory | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetCategory }) => {
      this.assetCategory = assetCategory;
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
