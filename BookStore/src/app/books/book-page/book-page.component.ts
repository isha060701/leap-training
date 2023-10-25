import { Component } from '@angular/core';
import { Book } from '../../models/book';
import { BookService } from 'src/app/book.service';

@Component({
  selector: 'app-book-page',
  templateUrl: './book-page.component.html',
  styleUrls: ['./book-page.component.css']
})
export class BookPageComponent {
  books: Book[] = [];
  errorMessage: string = "";
  constructor(private bookService: BookService) { }
  ngOnInit() {
    this.getBooks();
  }
  getBooks() {
    this.bookService.getBooks()
    .subscribe({ next : (data) => { 
      this.books = data; 
      this.errorMessage = '';}, error: (e) => this.errorMessage = e });
  }
  addBook(book: Book) {
    this.bookService.addBook(book) .subscribe(() => this.getBooks());
  }
}
