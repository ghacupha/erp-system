jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PrepaymentAmortizationService } from '../service/prepayment-amortization.service';

import { PrepaymentAmortizationDeleteDialogComponent } from './prepayment-amortization-delete-dialog.component';

describe('PrepaymentAmortization Management Delete Component', () => {
  let comp: PrepaymentAmortizationDeleteDialogComponent;
  let fixture: ComponentFixture<PrepaymentAmortizationDeleteDialogComponent>;
  let service: PrepaymentAmortizationService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrepaymentAmortizationDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(PrepaymentAmortizationDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentAmortizationDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PrepaymentAmortizationService);
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
