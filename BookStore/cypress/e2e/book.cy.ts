import { AppPageObject } from "cypress/support/app.po";
import { BookPage } from "cypress/support/book.po";
import { v4 as uuid } from "uuid";

let app: AppPageObject = new AppPageObject();
let book: BookPage = new BookPage();
it("should add a book", () => {
  // const title = "Zero History";
  const title = uuid();
  app.navigateToHomePage();
  cy.contains(title).should("not.exist");
  book.checkAddButtonDisabled();
  book.addAuthor("William Gibson");
  book.addTitle(title);
  book.checkAddButtonEnabled();
  book.clickAddBook();
  cy.contains(title).should("exist");
  cy.get('[type="text"]').clear()
});
