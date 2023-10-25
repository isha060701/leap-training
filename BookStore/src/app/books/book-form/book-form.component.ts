import { Component, EventEmitter, Output } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Book } from "src/app/models/book";

@Component({
  selector: "app-book-form",
  templateUrl: "./book-form.component.html",
  styleUrls: ["./book-form.component.css"],
})
export class BookFormComponent {
  constructor(private formBuilder: FormBuilder) {}
  // book: Book = new Book("", "", "", -1);

  @Output() createBook = new EventEmitter<Book>();
  bookForm: FormGroup = new FormGroup({});
  add() {
    // console.log(`book = ${JSON.stringify(this.book)}`);
    // this.createBook.emit(this.book);
    // this.book = new Book("", "", "", -1);

    this.createBook.emit(
      new Book(
        this.bookForm.get("title")?.value,
        this.bookForm.get("author")?.value,
        "",
        -1
      )
    );
    this.bookForm.reset();
  }

  ngOnInit() {
    this.bookForm = this.formBuilder.group({
      title: ["", Validators.required],
      author: ["", Validators.required],
    });
  }
}
