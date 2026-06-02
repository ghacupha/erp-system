jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LoanRepaymentFrequencyService } from '../service/loan-repayment-frequency.service';

import { LoanRepaymentFrequencyDeleteDialogComponent } from './loan-repayment-frequency-delete-dialog.component';

describe('LoanRepaymentFrequency Management Delete Component', () => {
  let comp: LoanRepaymentFrequencyDeleteDialogComponent;
  let fixture: ComponentFixture<LoanRepaymentFrequencyDeleteDialogComponent>;
  let service: LoanRepaymentFrequencyService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanRepaymentFrequencyDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(LoanRepaymentFrequencyDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LoanRepaymentFrequencyDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LoanRepaymentFrequencyService);
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
