jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardTypesService } from '../service/card-types.service';
import { ICardTypes, CardTypes } from '../card-types.model';

import { CardTypesUpdateComponent } from './card-types-update.component';

describe('CardTypes Management Update Component', () => {
  let comp: CardTypesUpdateComponent;
  let fixture: ComponentFixture<CardTypesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardTypesService: CardTypesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardTypesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardTypesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardTypesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardTypesService = TestBed.inject(CardTypesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardTypes: ICardTypes = { id: 456 };

      activatedRoute.data = of({ cardTypes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardTypes));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardTypes>>();
      const cardTypes = { id: 123 };
      jest.spyOn(cardTypesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardTypes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardTypesService.update).toHaveBeenCalledWith(cardTypes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardTypes>>();
      const cardTypes = new CardTypes();
      jest.spyOn(cardTypesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardTypes }));
      saveSubject.complete();

      // THEN
      expect(cardTypesService.create).toHaveBeenCalledWith(cardTypes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardTypes>>();
      const cardTypes = { id: 123 };
      jest.spyOn(cardTypesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardTypesService.update).toHaveBeenCalledWith(cardTypes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
