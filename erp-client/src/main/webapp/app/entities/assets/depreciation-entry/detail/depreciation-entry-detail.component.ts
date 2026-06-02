import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepreciationEntry } from '../depreciation-entry.model';

@Component({
  selector: 'jhi-depreciation-entry-detail',
  templateUrl: './depreciation-entry-detail.component.html',
})
export class DepreciationEntryDetailComponent implements OnInit {
  depreciationEntry: IDepreciationEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationEntry }) => {
      this.depreciationEntry = depreciationEntry;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
