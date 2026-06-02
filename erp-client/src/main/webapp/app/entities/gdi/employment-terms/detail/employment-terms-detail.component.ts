import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmploymentTerms } from '../employment-terms.model';

@Component({
  selector: 'jhi-employment-terms-detail',
  templateUrl: './employment-terms-detail.component.html',
})
export class EmploymentTermsDetailComponent implements OnInit {
  employmentTerms: IEmploymentTerms | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentTerms }) => {
      this.employmentTerms = employmentTerms;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
