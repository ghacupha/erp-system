jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardStateService } from '../service/card-state.service';
import { ICardState, CardState } from '../card-state.model';

import { CardStateUpdateComponent } from './card-state-update.component';

describe('CardState Management Update Component', () => {
  let comp: CardStateUpdateComponent;
  let fixture: ComponentFixture<CardStateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardStateService: CardStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardStateUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardStateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardStateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardStateService = TestBed.inject(CardStateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardState: ICardState = { id: 456 };

      activatedRoute.data = of({ cardState });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardState));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardState>>();
      const cardState = { id: 123 };
      jest.spyOn(cardStateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardState }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardStateService.update).toHaveBeenCalledWith(cardState);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardState>>();
      const cardState = new CardState();
      jest.spyOn(cardStateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardState }));
      saveSubject.complete();

      // THEN
      expect(cardStateService.create).toHaveBeenCalledWith(cardState);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardState>>();
      const cardState = { id: 123 };
      jest.spyOn(cardStateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardStateService.update).toHaveBeenCalledWith(cardState);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
