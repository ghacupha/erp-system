import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGlMapping } from '../gl-mapping.model';

@Component({
  selector: 'jhi-gl-mapping-detail',
  templateUrl: './gl-mapping-detail.component.html',
})
export class GlMappingDetailComponent implements OnInit {
  glMapping: IGlMapping | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ glMapping }) => {
      this.glMapping = glMapping;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
