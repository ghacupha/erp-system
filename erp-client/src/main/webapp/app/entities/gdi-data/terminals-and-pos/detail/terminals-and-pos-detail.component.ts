import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITerminalsAndPOS } from '../terminals-and-pos.model';

@Component({
  selector: 'jhi-terminals-and-pos-detail',
  templateUrl: './terminals-and-pos-detail.component.html',
})
export class TerminalsAndPOSDetailComponent implements OnInit {
  terminalsAndPOS: ITerminalsAndPOS | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ terminalsAndPOS }) => {
      this.terminalsAndPOS = terminalsAndPOS;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
