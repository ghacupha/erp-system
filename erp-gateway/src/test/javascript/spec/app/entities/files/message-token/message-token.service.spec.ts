import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MessageTokenService } from 'app/entities/files/message-token/message-token.service';
import { IMessageToken, MessageToken } from 'app/shared/model/files/message-token.model';

describe('Service Tests', () => {
  describe('MessageToken Service', () => {
    let injector: TestBed;
    let service: MessageTokenService;
    let httpMock: HttpTestingController;
    let elemDefault: IMessageToken;
    let expectedResult: IMessageToken | IMessageToken[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(MessageTokenService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new MessageToken(0, 'AAAAAAA', 0, 'AAAAAAA', false, false, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MessageToken', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MessageToken()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MessageToken', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            timeSent: 1,
            tokenValue: 'BBBBBB',
            received: true,
            actioned: true,
            contentFullyEnqueued: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MessageToken', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            timeSent: 1,
            tokenValue: 'BBBBBB',
            received: true,
            actioned: true,
            contentFullyEnqueued: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MessageToken', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
