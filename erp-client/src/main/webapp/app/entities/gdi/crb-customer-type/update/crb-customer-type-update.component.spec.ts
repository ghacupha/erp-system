jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbCustomerTypeService } from '../service/crb-customer-type.service';
import { ICrbCustomerType, CrbCustomerType } from '../crb-customer-type.model';

import { CrbCustomerTypeUpdateComponent } from './crb-customer-type-update.component';

describe('CrbCustomerType Management Update Component', () => {
  let comp: CrbCustomerTypeUpdateComponent;
  let fixture: ComponentFixture<CrbCustomerTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbCustomerTypeService: CrbCustomerTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbCustomerTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbCustomerTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbCustomerTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbCustomerTypeService = TestBed.inject(CrbCustomerTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbCustomerType: ICrbCustomerType = { id: 456 };

      activatedRoute.data = of({ crbCustomerType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbCustomerType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbCustomerType>>();
      const crbCustomerType = { id: 123 };
      jest.spyOn(crbCustomerTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbCustomerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbCustomerType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbCustomerTypeService.update).toHaveBeenCalledWith(crbCustomerType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbCustomerType>>();
      const crbCustomerType = new CrbCustomerType();
      jest.spyOn(crbCustomerTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbCustomerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbCustomerType }));
      saveSubject.complete();

      // THEN
      expect(crbCustomerTypeService.create).toHaveBeenCalledWith(crbCustomerType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbCustomerType>>();
      const crbCustomerType = { id: 123 };
      jest.spyOn(crbCustomerTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbCustomerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbCustomerTypeService.update).toHaveBeenCalledWith(crbCustomerType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
