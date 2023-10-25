import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookPageComponent } from './book-page.component';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Book } from 'src/app/models/book';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { BookService } from 'src/app/book.service';
import { RouterTestingModule } from '@angular/router/testing';

@Component({
  selector: 'app-book-list',
  template: 'mock book list'
})
class MockBookListComponent {
  @Input()
  books: Book[] = [];
}

@Component({
  selector: 'app-book-form',
  template: ''
})
class MockBookFormComponent {
  book: Book = new Book('', '', '', -1);
  @Output() createBook = new EventEmitter<Book>();
  add() {
    this.createBook.emit(this.book);
  }
}





describe('BookPageComponent', () => {
  let component: BookPageComponent;
  let fixture: ComponentFixture<BookPageComponent>;
  const testBooks: Book[] = [
    {
      title: 'The Hobbit',
      author: 'J R R Tolkien',
      cover: '',
      bookId: 1
    },
    {
      title: 'A Wizard of Earthsea',
      author: 'Ursula K Le Guin',
      cover: '',
      bookId: 2
    }];
  let mockBookService: any = jasmine.createSpyObj('BookService', ['getBooks', 'addBook']);
  mockBookService.getBooks.and.returnValue(of(testBooks));
  let addBookSpy: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        BookPageComponent,
        MockBookListComponent,
        MockBookFormComponent
      ],
      providers: [
        { provide: BookService, useValue: mockBookService }
      ],
      imports:[
        RouterTestingModule
      ]
    })
      .compileComponents();

    addBookSpy = mockBookService.addBook.and.callFake((param: any) => {
      return of(param); // return an Observable that wraps the param
    });
    fixture = TestBed.createComponent(BookPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();


  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });



  it('should pass books to the child component', () => {
    const bookList = fixture.debugElement.query(
      By.css('app-book-list')).componentInstance;
    expect(bookList.books.length).toBeGreaterThanOrEqual(2);
  });

  // it('should add a book to the array', () => {
  //   const oldLength = component.books.length;
  //   component.addBook(new Book('The Lathe of Heaven', 'Ursula K Le Guin', '', 3));
  //   expect(component.books.length).toBe(oldLength + 1);
  //   expect(component.books[oldLength].title).toBe('The Lathe of Heaven');
  // });

  it('should retrieve books from the service', () => {
    expect(component.books.length).toBeGreaterThanOrEqual(2);
    expect(component.books[0].title).toBe('The Hobbit');
    expect(component.books[1].title).toBe('A Wizard of Earthsea');
  });

  it('should call the service to add a book', () => {
    const expected = new Book('The Lathe of Heaven', 'Ursula K Le Guin', '', 3);
    component.addBook(expected); expect(addBookSpy).toHaveBeenCalledWith(expected);
  });

  it('should respond to output event from book form', () => {
    const expected = new Book('The Silmarillion', 'J R R Tolkien', '', 3); // Get the mock book form component 
    const bookForm = fixture.debugElement.query(By.css('app-book-form')).componentInstance; // Set the book 
    bookForm.book = expected; // Trigger the output event 
    bookForm.add(); // Now check the method was called 
    expect(addBookSpy).toHaveBeenCalledWith(expected);
  });

  it('should display an error message', () => { 
    let errorDiv = fixture.debugElement.nativeElement .querySelector('.error'); 
    expect(errorDiv).toBeFalsy(); component.errorMessage = 'An error'; 
    fixture.detectChanges(); 
    errorDiv = fixture.debugElement.nativeElement .querySelector('.error'); 
    expect(errorDiv).toBeTruthy(); });
});
