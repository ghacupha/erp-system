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

import { LoanRestructureItemService } from '../service/loan-restructure-item.service';
import { ILoanRestructureItem, LoanRestructureItem } from '../loan-restructure-item.model';

import { LoanRestructureItemUpdateComponent } from './loan-restructure-item-update.component';

describe('LoanRestructureItem Management Update Component', () => {
  let comp: LoanRestructureItemUpdateComponent;
  let fixture: ComponentFixture<LoanRestructureItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanRestructureItemService: LoanRestructureItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanRestructureItemUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanRestructureItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanRestructureItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanRestructureItemService = TestBed.inject(LoanRestructureItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanRestructureItem: ILoanRestructureItem = { id: 456 };

      activatedRoute.data = of({ loanRestructureItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanRestructureItem));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRestructureItem>>();
      const loanRestructureItem = { id: 123 };
      jest.spyOn(loanRestructureItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRestructureItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanRestructureItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanRestructureItemService.update).toHaveBeenCalledWith(loanRestructureItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRestructureItem>>();
      const loanRestructureItem = new LoanRestructureItem();
      jest.spyOn(loanRestructureItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRestructureItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanRestructureItem }));
      saveSubject.complete();

      // THEN
      expect(loanRestructureItemService.create).toHaveBeenCalledWith(loanRestructureItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRestructureItem>>();
      const loanRestructureItem = { id: 123 };
      jest.spyOn(loanRestructureItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRestructureItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanRestructureItemService.update).toHaveBeenCalledWith(loanRestructureItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
