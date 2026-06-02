jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardCategoryTypeService } from '../service/card-category-type.service';
import { ICardCategoryType, CardCategoryType } from '../card-category-type.model';

import { CardCategoryTypeUpdateComponent } from './card-category-type-update.component';

describe('CardCategoryType Management Update Component', () => {
  let comp: CardCategoryTypeUpdateComponent;
  let fixture: ComponentFixture<CardCategoryTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardCategoryTypeService: CardCategoryTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardCategoryTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardCategoryTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardCategoryTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardCategoryTypeService = TestBed.inject(CardCategoryTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardCategoryType: ICardCategoryType = { id: 456 };

      activatedRoute.data = of({ cardCategoryType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardCategoryType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardCategoryType>>();
      const cardCategoryType = { id: 123 };
      jest.spyOn(cardCategoryTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardCategoryType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardCategoryTypeService.update).toHaveBeenCalledWith(cardCategoryType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardCategoryType>>();
      const cardCategoryType = new CardCategoryType();
      jest.spyOn(cardCategoryTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardCategoryType }));
      saveSubject.complete();

      // THEN
      expect(cardCategoryTypeService.create).toHaveBeenCalledWith(cardCategoryType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardCategoryType>>();
      const cardCategoryType = { id: 123 };
      jest.spyOn(cardCategoryTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardCategoryTypeService.update).toHaveBeenCalledWith(cardCategoryType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
