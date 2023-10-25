import { NoImagePipe } from "./no-image.pipe";

describe("NoImagePipe", () => {
  it("create an instance", () => {
    const pipe = new NoImagePipe();
    expect(pipe).toBeTruthy();
  });

  it("should pass a specified image through", () => {
    const pipe = new NoImagePipe();
    expect(pipe.transform("a.png")).toEqual("a.png");
  });

  it("should use default if there is no value", () => {
    const pipe = new NoImagePipe();
    expect(pipe.transform("")).toEqual("/assets/images/NoImage.svg");
  });
});
