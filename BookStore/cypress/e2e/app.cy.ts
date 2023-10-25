import { AppPageObject } from "cypress/support/app.po";
import { TablePageObject } from "cypress/support/table.po";
let appPage: AppPageObject = new AppPageObject();
let tablePage: TablePageObject = new TablePageObject();

describe("My First Test", () => {
  it("Visits the initial project page", () => {
    appPage.navigateToHomePage();
    appPage.checkTitle("Welcome to the Angles on Books!");
  });

});

describe("My Second Test", () => {
  it("contains headers and a table of books", () => {
    appPage.navigateToHomePage();
    appPage.checkTitle("Welcome to the Angles on Books!");
    tablePage.checkBookTableHeader("Books");
    tablePage.getBookTableRows().should("have.length.gte", 2);
    tablePage.getBookTableFirstRowTitleColumn().should("eq", "Design Patterns");
    tablePage.getBookTableLastRowTitleColumn().should("eq", "Cryptonomicon");
  });
});

