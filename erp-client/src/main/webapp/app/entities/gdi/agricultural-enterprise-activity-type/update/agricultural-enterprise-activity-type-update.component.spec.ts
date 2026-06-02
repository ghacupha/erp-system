jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AgriculturalEnterpriseActivityTypeService } from '../service/agricultural-enterprise-activity-type.service';
import { IAgriculturalEnterpriseActivityType, AgriculturalEnterpriseActivityType } from '../agricultural-enterprise-activity-type.model';

import { AgriculturalEnterpriseActivityTypeUpdateComponent } from './agricultural-enterprise-activity-type-update.component';

describe('AgriculturalEnterpriseActivityType Management Update Component', () => {
  let comp: AgriculturalEnterpriseActivityTypeUpdateComponent;
  let fixture: ComponentFixture<AgriculturalEnterpriseActivityTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agriculturalEnterpriseActivityTypeService: AgriculturalEnterpriseActivityTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AgriculturalEnterpriseActivityTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AgriculturalEnterpriseActivityTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgriculturalEnterpriseActivityTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agriculturalEnterpriseActivityTypeService = TestBed.inject(AgriculturalEnterpriseActivityTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType = { id: 456 };

      activatedRoute.data = of({ agriculturalEnterpriseActivityType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(agriculturalEnterpriseActivityType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgriculturalEnterpriseActivityType>>();
      const agriculturalEnterpriseActivityType = { id: 123 };
      jest.spyOn(agriculturalEnterpriseActivityTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agriculturalEnterpriseActivityType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agriculturalEnterpriseActivityType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(agriculturalEnterpriseActivityTypeService.update).toHaveBeenCalledWith(agriculturalEnterpriseActivityType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgriculturalEnterpriseActivityType>>();
      const agriculturalEnterpriseActivityType = new AgriculturalEnterpriseActivityType();
      jest.spyOn(agriculturalEnterpriseActivityTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agriculturalEnterpriseActivityType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agriculturalEnterpriseActivityType }));
      saveSubject.complete();

      // THEN
      expect(agriculturalEnterpriseActivityTypeService.create).toHaveBeenCalledWith(agriculturalEnterpriseActivityType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgriculturalEnterpriseActivityType>>();
      const agriculturalEnterpriseActivityType = { id: 123 };
      jest.spyOn(agriculturalEnterpriseActivityTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agriculturalEnterpriseActivityType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agriculturalEnterpriseActivityTypeService.update).toHaveBeenCalledWith(agriculturalEnterpriseActivityType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
