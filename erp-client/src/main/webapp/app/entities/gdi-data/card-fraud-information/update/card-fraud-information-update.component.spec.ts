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

import { CardFraudInformationService } from '../service/card-fraud-information.service';
import { ICardFraudInformation, CardFraudInformation } from '../card-fraud-information.model';

import { CardFraudInformationUpdateComponent } from './card-fraud-information-update.component';

describe('CardFraudInformation Management Update Component', () => {
  let comp: CardFraudInformationUpdateComponent;
  let fixture: ComponentFixture<CardFraudInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardFraudInformationService: CardFraudInformationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardFraudInformationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardFraudInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardFraudInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardFraudInformationService = TestBed.inject(CardFraudInformationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardFraudInformation: ICardFraudInformation = { id: 456 };

      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardFraudInformation));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudInformation>>();
      const cardFraudInformation = { id: 123 };
      jest.spyOn(cardFraudInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardFraudInformation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardFraudInformationService.update).toHaveBeenCalledWith(cardFraudInformation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudInformation>>();
      const cardFraudInformation = new CardFraudInformation();
      jest.spyOn(cardFraudInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardFraudInformation }));
      saveSubject.complete();

      // THEN
      expect(cardFraudInformationService.create).toHaveBeenCalledWith(cardFraudInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudInformation>>();
      const cardFraudInformation = { id: 123 };
      jest.spyOn(cardFraudInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardFraudInformationService.update).toHaveBeenCalledWith(cardFraudInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
