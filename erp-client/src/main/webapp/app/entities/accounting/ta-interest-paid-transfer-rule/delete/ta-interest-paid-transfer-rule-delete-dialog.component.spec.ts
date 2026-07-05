jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TAInterestPaidTransferRuleService } from '../service/ta-interest-paid-transfer-rule.service';

import { TAInterestPaidTransferRuleDeleteDialogComponent } from './ta-interest-paid-transfer-rule-delete-dialog.component';

describe('TAInterestPaidTransferRule Management Delete Component', () => {
  let comp: TAInterestPaidTransferRuleDeleteDialogComponent;
  let fixture: ComponentFixture<TAInterestPaidTransferRuleDeleteDialogComponent>;
  let service: TAInterestPaidTransferRuleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TAInterestPaidTransferRuleDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TAInterestPaidTransferRuleDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TAInterestPaidTransferRuleDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TAInterestPaidTransferRuleService);
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
