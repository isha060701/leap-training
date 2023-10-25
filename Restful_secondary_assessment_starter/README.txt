Notes for anyone proctoring the assessment:

IMPORTANT: note that the URL for the request to the associates' service has only one parameter, a vessel name; but the backend API URL has parameters for both the marina name and vessel name.

Associates should execute the starter's schema.sql and data.sql from SQL Developer so they can interactively test their query. But when they run their service or their tests, they'll be using the in-memory hsqldb database. So if someone changes the contents of an Oracle table, their service and tests won't see that change.

E2E tests: the numeric data values are random, so tests can't assert expected values. But they can verify values are greater than 0, etc.

The backend API returns status 200 and valid data for all vessels, even if they're not in the DB, and even if the vessel is in a different marina. So the E2E test can't have a negative test for status 400. The API returns status 500 if the marina is not in the DB.
