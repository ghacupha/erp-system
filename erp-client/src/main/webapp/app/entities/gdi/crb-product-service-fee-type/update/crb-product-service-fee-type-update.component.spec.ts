jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbProductServiceFeeTypeService } from '../service/crb-product-service-fee-type.service';
import { ICrbProductServiceFeeType, CrbProductServiceFeeType } from '../crb-product-service-fee-type.model';

import { CrbProductServiceFeeTypeUpdateComponent } from './crb-product-service-fee-type-update.component';

describe('CrbProductServiceFeeType Management Update Component', () => {
  let comp: CrbProductServiceFeeTypeUpdateComponent;
  let fixture: ComponentFixture<CrbProductServiceFeeTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbProductServiceFeeTypeService: CrbProductServiceFeeTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbProductServiceFeeTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbProductServiceFeeTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbProductServiceFeeTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbProductServiceFeeTypeService = TestBed.inject(CrbProductServiceFeeTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbProductServiceFeeType: ICrbProductServiceFeeType = { id: 456 };

      activatedRoute.data = of({ crbProductServiceFeeType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbProductServiceFeeType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbProductServiceFeeType>>();
      const crbProductServiceFeeType = { id: 123 };
      jest.spyOn(crbProductServiceFeeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbProductServiceFeeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbProductServiceFeeType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbProductServiceFeeTypeService.update).toHaveBeenCalledWith(crbProductServiceFeeType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbProductServiceFeeType>>();
      const crbProductServiceFeeType = new CrbProductServiceFeeType();
      jest.spyOn(crbProductServiceFeeTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbProductServiceFeeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbProductServiceFeeType }));
      saveSubject.complete();

      // THEN
      expect(crbProductServiceFeeTypeService.create).toHaveBeenCalledWith(crbProductServiceFeeType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbProductServiceFeeType>>();
      const crbProductServiceFeeType = { id: 123 };
      jest.spyOn(crbProductServiceFeeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbProductServiceFeeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbProductServiceFeeTypeService.update).toHaveBeenCalledWith(crbProductServiceFeeType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
