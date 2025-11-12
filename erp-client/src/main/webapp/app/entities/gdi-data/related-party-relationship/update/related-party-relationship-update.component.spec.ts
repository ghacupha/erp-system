///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { RelatedPartyRelationshipService } from '../service/related-party-relationship.service';
import { IRelatedPartyRelationship, RelatedPartyRelationship } from '../related-party-relationship.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IPartyRelationType } from 'app/entities/gdi/party-relation-type/party-relation-type.model';
import { PartyRelationTypeService } from 'app/entities/gdi/party-relation-type/service/party-relation-type.service';

import { RelatedPartyRelationshipUpdateComponent } from './related-party-relationship-update.component';

describe('RelatedPartyRelationship Management Update Component', () => {
  let comp: RelatedPartyRelationshipUpdateComponent;
  let fixture: ComponentFixture<RelatedPartyRelationshipUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let relatedPartyRelationshipService: RelatedPartyRelationshipService;
  let institutionCodeService: InstitutionCodeService;
  let bankBranchCodeService: BankBranchCodeService;
  let partyRelationTypeService: PartyRelationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RelatedPartyRelationshipUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RelatedPartyRelationshipUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RelatedPartyRelationshipUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    relatedPartyRelationshipService = TestBed.inject(RelatedPartyRelationshipService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);
    partyRelationTypeService = TestBed.inject(PartyRelationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const relatedPartyRelationship: IRelatedPartyRelationship = { id: 456 };
      const bankCode: IInstitutionCode = { id: 12571 };
      relatedPartyRelationship.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 25031 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ relatedPartyRelationship });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const relatedPartyRelationship: IRelatedPartyRelationship = { id: 456 };
      const branchId: IBankBranchCode = { id: 52868 };
      relatedPartyRelationship.branchId = branchId;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 29140 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [branchId];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ relatedPartyRelationship });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PartyRelationType query and add missing value', () => {
      const relatedPartyRelationship: IRelatedPartyRelationship = { id: 456 };
      const relationshipType: IPartyRelationType = { id: 5387 };
      relatedPartyRelationship.relationshipType = relationshipType;

      const partyRelationTypeCollection: IPartyRelationType[] = [{ id: 22110 }];
      jest.spyOn(partyRelationTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: partyRelationTypeCollection })));
      const additionalPartyRelationTypes = [relationshipType];
      const expectedCollection: IPartyRelationType[] = [...additionalPartyRelationTypes, ...partyRelationTypeCollection];
      jest.spyOn(partyRelationTypeService, 'addPartyRelationTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ relatedPartyRelationship });
      comp.ngOnInit();

      expect(partyRelationTypeService.query).toHaveBeenCalled();
      expect(partyRelationTypeService.addPartyRelationTypeToCollectionIfMissing).toHaveBeenCalledWith(
        partyRelationTypeCollection,
        ...additionalPartyRelationTypes
      );
      expect(comp.partyRelationTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const relatedPartyRelationship: IRelatedPartyRelationship = { id: 456 };
      const bankCode: IInstitutionCode = { id: 36106 };
      relatedPartyRelationship.bankCode = bankCode;
      const branchId: IBankBranchCode = { id: 21489 };
      relatedPartyRelationship.branchId = branchId;
      const relationshipType: IPartyRelationType = { id: 59609 };
      relatedPartyRelationship.relationshipType = relationshipType;

      activatedRoute.data = of({ relatedPartyRelationship });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(relatedPartyRelationship));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.bankBranchCodesSharedCollection).toContain(branchId);
      expect(comp.partyRelationTypesSharedCollection).toContain(relationshipType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RelatedPartyRelationship>>();
      const relatedPartyRelationship = { id: 123 };
      jest.spyOn(relatedPartyRelationshipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatedPartyRelationship });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: relatedPartyRelationship }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(relatedPartyRelationshipService.update).toHaveBeenCalledWith(relatedPartyRelationship);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RelatedPartyRelationship>>();
      const relatedPartyRelationship = new RelatedPartyRelationship();
      jest.spyOn(relatedPartyRelationshipService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatedPartyRelationship });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: relatedPartyRelationship }));
      saveSubject.complete();

      // THEN
      expect(relatedPartyRelationshipService.create).toHaveBeenCalledWith(relatedPartyRelationship);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RelatedPartyRelationship>>();
      const relatedPartyRelationship = { id: 123 };
      jest.spyOn(relatedPartyRelationshipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatedPartyRelationship });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(relatedPartyRelationshipService.update).toHaveBeenCalledWith(relatedPartyRelationship);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInstitutionCodeById', () => {
      it('Should return tracked InstitutionCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInstitutionCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBankBranchCodeById', () => {
      it('Should return tracked BankBranchCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBankBranchCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPartyRelationTypeById', () => {
      it('Should return tracked PartyRelationType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPartyRelationTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
