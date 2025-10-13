///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContractMetadataService } from '../service/contract-metadata.service';
import { IContractMetadata, ContractMetadata } from '../contract-metadata.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { ISecurityClearance } from 'app/entities/people/security-clearance/security-clearance.model';
import { SecurityClearanceService } from 'app/entities/people/security-clearance/service/security-clearance.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';

import { ContractMetadataUpdateComponent } from './contract-metadata-update.component';

describe('ContractMetadata Management Update Component', () => {
  let comp: ContractMetadataUpdateComponent;
  let fixture: ComponentFixture<ContractMetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractMetadataService: ContractMetadataService;
  let dealerService: DealerService;
  let applicationUserService: ApplicationUserService;
  let securityClearanceService: SecurityClearanceService;
  let placeholderService: PlaceholderService;
  let businessDocumentService: BusinessDocumentService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContractMetadataUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ContractMetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractMetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractMetadataService = TestBed.inject(ContractMetadataService);
    dealerService = TestBed.inject(DealerService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);
    placeholderService = TestBed.inject(PlaceholderService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ContractMetadata query and add missing value', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const relatedContracts: IContractMetadata[] = [{ id: 72260 }];
      contractMetadata.relatedContracts = relatedContracts;

      const contractMetadataCollection: IContractMetadata[] = [{ id: 78111 }];
      jest.spyOn(contractMetadataService, 'query').mockReturnValue(of(new HttpResponse({ body: contractMetadataCollection })));
      const additionalContractMetadata = [...relatedContracts];
      const expectedCollection: IContractMetadata[] = [...additionalContractMetadata, ...contractMetadataCollection];
      jest.spyOn(contractMetadataService, 'addContractMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(contractMetadataService.query).toHaveBeenCalled();
      expect(contractMetadataService.addContractMetadataToCollectionIfMissing).toHaveBeenCalledWith(
        contractMetadataCollection,
        ...additionalContractMetadata
      );
      expect(comp.contractMetadataSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const department: IDealer = { id: 69263 };
      contractMetadata.department = department;
      const contractPartner: IDealer = { id: 43356 };
      contractMetadata.contractPartner = contractPartner;

      const dealerCollection: IDealer[] = [{ id: 5737 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [department, contractPartner];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const responsiblePerson: IApplicationUser = { id: 78737 };
      contractMetadata.responsiblePerson = responsiblePerson;
      const signatories: IApplicationUser[] = [{ id: 73651 }];
      contractMetadata.signatories = signatories;

      const applicationUserCollection: IApplicationUser[] = [{ id: 80062 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [responsiblePerson, ...signatories];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityClearance query and add missing value', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const securityClearance: ISecurityClearance = { id: 96472 };
      contractMetadata.securityClearance = securityClearance;

      const securityClearanceCollection: ISecurityClearance[] = [{ id: 43995 }];
      jest.spyOn(securityClearanceService, 'query').mockReturnValue(of(new HttpResponse({ body: securityClearanceCollection })));
      const additionalSecurityClearances = [securityClearance];
      const expectedCollection: ISecurityClearance[] = [...additionalSecurityClearances, ...securityClearanceCollection];
      jest.spyOn(securityClearanceService, 'addSecurityClearanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(securityClearanceService.query).toHaveBeenCalled();
      expect(securityClearanceService.addSecurityClearanceToCollectionIfMissing).toHaveBeenCalledWith(
        securityClearanceCollection,
        ...additionalSecurityClearances
      );
      expect(comp.securityClearancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 56582 }];
      contractMetadata.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 72743 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const contractDocumentFiles: IBusinessDocument[] = [{ id: 8473 }];
      contractMetadata.contractDocumentFiles = contractDocumentFiles;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 1239 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [...contractDocumentFiles];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const contractMappings: IUniversallyUniqueMapping[] = [{ id: 52979 }];
      contractMetadata.contractMappings = contractMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 54091 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...contractMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contractMetadata: IContractMetadata = { id: 456 };
      const relatedContracts: IContractMetadata = { id: 36190 };
      contractMetadata.relatedContracts = [relatedContracts];
      const department: IDealer = { id: 30586 };
      contractMetadata.department = department;
      const contractPartner: IDealer = { id: 14250 };
      contractMetadata.contractPartner = contractPartner;
      const responsiblePerson: IApplicationUser = { id: 23138 };
      contractMetadata.responsiblePerson = responsiblePerson;
      const signatories: IApplicationUser = { id: 93456 };
      contractMetadata.signatories = [signatories];
      const securityClearance: ISecurityClearance = { id: 77196 };
      contractMetadata.securityClearance = securityClearance;
      const placeholders: IPlaceholder = { id: 57236 };
      contractMetadata.placeholders = [placeholders];
      const contractDocumentFiles: IBusinessDocument = { id: 80794 };
      contractMetadata.contractDocumentFiles = [contractDocumentFiles];
      const contractMappings: IUniversallyUniqueMapping = { id: 3275 };
      contractMetadata.contractMappings = [contractMappings];

      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contractMetadata));
      expect(comp.contractMetadataSharedCollection).toContain(relatedContracts);
      expect(comp.dealersSharedCollection).toContain(department);
      expect(comp.dealersSharedCollection).toContain(contractPartner);
      expect(comp.applicationUsersSharedCollection).toContain(responsiblePerson);
      expect(comp.applicationUsersSharedCollection).toContain(signatories);
      expect(comp.securityClearancesSharedCollection).toContain(securityClearance);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.businessDocumentsSharedCollection).toContain(contractDocumentFiles);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(contractMappings);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContractMetadata>>();
      const contractMetadata = { id: 123 };
      jest.spyOn(contractMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractMetadata }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractMetadataService.update).toHaveBeenCalledWith(contractMetadata);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContractMetadata>>();
      const contractMetadata = new ContractMetadata();
      jest.spyOn(contractMetadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractMetadata }));
      saveSubject.complete();

      // THEN
      expect(contractMetadataService.create).toHaveBeenCalledWith(contractMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContractMetadata>>();
      const contractMetadata = { id: 123 };
      jest.spyOn(contractMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractMetadataService.update).toHaveBeenCalledWith(contractMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContractMetadataById', () => {
      it('Should return tracked ContractMetadata primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContractMetadataById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSecurityClearanceById', () => {
      it('Should return tracked SecurityClearance primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityClearanceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBusinessDocumentById', () => {
      it('Should return tracked BusinessDocument primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessDocumentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedContractMetadata', () => {
      it('Should return option if no ContractMetadata is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedContractMetadata(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected ContractMetadata for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedContractMetadata(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this ContractMetadata is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedContractMetadata(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedApplicationUser', () => {
      it('Should return option if no ApplicationUser is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedApplicationUser(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected ApplicationUser for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedApplicationUser(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this ApplicationUser is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedApplicationUser(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPlaceholder', () => {
      it('Should return option if no Placeholder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlaceholder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Placeholder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Placeholder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedBusinessDocument', () => {
      it('Should return option if no BusinessDocument is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBusinessDocument(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BusinessDocument for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BusinessDocument is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
