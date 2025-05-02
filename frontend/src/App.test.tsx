import { render, screen } from "@testing-library/react";
import App from "./App";

test("renders service picker title", () => {
  render(<App />);
  const titleElement = screen.getByText(/Подборщик услуг/i);
  expect(titleElement).toBeInTheDocument();
});
