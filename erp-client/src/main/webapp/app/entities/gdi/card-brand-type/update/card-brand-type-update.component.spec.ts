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

import { CardBrandTypeService } from '../service/card-brand-type.service';
import { ICardBrandType, CardBrandType } from '../card-brand-type.model';

import { CardBrandTypeUpdateComponent } from './card-brand-type-update.component';

describe('CardBrandType Management Update Component', () => {
  let comp: CardBrandTypeUpdateComponent;
  let fixture: ComponentFixture<CardBrandTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardBrandTypeService: CardBrandTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardBrandTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardBrandTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardBrandTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardBrandTypeService = TestBed.inject(CardBrandTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardBrandType: ICardBrandType = { id: 456 };

      activatedRoute.data = of({ cardBrandType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardBrandType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardBrandType>>();
      const cardBrandType = { id: 123 };
      jest.spyOn(cardBrandTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardBrandType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardBrandType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardBrandTypeService.update).toHaveBeenCalledWith(cardBrandType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardBrandType>>();
      const cardBrandType = new CardBrandType();
      jest.spyOn(cardBrandTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardBrandType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardBrandType }));
      saveSubject.complete();

      // THEN
      expect(cardBrandTypeService.create).toHaveBeenCalledWith(cardBrandType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardBrandType>>();
      const cardBrandType = { id: 123 };
      jest.spyOn(cardBrandTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardBrandType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardBrandTypeService.update).toHaveBeenCalledWith(cardBrandType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
