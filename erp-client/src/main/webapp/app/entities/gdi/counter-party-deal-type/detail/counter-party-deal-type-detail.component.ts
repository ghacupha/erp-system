import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICounterPartyDealType } from '../counter-party-deal-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-counter-party-deal-type-detail',
  templateUrl: './counter-party-deal-type-detail.component.html',
})
export class CounterPartyDealTypeDetailComponent implements OnInit {
  counterPartyDealType: ICounterPartyDealType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterPartyDealType }) => {
      this.counterPartyDealType = counterPartyDealType;
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
