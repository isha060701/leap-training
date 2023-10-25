import { Shop } from './shop';

describe('Shop', () => {
  it('should create an instance', () => {
    expect(new Shop(-1,"",-1,"")).toBeTruthy();
  });
});
