import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbAccountStatus } from '../crb-account-status.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-account-status-detail',
  templateUrl: './crb-account-status-detail.component.html',
})
export class CrbAccountStatusDetailComponent implements OnInit {
  crbAccountStatus: ICrbAccountStatus | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAccountStatus }) => {
      this.crbAccountStatus = crbAccountStatus;
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
