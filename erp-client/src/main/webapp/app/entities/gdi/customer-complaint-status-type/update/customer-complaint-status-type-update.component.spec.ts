jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomerComplaintStatusTypeService } from '../service/customer-complaint-status-type.service';
import { ICustomerComplaintStatusType, CustomerComplaintStatusType } from '../customer-complaint-status-type.model';

import { CustomerComplaintStatusTypeUpdateComponent } from './customer-complaint-status-type-update.component';

describe('CustomerComplaintStatusType Management Update Component', () => {
  let comp: CustomerComplaintStatusTypeUpdateComponent;
  let fixture: ComponentFixture<CustomerComplaintStatusTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerComplaintStatusTypeService: CustomerComplaintStatusTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CustomerComplaintStatusTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CustomerComplaintStatusTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerComplaintStatusTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerComplaintStatusTypeService = TestBed.inject(CustomerComplaintStatusTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customerComplaintStatusType: ICustomerComplaintStatusType = { id: 456 };

      activatedRoute.data = of({ customerComplaintStatusType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(customerComplaintStatusType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerComplaintStatusType>>();
      const customerComplaintStatusType = { id: 123 };
      jest.spyOn(customerComplaintStatusTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerComplaintStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerComplaintStatusType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerComplaintStatusTypeService.update).toHaveBeenCalledWith(customerComplaintStatusType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerComplaintStatusType>>();
      const customerComplaintStatusType = new CustomerComplaintStatusType();
      jest.spyOn(customerComplaintStatusTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerComplaintStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerComplaintStatusType }));
      saveSubject.complete();

      // THEN
      expect(customerComplaintStatusTypeService.create).toHaveBeenCalledWith(customerComplaintStatusType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerComplaintStatusType>>();
      const customerComplaintStatusType = { id: 123 };
      jest.spyOn(customerComplaintStatusTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerComplaintStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerComplaintStatusTypeService.update).toHaveBeenCalledWith(customerComplaintStatusType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
