import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIssuersOfSecurities } from '../issuers-of-securities.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-issuers-of-securities-detail',
  templateUrl: './issuers-of-securities-detail.component.html',
})
export class IssuersOfSecuritiesDetailComponent implements OnInit {
  issuersOfSecurities: IIssuersOfSecurities | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issuersOfSecurities }) => {
      this.issuersOfSecurities = issuersOfSecurities;
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
