import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUniversallyUniqueMapping } from '../universally-unique-mapping.model';

@Component({
  selector: 'jhi-universally-unique-mapping-detail',
  templateUrl: './universally-unique-mapping-detail.component.html',
})
export class UniversallyUniqueMappingDetailComponent implements OnInit {
  universallyUniqueMapping: IUniversallyUniqueMapping | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ universallyUniqueMapping }) => {
      this.universallyUniqueMapping = universallyUniqueMapping;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
