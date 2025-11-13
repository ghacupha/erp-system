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

import { IContractMetadata, ContractMetadata } from '../contract-metadata.model';
import { ContractMetadataService } from '../service/contract-metadata.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from '../../application-user/application-user.model';
import { ISecurityClearance } from '../../security-clearance/security-clearance.model';
import { ApplicationUserService } from '../../application-user/service/application-user.service';
import { ContractStatus } from '../../../erp-common/enumerations/contract-status.model';
import { PlaceholderService } from '../../placeholder/service/placeholder.service';
import { IDealer } from '../../dealers/dealer/dealer.model';
import { IPlaceholder } from '../../placeholder/placeholder.model';
import { IBusinessDocument } from '../../business-document/business-document.model';
import { SecurityClearanceService } from '../../security-clearance/service/security-clearance.service';
import { DealerService } from '../../dealers/dealer/service/dealer.service';
import { UniversallyUniqueMappingService } from '../../universally-unique-mapping/service/universally-unique-mapping.service';
import { BusinessDocumentService } from '../../business-document/service/business-document.service';
import { ContractType } from '../../../erp-common/enumerations/contract-type.model';
import { IUniversallyUniqueMapping } from '../../universally-unique-mapping/universally-unique-mapping.model';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'jhi-contract-metadata-update',
  templateUrl: './contract-metadata-update.component.html',
})
export class ContractMetadataUpdateComponent implements OnInit {
  isSaving = false;
  contractTypeValues = Object.keys(ContractType);
  contractStatusValues = Object.keys(ContractStatus);

  contractMetadataSharedCollection: IContractMetadata[] = [];
  dealersSharedCollection: IDealer[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  securityClearancesSharedCollection: ISecurityClearance[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    typeOfContract: [null, [Validators.required]],
    contractStatus: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    terminationDate: [null, [Validators.required]],
    commentsAndAttachment: [],
    contractTitle: [null, [Validators.required]],
    contractIdentifier: [null, [Validators.required]],
    contractIdentifierShort: [null, [Validators.required, Validators.minLength(6)]],
    relatedContracts: [],
    department: [],
    contractPartner: [],
    responsiblePerson: [],
    signatories: [],
    securityClearance: [],
    placeholders: [],
    contractDocumentFiles: [],
    contractMappings: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected contractMetadataService: ContractMetadataService,
    protected dealerService: DealerService,
    protected applicationUserService: ApplicationUserService,
    protected securityClearanceService: SecurityClearanceService,
    protected placeholderService: PlaceholderService,
    protected businessDocumentService: BusinessDocumentService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const reqSerial = uuidv4();

    this.activatedRoute.data.subscribe(({ contractMetadata }) => {

      if (contractMetadata.id === undefined) {


        this.editForm.patchValue({
          contractIdentifier: reqSerial,
          contractIdentifierShort: reqSerial.substring(0, 8),
        });

        this.updateForm(contractMetadata);

        this.loadRelationshipsOptions();
      }
    });

    this.editForm.patchValue({
      contractIdentifier: reqSerial,
      contractIdentifierShort: reqSerial.substring(0, 8),
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateRelatedContracts(update: IContractMetadata[]): void {
    this.editForm.patchValue({
      relatedContracts: [...update],
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateContractMappings(update: IUniversallyUniqueMapping[]): void {
    this.editForm.patchValue({
      contractMappings: [...update],
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateSignatories(update: IDealer[]): void {
    this.editForm.patchValue({
      signatories: [...update],
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateContractDocuments(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      contractDocumentFiles: [...update],
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateDepartment(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      department: dealerUpdate,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateContractPartner(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      contractPartner: dealerUpdate,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateResponsiblePerson(update: IApplicationUser): void {
    this.editForm.patchValue({
      responsiblePerson: update,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractMetadata = this.createFromForm();
    if (contractMetadata.id !== undefined) {
      this.subscribeToSaveResponse(this.contractMetadataService.update(contractMetadata));
    } else {
      this.subscribeToSaveResponse(this.contractMetadataService.create(contractMetadata));
    }
  }

  trackContractMetadataById(index: number, item: IContractMetadata): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackSecurityClearanceById(index: number, item: ISecurityClearance): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  getSelectedContractMetadata(option: IContractMetadata, selectedVals?: IContractMetadata[]): IContractMetadata {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedApplicationUser(option: IApplicationUser, selectedVals?: IApplicationUser[]): IApplicationUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedBusinessDocument(option: IBusinessDocument, selectedVals?: IBusinessDocument[]): IBusinessDocument {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractMetadata>>): void {
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

  protected updateForm(contractMetadata: IContractMetadata): void {
    this.editForm.patchValue({
      id: contractMetadata.id,
      description: contractMetadata.description,
      typeOfContract: contractMetadata.typeOfContract,
      contractStatus: contractMetadata.contractStatus,
      startDate: contractMetadata.startDate,
      terminationDate: contractMetadata.terminationDate,
      commentsAndAttachment: contractMetadata.commentsAndAttachment,
      contractTitle: contractMetadata.contractTitle,
      contractIdentifier: contractMetadata.contractIdentifier,
      contractIdentifierShort: contractMetadata.contractIdentifierShort,
      relatedContracts: contractMetadata.relatedContracts,
      department: contractMetadata.department,
      contractPartner: contractMetadata.contractPartner,
      responsiblePerson: contractMetadata.responsiblePerson,
      signatories: contractMetadata.signatories,
      securityClearance: contractMetadata.securityClearance,
      placeholders: contractMetadata.placeholders,
      contractDocumentFiles: contractMetadata.contractDocumentFiles,
      contractMappings: contractMetadata.contractMappings,
    });

    this.contractMetadataSharedCollection = this.contractMetadataService.addContractMetadataToCollectionIfMissing(
      this.contractMetadataSharedCollection,
      ...(contractMetadata.relatedContracts ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      contractMetadata.department,
      contractMetadata.contractPartner
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      contractMetadata.responsiblePerson,
      ...(contractMetadata.signatories ?? [])
    );
    this.securityClearancesSharedCollection = this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
      this.securityClearancesSharedCollection,
      contractMetadata.securityClearance
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(contractMetadata.placeholders ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(contractMetadata.contractDocumentFiles ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(contractMetadata.contractMappings ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractMetadataService
      .query()
      .pipe(map((res: HttpResponse<IContractMetadata[]>) => res.body ?? []))
      .pipe(
        map((contractMetadata: IContractMetadata[]) =>
          this.contractMetadataService.addContractMetadataToCollectionIfMissing(
            contractMetadata,
            ...(this.editForm.get('relatedContracts')!.value ?? [])
          )
        )
      )
      .subscribe((contractMetadata: IContractMetadata[]) => (this.contractMetadataSharedCollection = contractMetadata));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            this.editForm.get('department')!.value,
            this.editForm.get('contractPartner')!.value
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(
            applicationUsers,
            this.editForm.get('responsiblePerson')!.value,
            ...(this.editForm.get('signatories')!.value ?? [])
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

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

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            ...(this.editForm.get('contractDocumentFiles')!.value ?? [])
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('contractMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );
  }

  protected createFromForm(): IContractMetadata {
    return {
      ...new ContractMetadata(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      typeOfContract: this.editForm.get(['typeOfContract'])!.value,
      contractStatus: this.editForm.get(['contractStatus'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      terminationDate: this.editForm.get(['terminationDate'])!.value,
      commentsAndAttachment: this.editForm.get(['commentsAndAttachment'])!.value,
      contractTitle: this.editForm.get(['contractTitle'])!.value,
      contractIdentifier: this.editForm.get(['contractIdentifier'])!.value,
      contractIdentifierShort: this.editForm.get(['contractIdentifierShort'])!.value,
      relatedContracts: this.editForm.get(['relatedContracts'])!.value,
      department: this.editForm.get(['department'])!.value,
      contractPartner: this.editForm.get(['contractPartner'])!.value,
      responsiblePerson: this.editForm.get(['responsiblePerson'])!.value,
      signatories: this.editForm.get(['signatories'])!.value,
      securityClearance: this.editForm.get(['securityClearance'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      contractDocumentFiles: this.editForm.get(['contractDocumentFiles'])!.value,
      contractMappings: this.editForm.get(['contractMappings'])!.value,
    };
  }
}
