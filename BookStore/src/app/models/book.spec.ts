import { Book } from './book';

describe('Book', () => {
  it('should create an instance', () => {
    expect(new Book("Think and Grow Rich","Napoleon Hill", "",42)).toBeTruthy();
  });
});
