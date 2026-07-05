import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INetBookValueEntry } from '../net-book-value-entry.model';

@Component({
  selector: 'jhi-net-book-value-entry-detail',
  templateUrl: './net-book-value-entry-detail.component.html',
})
export class NetBookValueEntryDetailComponent implements OnInit {
  netBookValueEntry: INetBookValueEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ netBookValueEntry }) => {
      this.netBookValueEntry = netBookValueEntry;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
