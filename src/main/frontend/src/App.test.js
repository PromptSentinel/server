import { render, screen } from '@testing-library/react';
import Overview from './Overview';

test('renders learn react link', () => {
  render(<Overview />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
