jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NatureOfCustomerComplaintsService } from '../service/nature-of-customer-complaints.service';
import { INatureOfCustomerComplaints, NatureOfCustomerComplaints } from '../nature-of-customer-complaints.model';

import { NatureOfCustomerComplaintsUpdateComponent } from './nature-of-customer-complaints-update.component';

describe('NatureOfCustomerComplaints Management Update Component', () => {
  let comp: NatureOfCustomerComplaintsUpdateComponent;
  let fixture: ComponentFixture<NatureOfCustomerComplaintsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let natureOfCustomerComplaintsService: NatureOfCustomerComplaintsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NatureOfCustomerComplaintsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NatureOfCustomerComplaintsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NatureOfCustomerComplaintsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    natureOfCustomerComplaintsService = TestBed.inject(NatureOfCustomerComplaintsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const natureOfCustomerComplaints: INatureOfCustomerComplaints = { id: 456 };

      activatedRoute.data = of({ natureOfCustomerComplaints });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(natureOfCustomerComplaints));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NatureOfCustomerComplaints>>();
      const natureOfCustomerComplaints = { id: 123 };
      jest.spyOn(natureOfCustomerComplaintsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ natureOfCustomerComplaints });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: natureOfCustomerComplaints }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(natureOfCustomerComplaintsService.update).toHaveBeenCalledWith(natureOfCustomerComplaints);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NatureOfCustomerComplaints>>();
      const natureOfCustomerComplaints = new NatureOfCustomerComplaints();
      jest.spyOn(natureOfCustomerComplaintsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ natureOfCustomerComplaints });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: natureOfCustomerComplaints }));
      saveSubject.complete();

      // THEN
      expect(natureOfCustomerComplaintsService.create).toHaveBeenCalledWith(natureOfCustomerComplaints);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NatureOfCustomerComplaints>>();
      const natureOfCustomerComplaints = { id: 123 };
      jest.spyOn(natureOfCustomerComplaintsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ natureOfCustomerComplaints });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(natureOfCustomerComplaintsService.update).toHaveBeenCalledWith(natureOfCustomerComplaints);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
