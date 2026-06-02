import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISnaSectorCode } from '../sna-sector-code.model';

@Component({
  selector: 'jhi-sna-sector-code-detail',
  templateUrl: './sna-sector-code-detail.component.html',
})
export class SnaSectorCodeDetailComponent implements OnInit {
  snaSectorCode: ISnaSectorCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ snaSectorCode }) => {
      this.snaSectorCode = snaSectorCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
