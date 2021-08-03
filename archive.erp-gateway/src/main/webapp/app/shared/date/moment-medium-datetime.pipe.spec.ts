import * as moment from 'moment';
import {MomentMediumDatetimePipe} from "app/shared/date/moment-medium-datetime.pipe";

describe('FormatMediumDatePipe', () => {
  const formatMediumDatetimePipe = new MomentMediumDatetimePipe();

  it('should return an empty string when receive undefined', () => {
    expect(formatMediumDatetimePipe.transform(undefined)).toBe('');
  });

  it('should return an empty string when receive null', () => {
    expect(formatMediumDatetimePipe.transform(null)).toBe('');
  });

  it('should format date like this D MMM YYYY', () => {
    expect(formatMediumDatetimePipe.transform(moment('2020-11-16').locale('fr'))).toBe('16 Nov 2020 00:00:00');
  });
});
