import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICreditCardOwnership } from '../credit-card-ownership.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-credit-card-ownership-detail',
  templateUrl: './credit-card-ownership-detail.component.html',
})
export class CreditCardOwnershipDetailComponent implements OnInit {
  creditCardOwnership: ICreditCardOwnership | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditCardOwnership }) => {
      this.creditCardOwnership = creditCardOwnership;
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
