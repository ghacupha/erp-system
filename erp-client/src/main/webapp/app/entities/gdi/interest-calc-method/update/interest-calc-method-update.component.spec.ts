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

import { InterestCalcMethodService } from '../service/interest-calc-method.service';
import { IInterestCalcMethod, InterestCalcMethod } from '../interest-calc-method.model';

import { InterestCalcMethodUpdateComponent } from './interest-calc-method-update.component';

describe('InterestCalcMethod Management Update Component', () => {
  let comp: InterestCalcMethodUpdateComponent;
  let fixture: ComponentFixture<InterestCalcMethodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interestCalcMethodService: InterestCalcMethodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InterestCalcMethodUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(InterestCalcMethodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterestCalcMethodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interestCalcMethodService = TestBed.inject(InterestCalcMethodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const interestCalcMethod: IInterestCalcMethod = { id: 456 };

      activatedRoute.data = of({ interestCalcMethod });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(interestCalcMethod));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InterestCalcMethod>>();
      const interestCalcMethod = { id: 123 };
      jest.spyOn(interestCalcMethodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interestCalcMethod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interestCalcMethod }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(interestCalcMethodService.update).toHaveBeenCalledWith(interestCalcMethod);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InterestCalcMethod>>();
      const interestCalcMethod = new InterestCalcMethod();
      jest.spyOn(interestCalcMethodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interestCalcMethod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interestCalcMethod }));
      saveSubject.complete();

      // THEN
      expect(interestCalcMethodService.create).toHaveBeenCalledWith(interestCalcMethod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InterestCalcMethod>>();
      const interestCalcMethod = { id: 123 };
      jest.spyOn(interestCalcMethodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interestCalcMethod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interestCalcMethodService.update).toHaveBeenCalledWith(interestCalcMethod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
