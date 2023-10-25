import { Injectable } from '@angular/core';
import { Book } from './models/book';
import { Observable, catchError, of, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  public url = '/BookService/jaxrs/books';

  books: Book[] = [{
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
  addBook(book: Book): Observable<Book> {
    const headers = new HttpHeaders({ 'Content-type': 'application/json' });
    return this.http.post<Book>(this.url, book, { headers: headers });
  }
  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.url).pipe(catchError(this.handleError));
  }

  handleError(response: HttpErrorResponse) {
    if (response.error instanceof ProgressEvent) {
      console.error('A client-side or network error occurred; ' + `${response.message} ${response.status} ${response.statusText}`);
    }
    else { console.error(`Backend returned code ${response.status}, ` + `body was: ${JSON.stringify(response.error)}`); }
    return throwError(() => 'Unable to contact service; please try again later.');
  }
  constructor(private http: HttpClient) { }

  
}
