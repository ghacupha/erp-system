import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManagementMemberType } from '../management-member-type.model';

@Component({
  selector: 'jhi-management-member-type-detail',
  templateUrl: './management-member-type-detail.component.html',
})
export class ManagementMemberTypeDetailComponent implements OnInit {
  managementMemberType: IManagementMemberType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ managementMemberType }) => {
      this.managementMemberType = managementMemberType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
