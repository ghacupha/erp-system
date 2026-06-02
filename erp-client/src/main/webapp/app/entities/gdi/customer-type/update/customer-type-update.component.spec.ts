jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomerTypeService } from '../service/customer-type.service';
import { ICustomerType, CustomerType } from '../customer-type.model';

import { CustomerTypeUpdateComponent } from './customer-type-update.component';

describe('CustomerType Management Update Component', () => {
  let comp: CustomerTypeUpdateComponent;
  let fixture: ComponentFixture<CustomerTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerTypeService: CustomerTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CustomerTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CustomerTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerTypeService = TestBed.inject(CustomerTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customerType: ICustomerType = { id: 456 };

      activatedRoute.data = of({ customerType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(customerType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerType>>();
      const customerType = { id: 123 };
      jest.spyOn(customerTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerTypeService.update).toHaveBeenCalledWith(customerType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerType>>();
      const customerType = new CustomerType();
      jest.spyOn(customerTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerType }));
      saveSubject.complete();

      // THEN
      expect(customerTypeService.create).toHaveBeenCalledWith(customerType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerType>>();
      const customerType = { id: 123 };
      jest.spyOn(customerTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerTypeService.update).toHaveBeenCalledWith(customerType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
