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

import { PerformanceOfForeignSubsidiariesService } from '../service/performance-of-foreign-subsidiaries.service';
import { IPerformanceOfForeignSubsidiaries, PerformanceOfForeignSubsidiaries } from '../performance-of-foreign-subsidiaries.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IIsoCountryCode } from 'app/entities/gdi/iso-country-code/iso-country-code.model';
import { IsoCountryCodeService } from 'app/entities/gdi/iso-country-code/service/iso-country-code.service';

import { PerformanceOfForeignSubsidiariesUpdateComponent } from './performance-of-foreign-subsidiaries-update.component';

describe('PerformanceOfForeignSubsidiaries Management Update Component', () => {
  let comp: PerformanceOfForeignSubsidiariesUpdateComponent;
  let fixture: ComponentFixture<PerformanceOfForeignSubsidiariesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let performanceOfForeignSubsidiariesService: PerformanceOfForeignSubsidiariesService;
  let institutionCodeService: InstitutionCodeService;
  let isoCountryCodeService: IsoCountryCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PerformanceOfForeignSubsidiariesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PerformanceOfForeignSubsidiariesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerformanceOfForeignSubsidiariesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    performanceOfForeignSubsidiariesService = TestBed.inject(PerformanceOfForeignSubsidiariesService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    isoCountryCodeService = TestBed.inject(IsoCountryCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 456 };
      const bankCode: IInstitutionCode = { id: 9990 };
      performanceOfForeignSubsidiaries.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 82069 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ performanceOfForeignSubsidiaries });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IsoCountryCode query and add missing value', () => {
      const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 456 };
      const subsidiaryCountryCode: IIsoCountryCode = { id: 12663 };
      performanceOfForeignSubsidiaries.subsidiaryCountryCode = subsidiaryCountryCode;

      const isoCountryCodeCollection: IIsoCountryCode[] = [{ id: 73079 }];
      jest.spyOn(isoCountryCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: isoCountryCodeCollection })));
      const additionalIsoCountryCodes = [subsidiaryCountryCode];
      const expectedCollection: IIsoCountryCode[] = [...additionalIsoCountryCodes, ...isoCountryCodeCollection];
      jest.spyOn(isoCountryCodeService, 'addIsoCountryCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ performanceOfForeignSubsidiaries });
      comp.ngOnInit();

      expect(isoCountryCodeService.query).toHaveBeenCalled();
      expect(isoCountryCodeService.addIsoCountryCodeToCollectionIfMissing).toHaveBeenCalledWith(
        isoCountryCodeCollection,
        ...additionalIsoCountryCodes
      );
      expect(comp.isoCountryCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 456 };
      const bankCode: IInstitutionCode = { id: 32656 };
      performanceOfForeignSubsidiaries.bankCode = bankCode;
      const subsidiaryCountryCode: IIsoCountryCode = { id: 48268 };
      performanceOfForeignSubsidiaries.subsidiaryCountryCode = subsidiaryCountryCode;

      activatedRoute.data = of({ performanceOfForeignSubsidiaries });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(performanceOfForeignSubsidiaries));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.isoCountryCodesSharedCollection).toContain(subsidiaryCountryCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PerformanceOfForeignSubsidiaries>>();
      const performanceOfForeignSubsidiaries = { id: 123 };
      jest.spyOn(performanceOfForeignSubsidiariesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ performanceOfForeignSubsidiaries });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: performanceOfForeignSubsidiaries }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(performanceOfForeignSubsidiariesService.update).toHaveBeenCalledWith(performanceOfForeignSubsidiaries);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PerformanceOfForeignSubsidiaries>>();
      const performanceOfForeignSubsidiaries = new PerformanceOfForeignSubsidiaries();
      jest.spyOn(performanceOfForeignSubsidiariesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ performanceOfForeignSubsidiaries });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: performanceOfForeignSubsidiaries }));
      saveSubject.complete();

      // THEN
      expect(performanceOfForeignSubsidiariesService.create).toHaveBeenCalledWith(performanceOfForeignSubsidiaries);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PerformanceOfForeignSubsidiaries>>();
      const performanceOfForeignSubsidiaries = { id: 123 };
      jest.spyOn(performanceOfForeignSubsidiariesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ performanceOfForeignSubsidiaries });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(performanceOfForeignSubsidiariesService.update).toHaveBeenCalledWith(performanceOfForeignSubsidiaries);
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

    describe('trackIsoCountryCodeById', () => {
      it('Should return tracked IsoCountryCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIsoCountryCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
