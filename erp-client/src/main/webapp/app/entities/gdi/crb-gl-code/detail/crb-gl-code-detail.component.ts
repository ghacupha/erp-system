import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbGlCode } from '../crb-gl-code.model';

@Component({
  selector: 'jhi-crb-gl-code-detail',
  templateUrl: './crb-gl-code-detail.component.html',
})
export class CrbGlCodeDetailComponent implements OnInit {
  crbGlCode: ICrbGlCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbGlCode }) => {
      this.crbGlCode = crbGlCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
