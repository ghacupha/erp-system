jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardClassTypeService } from '../service/card-class-type.service';
import { ICardClassType, CardClassType } from '../card-class-type.model';

import { CardClassTypeUpdateComponent } from './card-class-type-update.component';

describe('CardClassType Management Update Component', () => {
  let comp: CardClassTypeUpdateComponent;
  let fixture: ComponentFixture<CardClassTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardClassTypeService: CardClassTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardClassTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardClassTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardClassTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardClassTypeService = TestBed.inject(CardClassTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardClassType: ICardClassType = { id: 456 };

      activatedRoute.data = of({ cardClassType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardClassType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardClassType>>();
      const cardClassType = { id: 123 };
      jest.spyOn(cardClassTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardClassType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardClassType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardClassTypeService.update).toHaveBeenCalledWith(cardClassType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardClassType>>();
      const cardClassType = new CardClassType();
      jest.spyOn(cardClassTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardClassType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardClassType }));
      saveSubject.complete();

      // THEN
      expect(cardClassTypeService.create).toHaveBeenCalledWith(cardClassType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardClassType>>();
      const cardClassType = { id: 123 };
      jest.spyOn(cardClassTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardClassType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardClassTypeService.update).toHaveBeenCalledWith(cardClassType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
