jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContractStatusService } from '../service/contract-status.service';
import { IContractStatus, ContractStatus } from '../contract-status.model';

import { ContractStatusUpdateComponent } from './contract-status-update.component';

describe('ContractStatus Management Update Component', () => {
  let comp: ContractStatusUpdateComponent;
  let fixture: ComponentFixture<ContractStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractStatusService: ContractStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContractStatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ContractStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractStatusService = TestBed.inject(ContractStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contractStatus: IContractStatus = { id: 456 };

      activatedRoute.data = of({ contractStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contractStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContractStatus>>();
      const contractStatus = { id: 123 };
      jest.spyOn(contractStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractStatusService.update).toHaveBeenCalledWith(contractStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContractStatus>>();
      const contractStatus = new ContractStatus();
      jest.spyOn(contractStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractStatus }));
      saveSubject.complete();

      // THEN
      expect(contractStatusService.create).toHaveBeenCalledWith(contractStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContractStatus>>();
      const contractStatus = { id: 123 };
      jest.spyOn(contractStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractStatusService.update).toHaveBeenCalledWith(contractStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
