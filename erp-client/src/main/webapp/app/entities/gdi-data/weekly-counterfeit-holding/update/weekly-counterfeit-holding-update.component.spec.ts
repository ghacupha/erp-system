jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WeeklyCounterfeitHoldingService } from '../service/weekly-counterfeit-holding.service';
import { IWeeklyCounterfeitHolding, WeeklyCounterfeitHolding } from '../weekly-counterfeit-holding.model';

import { WeeklyCounterfeitHoldingUpdateComponent } from './weekly-counterfeit-holding-update.component';

describe('WeeklyCounterfeitHolding Management Update Component', () => {
  let comp: WeeklyCounterfeitHoldingUpdateComponent;
  let fixture: ComponentFixture<WeeklyCounterfeitHoldingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let weeklyCounterfeitHoldingService: WeeklyCounterfeitHoldingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WeeklyCounterfeitHoldingUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(WeeklyCounterfeitHoldingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeeklyCounterfeitHoldingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    weeklyCounterfeitHoldingService = TestBed.inject(WeeklyCounterfeitHoldingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const weeklyCounterfeitHolding: IWeeklyCounterfeitHolding = { id: 456 };

      activatedRoute.data = of({ weeklyCounterfeitHolding });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(weeklyCounterfeitHolding));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeeklyCounterfeitHolding>>();
      const weeklyCounterfeitHolding = { id: 123 };
      jest.spyOn(weeklyCounterfeitHoldingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weeklyCounterfeitHolding });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weeklyCounterfeitHolding }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(weeklyCounterfeitHoldingService.update).toHaveBeenCalledWith(weeklyCounterfeitHolding);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeeklyCounterfeitHolding>>();
      const weeklyCounterfeitHolding = new WeeklyCounterfeitHolding();
      jest.spyOn(weeklyCounterfeitHoldingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weeklyCounterfeitHolding });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weeklyCounterfeitHolding }));
      saveSubject.complete();

      // THEN
      expect(weeklyCounterfeitHoldingService.create).toHaveBeenCalledWith(weeklyCounterfeitHolding);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeeklyCounterfeitHolding>>();
      const weeklyCounterfeitHolding = { id: 123 };
      jest.spyOn(weeklyCounterfeitHoldingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weeklyCounterfeitHolding });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(weeklyCounterfeitHoldingService.update).toHaveBeenCalledWith(weeklyCounterfeitHolding);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
