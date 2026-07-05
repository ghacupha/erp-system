import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetRegistration } from '../asset-registration.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-asset-registration-detail',
  templateUrl: './asset-registration-detail.component.html',
})
export class AssetRegistrationDetailComponent implements OnInit {
  assetRegistration: IAssetRegistration | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetRegistration }) => {
      this.assetRegistration = assetRegistration;
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
