import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouDepreciationEntry } from '../rou-depreciation-entry.model';

@Component({
  selector: 'jhi-rou-depreciation-entry-detail',
  templateUrl: './rou-depreciation-entry-detail.component.html',
})
export class RouDepreciationEntryDetailComponent implements OnInit {
  rouDepreciationEntry: IRouDepreciationEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationEntry }) => {
      this.rouDepreciationEntry = rouDepreciationEntry;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
