jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BusinessSegmentTypesService } from '../service/business-segment-types.service';
import { IBusinessSegmentTypes, BusinessSegmentTypes } from '../business-segment-types.model';

import { BusinessSegmentTypesUpdateComponent } from './business-segment-types-update.component';

describe('BusinessSegmentTypes Management Update Component', () => {
  let comp: BusinessSegmentTypesUpdateComponent;
  let fixture: ComponentFixture<BusinessSegmentTypesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessSegmentTypesService: BusinessSegmentTypesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusinessSegmentTypesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BusinessSegmentTypesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessSegmentTypesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessSegmentTypesService = TestBed.inject(BusinessSegmentTypesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const businessSegmentTypes: IBusinessSegmentTypes = { id: 456 };

      activatedRoute.data = of({ businessSegmentTypes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(businessSegmentTypes));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessSegmentTypes>>();
      const businessSegmentTypes = { id: 123 };
      jest.spyOn(businessSegmentTypesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessSegmentTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessSegmentTypes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessSegmentTypesService.update).toHaveBeenCalledWith(businessSegmentTypes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessSegmentTypes>>();
      const businessSegmentTypes = new BusinessSegmentTypes();
      jest.spyOn(businessSegmentTypesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessSegmentTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessSegmentTypes }));
      saveSubject.complete();

      // THEN
      expect(businessSegmentTypesService.create).toHaveBeenCalledWith(businessSegmentTypes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusinessSegmentTypes>>();
      const businessSegmentTypes = { id: 123 };
      jest.spyOn(businessSegmentTypesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessSegmentTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessSegmentTypesService.update).toHaveBeenCalledWith(businessSegmentTypes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
