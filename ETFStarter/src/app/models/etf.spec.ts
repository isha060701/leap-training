import { Etf } from './etf';

describe('Etf', () => {
  it('should create an instance', () => {
    expect(new Etf("BAR", "GraniteShares Gold Trust","GraniteShares",0.877,0.0017,0.0795,"Commodities: Precious Metals Gold")).toBeTruthy();
  });
});
