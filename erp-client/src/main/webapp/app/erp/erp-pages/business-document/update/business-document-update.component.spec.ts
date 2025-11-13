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

import { SecurityClearanceService } from '../../security-clearance/service/security-clearance.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BusinessDocumentService } from '../service/business-document.service';
import { IBusinessDocument, BusinessDocument } from '../business-document.model';

import { BusinessDocumentUpdateComponent } from './business-document-update.component';
import { IPlaceholder } from '../../placeholder/placeholder.model';
import { IAlgorithm } from '../../algorithm/algorithm.model';
import { DealerService } from '../../dealers/dealer/service/dealer.service';
import { UniversallyUniqueMappingService } from '../../universally-unique-mapping/service/universally-unique-mapping.service';
import { IApplicationUser } from '../../application-user/application-user.model';
import { ISecurityClearance } from '../../security-clearance/security-clearance.model';
import { ApplicationUserService } from '../../application-user/service/application-user.service';
import { AlgorithmService } from '../../algorithm/service/algorithm.service';
import { PlaceholderService } from '../../placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from '../../universally-unique-mapping/universally-unique-mapping.model';
import { IDealer } from '../../dealers/dealer/dealer.model';

describe('BusinessDocument Management Update Component', () => {
  let comp: BusinessDocumentUpdateComponent;
  let fixture: ComponentFixture<BusinessDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessDocumentService: BusinessDocumentService;
  let applicationUserService: ApplicationUserService;
  let dealerService: DealerService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let placeholderService: PlaceholderService;
  let algorithmService: AlgorithmService;
  let securityClearanceService: SecurityClearanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusinessDocumentUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BusinessDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessDocumentService = TestBed.inject(BusinessDocumentService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    dealerService = TestBed.inject(DealerService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    placeholderService = TestBed.inject(PlaceholderService);
    algorithmService = TestBed.inject(AlgorithmService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const businessDocument: IBusinessDocument = { id: 456 };
      const createdBy: IApplicationUser = { id: 6701 };
      businessDocument.createdBy = createdBy;
      const lastModifiedBy: IApplicationUser = { id: 67476 };
      businessDocument.lastModifiedBy = lastModifiedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 394 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [createdBy, lastModifiedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const businessDocument: IBusinessDocument = { id: 456 };
      const originatingDepartment: IDealer = { id: 24808 };
      businessDocument.originatingDepartment = originatingDepartment;

      const dealerCollection: IDealer[] = [{ id: 74683 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [originatingDepartment];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const businessDocument: IBusinessDocument = { id: 456 };
      const applicationMappings: IUniversallyUniqueMapping[] = [{ id: 27789 }];
      businessDocument.applicationMappings = applicationMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 92595 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...applicationMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const businessDocument: IBusinessDocument = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 77768 }];
      businessDocument.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 54072 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Algorithm query and add missing value', () => {
      const businessDocument: IBusinessDocument = { id: 456 };
      const fileChecksumAlgorithm: IAlgorithm = { id: 84406 };
      businessDocument.fileChecksumAlgorithm = fileChecksumAlgorithm;

      const algorithmCollection: IAlgorithm[] = [{ id: 66206 }];
      jest.spyOn(algorithmService, 'query').mockReturnValue(of(new HttpResponse({ body: algorithmCollection })));
      const additionalAlgorithms = [fileChecksumAlgorithm];
      const expectedCollection: IAlgorithm[] = [...additionalAlgorithms, ...algorithmCollection];
      jest.spyOn(algorithmService, 'addAlgorithmToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      expect(algorithmService.query).toHaveBeenCalled();
      expect(algorithmService.addAlgorithmToCollectionIfMissing).toHaveBeenCalledWith(algorithmCollection, ...additionalAlgorithms);
      expect(comp.algorithmsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityClearance query and add missing value', () => {
      const businessDocument: IBusinessDocument = { id: 456 };
      const securityClearance: ISecurityClearance = { id: 58295 };
      businessDocument.securityClearance = securityClearance;

      const securityClearanceCollection: ISecurityClearance[] = [{ id: 20851 }];
      jest.spyOn(securityClearanceService, 'query').mockReturnValue(of(new HttpResponse({ body: securityClearanceCollection })));
      const additionalSecurityClearances = [securityClearance];
      const expectedCollection: ISecurityClearance[] = [...additionalSecurityClearances, ...securityClearanceCollection];
      jest.spyOn(securityClearanceService, 'addSecurityClearanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      expect(securityClearanceService.query).toHaveBeenCalled();
      expect(securityClearanceService.addSecurityClearanceToCollectionIfMissing).toHaveBeenCalledWith(
        securityClearanceCollection,
        ...additionalSecurityClearances
      );
      expect(comp.securityClearancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const businessDocument: IBusinessDocument = { id: 456 };
      const createdBy: IApplicationUser = { id: 13811 };
      businessDocument.createdBy = createdBy;
      const lastModifiedBy: IApplicationUser = { id: 41032 };
      businessDocument.lastModifiedBy = lastModifiedBy;
      const originatingDepartment: IDealer = { id: 4612 };
      businessDocument.originatingDepartment = originatingDepartment;
      const applicationMappings: IUniversallyUniqueMapping = { id: 93664 };
      businessDocument.applicationMappings = [applicationMappings];
      const placeholders: IPlaceholder = { id: 51999 };
      businessDocument.placeholders = [placeholders];
      const fileChecksumAlgorithm: IAlgorithm = { id: 65209 };
      businessDocument.fileChecksumAlgorithm = fileChecksumAlgorithm;
      const securityClearance: ISecurityClearance = { id: 95179 };
      businessDocument.securityClearance = securityClearance;

      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      // todo expect(comp.editForm.value).toEqual(expect.objectContaining(businessDocument));
      // todo expect(comp.applicationUsersSharedCollection).toContain(createdBy);
      // todo expect(comp.applicationUsersSharedCollection).toContain(lastModifiedBy);
      // todo expect(comp.dealersSharedCollection).toContain(originatingDepartment);
      // todo expect(comp.universallyUniqueMappingsSharedCollection).toContain(applicationMappings);
      // todo expect(comp.placeholdersSharedCollection).toContain(placeholders);
      // todo expect(comp.algorithmsSharedCollection).toContain(fileChecksumAlgorithm);
      // todo expect(comp.securityClearancesSharedCollection).toContain(securityClearance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessDocument>>();
      const businessDocument = { id: 123 };
      jest.spyOn(businessDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessDocument }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      // todo expect(businessDocumentService.update).toHaveBeenCalledWith(businessDocument);
      // todo expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessDocument>>();
      const businessDocument = new BusinessDocument();
      jest.spyOn(businessDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessDocument }));
      saveSubject.complete();

      // THEN
      // todo expect(businessDocumentService.create).toHaveBeenCalledWith(businessDocument);
      // todo expect(comp.isSaving).toEqual(false);
      // todo expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessDocument>>();
      const businessDocument = { id: 123 };
      jest.spyOn(businessDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      // todo expect(businessDocumentService.update).toHaveBeenCalledWith(businessDocument);
      // todo expect(comp.isSaving).toEqual(false);
      // todo expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
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

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
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

    describe('trackAlgorithmById', () => {
      it('Should return tracked Algorithm primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAlgorithmById(0, entity);
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
  });

  describe('Getting selected relationships', () => {
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
  });
});
