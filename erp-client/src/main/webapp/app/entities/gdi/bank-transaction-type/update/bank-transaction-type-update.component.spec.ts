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

import { BankTransactionTypeService } from '../service/bank-transaction-type.service';
import { IBankTransactionType, BankTransactionType } from '../bank-transaction-type.model';

import { BankTransactionTypeUpdateComponent } from './bank-transaction-type-update.component';

describe('BankTransactionType Management Update Component', () => {
  let comp: BankTransactionTypeUpdateComponent;
  let fixture: ComponentFixture<BankTransactionTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bankTransactionTypeService: BankTransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BankTransactionTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BankTransactionTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankTransactionTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bankTransactionTypeService = TestBed.inject(BankTransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bankTransactionType: IBankTransactionType = { id: 456 };

      activatedRoute.data = of({ bankTransactionType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bankTransactionType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BankTransactionType>>();
      const bankTransactionType = { id: 123 };
      jest.spyOn(bankTransactionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankTransactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankTransactionType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bankTransactionTypeService.update).toHaveBeenCalledWith(bankTransactionType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BankTransactionType>>();
      const bankTransactionType = new BankTransactionType();
      jest.spyOn(bankTransactionTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankTransactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankTransactionType }));
      saveSubject.complete();

      // THEN
      expect(bankTransactionTypeService.create).toHaveBeenCalledWith(bankTransactionType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BankTransactionType>>();
      const bankTransactionType = { id: 123 };
      jest.spyOn(bankTransactionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankTransactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bankTransactionTypeService.update).toHaveBeenCalledWith(bankTransactionType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
