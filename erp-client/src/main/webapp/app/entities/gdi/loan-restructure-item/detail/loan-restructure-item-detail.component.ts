import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoanRestructureItem } from '../loan-restructure-item.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-loan-restructure-item-detail',
  templateUrl: './loan-restructure-item-detail.component.html',
})
export class LoanRestructureItemDetailComponent implements OnInit {
  loanRestructureItem: ILoanRestructureItem | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanRestructureItem }) => {
      this.loanRestructureItem = loanRestructureItem;
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
