import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityCompilation } from '../lease-liability-compilation.model';

@Component({
  selector: 'jhi-lease-liability-compilation-detail',
  templateUrl: './lease-liability-compilation-detail.component.html',
})
export class LeaseLiabilityCompilationDetailComponent implements OnInit {
  leaseLiabilityCompilation: ILeaseLiabilityCompilation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityCompilation }) => {
      this.leaseLiabilityCompilation = leaseLiabilityCompilation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
