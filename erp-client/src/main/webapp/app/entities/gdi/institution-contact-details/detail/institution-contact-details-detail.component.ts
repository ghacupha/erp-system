import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInstitutionContactDetails } from '../institution-contact-details.model';

@Component({
  selector: 'jhi-institution-contact-details-detail',
  templateUrl: './institution-contact-details-detail.component.html',
})
export class InstitutionContactDetailsDetailComponent implements OnInit {
  institutionContactDetails: IInstitutionContactDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ institutionContactDetails }) => {
      this.institutionContactDetails = institutionContactDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
