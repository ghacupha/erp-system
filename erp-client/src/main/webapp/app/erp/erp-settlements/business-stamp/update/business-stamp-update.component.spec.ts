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

import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BusinessStampService } from '../service/business-stamp.service';
import { IBusinessStamp, BusinessStamp } from '../business-stamp.model';

import { BusinessStampUpdateComponent } from './business-stamp-update.component';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ErpCommonModule } from '../../../erp-common/erp-common.module';

describe('BusinessStamp Management Update Component', () => {
  let comp: BusinessStampUpdateComponent;
  let fixture: ComponentFixture<BusinessStampUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessStampService: BusinessStampService;
  let dealerService: DealerService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ErpCommonModule, HttpClientTestingModule],
      declarations: [BusinessStampUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BusinessStampUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessStampUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessStampService = TestBed.inject(BusinessStampService);
    dealerService = TestBed.inject(DealerService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dealer query and add missing value', () => {
      const businessStamp: IBusinessStamp = { id: 456 };
      const stampHolder: IDealer = { id: 58917 };
      businessStamp.stampHolder = stampHolder;

      const dealerCollection: IDealer[] = [{ id: 78982 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [stampHolder];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessStamp });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const businessStamp: IBusinessStamp = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 38787 }];
      businessStamp.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 73707 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessStamp });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const businessStamp: IBusinessStamp = { id: 456 };
      const stampHolder: IDealer = { id: 12161 };
      businessStamp.stampHolder = stampHolder;
      const placeholders: IPlaceholder = { id: 20439 };
      businessStamp.placeholders = [placeholders];

      activatedRoute.data = of({ businessStamp });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(businessStamp));
      expect(comp.dealersSharedCollection).toContain(stampHolder);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessStamp>>();
      const businessStamp = { id: 123 };
      jest.spyOn(businessStampService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessStamp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessStamp }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessStampService.update).toHaveBeenCalledWith(businessStamp);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessStamp>>();
      const businessStamp = new BusinessStamp();
      jest.spyOn(businessStampService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessStamp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessStamp }));
      saveSubject.complete();

      // THEN
      expect(businessStampService.create).toHaveBeenCalledWith(businessStamp);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessStamp>>();
      const businessStamp = { id: 123 };
      jest.spyOn(businessStampService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessStamp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessStampService.update).toHaveBeenCalledWith(businessStamp);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
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
  });

  describe('Getting selected relationships', () => {
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
