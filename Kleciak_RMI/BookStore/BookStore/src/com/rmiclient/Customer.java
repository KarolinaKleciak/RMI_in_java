package com.rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import javax.swing.JOptionPane;

import com.rmiinterface.Book;
import com.rmiinterface.RMIInterface;

public class Customer {
	private static RMIInterface look_up;

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		look_up = (RMIInterface) Naming.lookup("//localhost/MyBookstore");

		boolean findmore;
		do {
			String[] options = {"Show All", "Find a Book by ISBN", "Search Books", "Purchase a Book", "View All Books", "Exit"};
			int choice = JOptionPane.showOptionDialog(null, "Welcome in Book store! Choose an action", "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

			switch (choice) {
				case 0:
					List<Book> list = look_up.allBooks();
					StringBuilder message = new StringBuilder();
					list.forEach(x -> {
						message.append(x.toString()).append("\n");
					});
					JOptionPane.showMessageDialog(null, new String(message));
					break;
				case 1:
					String isbn = JOptionPane.showInputDialog("Type the ISBN of the book you want to find.");
					try {
						Book response = look_up.findBook(new Book(isbn));
						JOptionPane.showMessageDialog(null, "Title: " + response.getTitle() + "\n" +
								"Publisher: " + response.getPublisher() + "\n" +
								"Cost: $" + response.getCost() + "\n" +
								"Quantity: " + response.getQuantity(), response.getIsbn(), JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Book not found. An error occurred during the find. Complete the field correctly");
					}
					break;
				case 2:
					String parameter = JOptionPane.showInputDialog("Type the parameter you want to search by (title, ISBN, publisher, cost, quantity).");
					if (parameter == null || parameter.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "An error occurred during the search. Complete the field correctly");
						break;
					}
					try {
						List<Book> searchResults = look_up.searchBooks(parameter);
						if (searchResults.isEmpty()) {
							JOptionPane.showMessageDialog(null, "No books found. Complete the field correctly");
						} else {
							StringBuilder searchMessage = new StringBuilder();
							searchResults.forEach(x -> {
								searchMessage.append("Title: ").append(x.getTitle()).append("\n")
										.append("Publisher: ").append(x.getPublisher()).append("\n")
										.append("Cost: $").append(x.getCost()).append("\n")
										.append("Quantity: ").append(x.getQuantity()).append("\n\n");
							});
							JOptionPane.showMessageDialog(null, new String(searchMessage));
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred during the search. Complete the field correctly");
					}
					break;
				case 3:
					String purchaseInput = JOptionPane.showInputDialog("Type the ISBN, title, publisher, or cost of the book you want to purchase.");
					if (purchaseInput == null || purchaseInput.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "An error occurred during the purchase. Complete the field correctly");
						break;
					}
					String quantityInput = JOptionPane.showInputDialog("Type the quantity you want to purchase.");
					if (quantityInput == null || quantityInput.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "An error occurred during the purchase. Complete the field correctly");
						break;
					}
					try {
						int purchaseQuantity = Integer.parseInt(quantityInput);
						if (purchaseQuantity <= 0) {
							throw new NumberFormatException();
						}
						boolean success = look_up.purchaseBook(purchaseInput, purchaseQuantity);
						if (success) {
							JOptionPane.showMessageDialog(null, "Purchase successful!");
						} else {
							JOptionPane.showMessageDialog(null, "Purchase failed. Not enough books in stock or book not found.");
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "An error occurred during the purchase. Complete the field correctly");
					}
					break;
				case 4:
					try {
						List<Book> allBooks = look_up.getAllBooks();
						if (allBooks.isEmpty()) {
							JOptionPane.showMessageDialog(null, "No books available.");
						} else {
							StringBuilder allBooksMessage = new StringBuilder();
							allBooks.forEach(x -> {
								allBooksMessage.append("Title: ").append(x.getTitle()).append("\n")
										.append("ISBN: ").append(x.getIsbn()).append("\n")
										.append("Publisher: ").append(x.getPublisher()).append("\n")
										.append("Cost: $").append(x.getCost()).append("\n")
										.append("Quantity: ").append(x.getQuantity()).append("\n\n");
							});
							JOptionPane.showMessageDialog(null, new String(allBooksMessage));
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred while retrieving the list of books.");
					}
					break;
				default:
					JOptionPane.showMessageDialog(null, "Thank you for visiting my Book Store. I invite you again ");
					System.exit(0);
					break;
			}
			findmore = (JOptionPane.showConfirmDialog(null, "Do you want to perform another action?", "Continue", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
		} while (findmore);
		JOptionPane.showMessageDialog(null, "Thank you for visiting my Book Store. I invite you again ");
		System.exit(0);
	}
}
