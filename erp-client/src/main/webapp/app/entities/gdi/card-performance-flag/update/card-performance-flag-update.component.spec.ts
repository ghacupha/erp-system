jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardPerformanceFlagService } from '../service/card-performance-flag.service';
import { ICardPerformanceFlag, CardPerformanceFlag } from '../card-performance-flag.model';

import { CardPerformanceFlagUpdateComponent } from './card-performance-flag-update.component';

describe('CardPerformanceFlag Management Update Component', () => {
  let comp: CardPerformanceFlagUpdateComponent;
  let fixture: ComponentFixture<CardPerformanceFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardPerformanceFlagService: CardPerformanceFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardPerformanceFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardPerformanceFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardPerformanceFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardPerformanceFlagService = TestBed.inject(CardPerformanceFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardPerformanceFlag: ICardPerformanceFlag = { id: 456 };

      activatedRoute.data = of({ cardPerformanceFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardPerformanceFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardPerformanceFlag>>();
      const cardPerformanceFlag = { id: 123 };
      jest.spyOn(cardPerformanceFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardPerformanceFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardPerformanceFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardPerformanceFlagService.update).toHaveBeenCalledWith(cardPerformanceFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardPerformanceFlag>>();
      const cardPerformanceFlag = new CardPerformanceFlag();
      jest.spyOn(cardPerformanceFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardPerformanceFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardPerformanceFlag }));
      saveSubject.complete();

      // THEN
      expect(cardPerformanceFlagService.create).toHaveBeenCalledWith(cardPerformanceFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardPerformanceFlag>>();
      const cardPerformanceFlag = { id: 123 };
      jest.spyOn(cardPerformanceFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardPerformanceFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardPerformanceFlagService.update).toHaveBeenCalledWith(cardPerformanceFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
