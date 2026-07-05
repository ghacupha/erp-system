jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SettlementRequisitionService } from '../service/settlement-requisition.service';

import { SettlementRequisitionDeleteDialogComponent } from './settlement-requisition-delete-dialog.component';

describe('SettlementRequisition Management Delete Component', () => {
  let comp: SettlementRequisitionDeleteDialogComponent;
  let fixture: ComponentFixture<SettlementRequisitionDeleteDialogComponent>;
  let service: SettlementRequisitionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SettlementRequisitionDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SettlementRequisitionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SettlementRequisitionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SettlementRequisitionService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
