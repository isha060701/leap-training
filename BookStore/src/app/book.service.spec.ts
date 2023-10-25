import { TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'
import { BookService } from './book.service';
import { Book } from './models/book';
import { HttpErrorResponse } from '@angular/common/http';

describe('BookService', () => {
  let service: BookService;
  let httpTestingController: HttpTestingController;
  let testBooks: Book[] = [{
    title: 'The Lord of the Rings',
    author: 'J R R Tolkien',
    cover: '',
    bookId: 1
    }, {
    title: 'The Left Hand of Darkness',
    author: 'Ursula K Le Guin',
    cover: '',
    bookId: 2
    }];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(BookService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return books', inject([BookService], fakeAsync((service: BookService) => { 
    let books: Book[] = []; 
    service.getBooks() .subscribe(data => books = data); 
    const req = httpTestingController.expectOne( 'http://localhost:8080/BookService/jaxrs/books');
     // Assert that the request is a GET. 
     expect(req.request.method).toEqual('GET'); 
     // Respond with mock data, causing Observable to resolve. 
     req.flush(testBooks); // Cause all Observables to complete and check the results 
     tick(); expect(books[0].title).toBe('The Lord of the Rings'); })));


  
  

  it('should POST to add a book', inject([BookService], fakeAsync((service: BookService) => {
    const expected = new Book('A Wizard of EarthSea', 'Ursula K Le Guin', '', 3);
    service.addBook(expected).subscribe();
    const req = httpTestingController.expectOne('http://localhost:8080/BookService/jaxrs/books');
    // Assert that the request is a POST. 
    expect(req.request.method).toEqual('POST');
    // Assert that it was called with the right data 
    expect(req.request.body).toBe(expected);
  })));

  it('should handle a 404 error', inject([BookService], fakeAsync((service: BookService) => { 
    let errorResp: HttpErrorResponse; 
    let errorReply: string = ''; 
    const errorHandlerSpy = spyOn(service,'handleError') .and.callThrough(); 
    service.getBooks() .subscribe({next: () => fail('Should not succeed'), error: (err) => errorReply = err}); 
    const req = httpTestingController.expectOne(service.url);
    // Assert that the request is a GET. 
    expect(req.request.method).toEqual('GET'); 
    // Respond with error 
    req.flush('Forced 404', { status: 404, statusText: 'Not Found' }); 
    // Cause all Observables to complete and check the results 
    tick(); expect(errorReply).toBe( 'Unable to contact service; please try again later.'); 
    expect(errorHandlerSpy).toHaveBeenCalled(); errorResp = errorHandlerSpy.calls.argsFor(0)[0]; 
    expect(errorResp.status).toBe(404); })));
});
