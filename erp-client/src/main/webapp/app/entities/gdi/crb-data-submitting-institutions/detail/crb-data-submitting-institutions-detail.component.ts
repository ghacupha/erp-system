import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbDataSubmittingInstitutions } from '../crb-data-submitting-institutions.model';

@Component({
  selector: 'jhi-crb-data-submitting-institutions-detail',
  templateUrl: './crb-data-submitting-institutions-detail.component.html',
})
export class CrbDataSubmittingInstitutionsDetailComponent implements OnInit {
  crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbDataSubmittingInstitutions }) => {
      this.crbDataSubmittingInstitutions = crbDataSubmittingInstitutions;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
