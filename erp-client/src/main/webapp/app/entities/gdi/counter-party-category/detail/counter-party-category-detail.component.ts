import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICounterPartyCategory } from '../counter-party-category.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-counter-party-category-detail',
  templateUrl: './counter-party-category-detail.component.html',
})
export class CounterPartyCategoryDetailComponent implements OnInit {
  counterPartyCategory: ICounterPartyCategory | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterPartyCategory }) => {
      this.counterPartyCategory = counterPartyCategory;
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
