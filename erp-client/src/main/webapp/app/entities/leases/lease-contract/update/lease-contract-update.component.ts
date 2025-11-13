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

import { ILeaseContract, LeaseContract } from '../lease-contract.model';
import { LeaseContractService } from '../service/lease-contract.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';
import { IContractMetadata } from 'app/entities/contract/contract-metadata/contract-metadata.model';
import { ContractMetadataService } from 'app/entities/contract/contract-metadata/service/contract-metadata.service';

@Component({
  selector: 'jhi-lease-contract-update',
  templateUrl: './lease-contract-update.component.html',
})
export class LeaseContractUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  contractMetadataSharedCollection: IContractMetadata[] = [];

  editForm = this.fb.group({
    id: [],
    bookingId: [null, [Validators.required]],
    leaseTitle: [null, [Validators.required]],
    identifier: [null, [Validators.required]],
    description: [],
    commencementDate: [null, [Validators.required]],
    terminalDate: [null, [Validators.required]],
    placeholders: [],
    systemMappings: [],
    businessDocuments: [],
    contractMetadata: [],
  });

  constructor(
    protected leaseContractService: LeaseContractService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected businessDocumentService: BusinessDocumentService,
    protected contractMetadataService: ContractMetadataService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseContract }) => {
      this.updateForm(leaseContract);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseContract = this.createFromForm();
    if (leaseContract.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseContractService.update(leaseContract));
    } else {
      this.subscribeToSaveResponse(this.leaseContractService.create(leaseContract));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackContractMetadataById(index: number, item: IContractMetadata): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseContract>>): void {
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

  protected updateForm(leaseContract: ILeaseContract): void {
    this.editForm.patchValue({
      id: leaseContract.id,
      bookingId: leaseContract.bookingId,
      leaseTitle: leaseContract.leaseTitle,
      identifier: leaseContract.identifier,
      description: leaseContract.description,
      commencementDate: leaseContract.commencementDate,
      terminalDate: leaseContract.terminalDate,
      placeholders: leaseContract.placeholders,
      systemMappings: leaseContract.systemMappings,
      businessDocuments: leaseContract.businessDocuments,
      contractMetadata: leaseContract.contractMetadata,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(leaseContract.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(leaseContract.systemMappings ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(leaseContract.businessDocuments ?? [])
    );
    this.contractMetadataSharedCollection = this.contractMetadataService.addContractMetadataToCollectionIfMissing(
      this.contractMetadataSharedCollection,
      ...(leaseContract.contractMetadata ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('systemMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            ...(this.editForm.get('businessDocuments')!.value ?? [])
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));

    this.contractMetadataService
      .query()
      .pipe(map((res: HttpResponse<IContractMetadata[]>) => res.body ?? []))
      .pipe(
        map((contractMetadata: IContractMetadata[]) =>
          this.contractMetadataService.addContractMetadataToCollectionIfMissing(
            contractMetadata,
            ...(this.editForm.get('contractMetadata')!.value ?? [])
          )
        )
      )
      .subscribe((contractMetadata: IContractMetadata[]) => (this.contractMetadataSharedCollection = contractMetadata));
  }

  protected createFromForm(): ILeaseContract {
    return {
      ...new LeaseContract(),
      id: this.editForm.get(['id'])!.value,
      bookingId: this.editForm.get(['bookingId'])!.value,
      leaseTitle: this.editForm.get(['leaseTitle'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      description: this.editForm.get(['description'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      terminalDate: this.editForm.get(['terminalDate'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      systemMappings: this.editForm.get(['systemMappings'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      contractMetadata: this.editForm.get(['contractMetadata'])!.value,
    };
  }
}
