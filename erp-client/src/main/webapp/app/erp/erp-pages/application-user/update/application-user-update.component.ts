///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApplicationUser, ApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';
import { IDealer } from '../../dealers/dealer/dealer.model';
import { ISecurityClearance } from '../../security-clearance/security-clearance.model';
import { IUser } from '../../../../admin/user-management/user-management.model';
import { IUniversallyUniqueMapping } from '../../universally-unique-mapping/universally-unique-mapping.model';
import { DealerService } from '../../dealers/dealer/service/dealer.service';
import { UserService } from '../../../../core/user/user.service';
import { UniversallyUniqueMappingService } from '../../universally-unique-mapping/service/universally-unique-mapping.service';
import { SecurityClearanceService } from '../../security-clearance/service/security-clearance.service';
import { v4 as uuidv4 } from 'uuid';
import { IPlaceholder } from '../../placeholder/placeholder.model';
import { PlaceholderService } from '../../placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-application-user-update',
  templateUrl: './application-user-update.component.html',
})
export class ApplicationUserUpdateComponent implements OnInit {
  isSaving = false;

  dealersSharedCollection: IDealer[] = [];
  dealerIdentitiesCollection: IDealer[] = [];
  securityClearancesSharedCollection: ISecurityClearance[] = [];
  usersSharedCollection: IUser[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    designation: [null, [Validators.required]],
    applicationIdentity: [null, [Validators.required]],
    organization: [null, Validators.required],
    department: [null, Validators.required],
    securityClearance: [null, Validators.required],
    systemIdentity: [null, Validators.required],
    userProperties: [],
    dealerIdentity: [null, Validators.required],
    placeholders: []
  });

  constructor(
    protected applicationUserService: ApplicationUserService,
    protected dealerService: DealerService,
    protected securityClearanceService: SecurityClearanceService,
    protected userService: UserService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.updateForm(applicationUser);

      this.loadRelationshipsOptions();
    });
    this.editForm.patchValue({
      designation: uuidv4(),
    })
  }

  updateDealerIdentity(update: IDealer): void {
    this.editForm.patchValue({
      dealerIdentity: update
    });
  }

  updatePlaceholders(updates: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...updates]
    });
  }

  updateSecurityClearance(update: ISecurityClearance): void {
    this.editForm.patchValue({
      securityClearance: update
    })
  }

  updateSystemIdentity(update: IUser): void {
    this.editForm.patchValue({
      systemIdentity: update
    })
  }

  updateParameters(update: IUniversallyUniqueMapping[]): void {
    this.editForm.patchValue({
      parameters: [...update]
    })
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateOrganization(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      organization: dealerUpdate,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateDepartment(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      department: dealerUpdate,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationUser = this.createFromForm();
    if (applicationUser.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationUserService.update(applicationUser));
    } else {
      this.subscribeToSaveResponse(this.applicationUserService.create(applicationUser));
    }
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackSecurityClearanceById(index: number, item: ISecurityClearance): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  getSelectedUniversallyUniqueMapping(
    option: IUniversallyUniqueMapping,
    selectedVals?: IUniversallyUniqueMapping[]
  ): IUniversallyUniqueMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(applicationUser: IApplicationUser): void {
    this.editForm.patchValue({
      id: applicationUser.id,
      designation: applicationUser.designation,
      applicationIdentity: applicationUser.applicationIdentity,
      organization: applicationUser.organization,
      department: applicationUser.department,
      securityClearance: applicationUser.securityClearance,
      systemIdentity: applicationUser.systemIdentity,
      userProperties: applicationUser.userProperties,
      dealerIdentity: applicationUser.dealerIdentity,
      placeholders: applicationUser.placeholders,
    });

    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      applicationUser.organization,
      applicationUser.department
    );
    this.dealerIdentitiesCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealerIdentitiesCollection,
      applicationUser.dealerIdentity
    );
    this.securityClearancesSharedCollection = this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
      this.securityClearancesSharedCollection,
      applicationUser.securityClearance
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, applicationUser.systemIdentity);
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(applicationUser.userProperties ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(applicationUser.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            this.editForm.get('organization')!.value,
            this.editForm.get('department')!.value
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.dealerService
      .query({ 'applicationUserId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealerIdentity')!.value))
      )
      .subscribe((dealers: IDealer[]) => (this.dealerIdentitiesCollection = dealers));

    this.securityClearanceService
      .query()
      .pipe(map((res: HttpResponse<ISecurityClearance[]>) => res.body ?? []))
      .pipe(
        map((securityClearances: ISecurityClearance[]) =>
          this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
            securityClearances,
            this.editForm.get('securityClearance')!.value
          )
        )
      )
      .subscribe((securityClearances: ISecurityClearance[]) => (this.securityClearancesSharedCollection = securityClearances));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('systemIdentity')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('userProperties')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(
            placeholders,
            ...(this.editForm.get('placeholders')!.value ?? [])
          )
        )
      )
      .subscribe(
        (placeholders: IPlaceholder[]) =>
          (this.placeholdersSharedCollection = placeholders)
      );
  }

  protected createFromForm(): IApplicationUser {
    return {
      ...new ApplicationUser(),
      id: this.editForm.get(['id'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      applicationIdentity: this.editForm.get(['applicationIdentity'])!.value,
      organization: this.editForm.get(['organization'])!.value,
      department: this.editForm.get(['department'])!.value,
      securityClearance: this.editForm.get(['securityClearance'])!.value,
      systemIdentity: this.editForm.get(['systemIdentity'])!.value,
      userProperties: this.editForm.get(['userProperties'])!.value,
      dealerIdentity: this.editForm.get(['dealerIdentity'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
