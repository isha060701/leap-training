import { ComponentFixture, TestBed } from "@angular/core/testing";

import { BookListComponent } from "./book-list.component";
import { Pipe, PipeTransform } from "@angular/core";
import { RouterTestingModule } from "@angular/router/testing";

@Pipe({
  name: "noImage",
})
class MockNoImagePipe implements PipeTransform {
  transform(value: string): string {
    return value;
  }
}
describe("BookListComponent", () => {
  let component: BookListComponent;
  let fixture: ComponentFixture<BookListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BookListComponent,MockNoImagePipe],
      imports:[RouterTestingModule]
    }).compileComponents();

    fixture = TestBed.createComponent(BookListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("should contain a table", () => {
    component.books = [
      {
        title: "The Lord of the Rings",
        author: "J R R Tolkien",
        cover: "",
        bookId: 1,
      },
      {
        title: "The Left Hand of Darkness",
        author: "Ursula K Le Guin",
        cover: "",
        bookId: 2,
      },
    ];
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const table = compiled.querySelector("table");
    console.log(table);
    expect(table.rows.length).toBeGreaterThanOrEqual(3);
    expect(table.rows[1].cells[0].textContent).toBe("The Lord of the Rings");
  });
});
