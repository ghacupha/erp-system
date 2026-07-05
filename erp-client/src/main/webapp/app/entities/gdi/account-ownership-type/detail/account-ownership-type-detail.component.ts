import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountOwnershipType } from '../account-ownership-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-account-ownership-type-detail',
  templateUrl: './account-ownership-type-detail.component.html',
})
export class AccountOwnershipTypeDetailComponent implements OnInit {
  accountOwnershipType: IAccountOwnershipType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountOwnershipType }) => {
      this.accountOwnershipType = accountOwnershipType;
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
