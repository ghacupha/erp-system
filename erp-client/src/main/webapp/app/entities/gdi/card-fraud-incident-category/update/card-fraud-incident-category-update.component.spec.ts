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

import { CardFraudIncidentCategoryService } from '../service/card-fraud-incident-category.service';
import { ICardFraudIncidentCategory, CardFraudIncidentCategory } from '../card-fraud-incident-category.model';

import { CardFraudIncidentCategoryUpdateComponent } from './card-fraud-incident-category-update.component';

describe('CardFraudIncidentCategory Management Update Component', () => {
  let comp: CardFraudIncidentCategoryUpdateComponent;
  let fixture: ComponentFixture<CardFraudIncidentCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardFraudIncidentCategoryService: CardFraudIncidentCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardFraudIncidentCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardFraudIncidentCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardFraudIncidentCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardFraudIncidentCategoryService = TestBed.inject(CardFraudIncidentCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardFraudIncidentCategory: ICardFraudIncidentCategory = { id: 456 };

      activatedRoute.data = of({ cardFraudIncidentCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardFraudIncidentCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudIncidentCategory>>();
      const cardFraudIncidentCategory = { id: 123 };
      jest.spyOn(cardFraudIncidentCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudIncidentCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardFraudIncidentCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardFraudIncidentCategoryService.update).toHaveBeenCalledWith(cardFraudIncidentCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudIncidentCategory>>();
      const cardFraudIncidentCategory = new CardFraudIncidentCategory();
      jest.spyOn(cardFraudIncidentCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudIncidentCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardFraudIncidentCategory }));
      saveSubject.complete();

      // THEN
      expect(cardFraudIncidentCategoryService.create).toHaveBeenCalledWith(cardFraudIncidentCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudIncidentCategory>>();
      const cardFraudIncidentCategory = { id: 123 };
      jest.spyOn(cardFraudIncidentCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudIncidentCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardFraudIncidentCategoryService.update).toHaveBeenCalledWith(cardFraudIncidentCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
