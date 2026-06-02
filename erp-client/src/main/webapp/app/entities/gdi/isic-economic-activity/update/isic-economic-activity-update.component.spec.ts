jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IsicEconomicActivityService } from '../service/isic-economic-activity.service';
import { IIsicEconomicActivity, IsicEconomicActivity } from '../isic-economic-activity.model';

import { IsicEconomicActivityUpdateComponent } from './isic-economic-activity-update.component';

describe('IsicEconomicActivity Management Update Component', () => {
  let comp: IsicEconomicActivityUpdateComponent;
  let fixture: ComponentFixture<IsicEconomicActivityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let isicEconomicActivityService: IsicEconomicActivityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IsicEconomicActivityUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(IsicEconomicActivityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IsicEconomicActivityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    isicEconomicActivityService = TestBed.inject(IsicEconomicActivityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const isicEconomicActivity: IIsicEconomicActivity = { id: 456 };

      activatedRoute.data = of({ isicEconomicActivity });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(isicEconomicActivity));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsicEconomicActivity>>();
      const isicEconomicActivity = { id: 123 };
      jest.spyOn(isicEconomicActivityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isicEconomicActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: isicEconomicActivity }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(isicEconomicActivityService.update).toHaveBeenCalledWith(isicEconomicActivity);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsicEconomicActivity>>();
      const isicEconomicActivity = new IsicEconomicActivity();
      jest.spyOn(isicEconomicActivityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isicEconomicActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: isicEconomicActivity }));
      saveSubject.complete();

      // THEN
      expect(isicEconomicActivityService.create).toHaveBeenCalledWith(isicEconomicActivity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsicEconomicActivity>>();
      const isicEconomicActivity = { id: 123 };
      jest.spyOn(isicEconomicActivityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isicEconomicActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(isicEconomicActivityService.update).toHaveBeenCalledWith(isicEconomicActivity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
