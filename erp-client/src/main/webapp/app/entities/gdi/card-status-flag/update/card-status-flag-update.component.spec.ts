jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardStatusFlagService } from '../service/card-status-flag.service';
import { ICardStatusFlag, CardStatusFlag } from '../card-status-flag.model';

import { CardStatusFlagUpdateComponent } from './card-status-flag-update.component';

describe('CardStatusFlag Management Update Component', () => {
  let comp: CardStatusFlagUpdateComponent;
  let fixture: ComponentFixture<CardStatusFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardStatusFlagService: CardStatusFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardStatusFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardStatusFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardStatusFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardStatusFlagService = TestBed.inject(CardStatusFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardStatusFlag: ICardStatusFlag = { id: 456 };

      activatedRoute.data = of({ cardStatusFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardStatusFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardStatusFlag>>();
      const cardStatusFlag = { id: 123 };
      jest.spyOn(cardStatusFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardStatusFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardStatusFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardStatusFlagService.update).toHaveBeenCalledWith(cardStatusFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardStatusFlag>>();
      const cardStatusFlag = new CardStatusFlag();
      jest.spyOn(cardStatusFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardStatusFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardStatusFlag }));
      saveSubject.complete();

      // THEN
      expect(cardStatusFlagService.create).toHaveBeenCalledWith(cardStatusFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardStatusFlag>>();
      const cardStatusFlag = { id: 123 };
      jest.spyOn(cardStatusFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardStatusFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardStatusFlagService.update).toHaveBeenCalledWith(cardStatusFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
