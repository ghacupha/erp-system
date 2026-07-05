import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRelatedPartyRelationship } from '../related-party-relationship.model';

@Component({
  selector: 'jhi-related-party-relationship-detail',
  templateUrl: './related-party-relationship-detail.component.html',
})
export class RelatedPartyRelationshipDetailComponent implements OnInit {
  relatedPartyRelationship: IRelatedPartyRelationship | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatedPartyRelationship }) => {
      this.relatedPartyRelationship = relatedPartyRelationship;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
