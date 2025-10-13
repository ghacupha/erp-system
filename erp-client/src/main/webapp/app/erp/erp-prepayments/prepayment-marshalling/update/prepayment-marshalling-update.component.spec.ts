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

import { PrepaymentAccountService } from '../../prepayment-account/service/prepayment-account.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrepaymentMarshallingService } from '../service/prepayment-marshalling.service';
import { IPrepaymentMarshalling, PrepaymentMarshalling } from '../prepayment-marshalling.model';

import { PrepaymentMarshallingUpdateComponent } from './prepayment-marshalling-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IPrepaymentAccount } from '../../prepayment-account/prepayment-account.model';

describe('PrepaymentMarshalling Management Update Component', () => {
  let comp: PrepaymentMarshallingUpdateComponent;
  let fixture: ComponentFixture<PrepaymentMarshallingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prepaymentMarshallingService: PrepaymentMarshallingService;
  let prepaymentAccountService: PrepaymentAccountService;
  let placeholderService: PlaceholderService;
  let fiscalMonthService: FiscalMonthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrepaymentMarshallingUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PrepaymentMarshallingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrepaymentMarshallingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prepaymentMarshallingService = TestBed.inject(PrepaymentMarshallingService);
    prepaymentAccountService = TestBed.inject(PrepaymentAccountService);
    placeholderService = TestBed.inject(PlaceholderService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PrepaymentAccount query and add missing value', () => {
      const prepaymentMarshalling: IPrepaymentMarshalling = { id: 456 };
      const prepaymentAccount: IPrepaymentAccount = { id: 27557 };
      prepaymentMarshalling.prepaymentAccount = prepaymentAccount;

      const prepaymentAccountCollection: IPrepaymentAccount[] = [{ id: 67420 }];
      jest.spyOn(prepaymentAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: prepaymentAccountCollection })));
      const additionalPrepaymentAccounts = [prepaymentAccount];
      const expectedCollection: IPrepaymentAccount[] = [...additionalPrepaymentAccounts, ...prepaymentAccountCollection];
      jest.spyOn(prepaymentAccountService, 'addPrepaymentAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentMarshalling });
      comp.ngOnInit();

      expect(prepaymentAccountService.query).toHaveBeenCalled();
      expect(prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing).toHaveBeenCalledWith(
        prepaymentAccountCollection,
        ...additionalPrepaymentAccounts
      );
      expect(comp.prepaymentAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const prepaymentMarshalling: IPrepaymentMarshalling = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 39118 }];
      prepaymentMarshalling.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 64027 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentMarshalling });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalMonth query and add missing value', () => {
      const prepaymentMarshalling: IPrepaymentMarshalling = { id: 456 };
      const firstFiscalMonth: IFiscalMonth = { id: 40032 };
      prepaymentMarshalling.firstFiscalMonth = firstFiscalMonth;
      const lastFiscalMonth: IFiscalMonth = { id: 4790 };
      prepaymentMarshalling.lastFiscalMonth = lastFiscalMonth;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 94111 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [firstFiscalMonth, lastFiscalMonth];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentMarshalling });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, ...additionalFiscalMonths);
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prepaymentMarshalling: IPrepaymentMarshalling = { id: 456 };
      const prepaymentAccount: IPrepaymentAccount = { id: 39745 };
      prepaymentMarshalling.prepaymentAccount = prepaymentAccount;
      const placeholders: IPlaceholder = { id: 72390 };
      prepaymentMarshalling.placeholders = [placeholders];
      const firstFiscalMonth: IFiscalMonth = { id: 2653 };
      prepaymentMarshalling.firstFiscalMonth = firstFiscalMonth;
      const lastFiscalMonth: IFiscalMonth = { id: 50711 };
      prepaymentMarshalling.lastFiscalMonth = lastFiscalMonth;

      activatedRoute.data = of({ prepaymentMarshalling });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prepaymentMarshalling));
      expect(comp.prepaymentAccountsSharedCollection).toContain(prepaymentAccount);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.fiscalMonthsSharedCollection).toContain(firstFiscalMonth);
      expect(comp.fiscalMonthsSharedCollection).toContain(lastFiscalMonth);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentMarshalling>>();
      const prepaymentMarshalling = { id: 123 };
      jest.spyOn(prepaymentMarshallingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentMarshalling });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentMarshalling }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(prepaymentMarshallingService.update).toHaveBeenCalledWith(prepaymentMarshalling);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentMarshalling>>();
      const prepaymentMarshalling = new PrepaymentMarshalling();
      jest.spyOn(prepaymentMarshallingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentMarshalling });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentMarshalling }));
      saveSubject.complete();

      // THEN
      expect(prepaymentMarshallingService.create).toHaveBeenCalledWith(prepaymentMarshalling);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentMarshalling>>();
      const prepaymentMarshalling = { id: 123 };
      jest.spyOn(prepaymentMarshallingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentMarshalling });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prepaymentMarshallingService.update).toHaveBeenCalledWith(prepaymentMarshalling);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPrepaymentAccountById', () => {
      it('Should return tracked PrepaymentAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrepaymentAccountById(0, entity);
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

    describe('trackFiscalMonthById', () => {
      it('Should return tracked FiscalMonth primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalMonthById(0, entity);
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
