package com.rmiserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.rmiinterface.Book;
import com.rmiinterface.RMIInterface;

public class Bookstore extends UnicastRemoteObject implements RMIInterface {
	private static final long serialVersionUID = 1L;
	private List<Book> bookList;

	protected Bookstore(List<Book> list) throws RemoteException {
		super();
		this.bookList = list;
	}

	@Override
	public Book findBook(Book book) throws RemoteException {
		String lowerCaseIsbn = book.getIsbn().toLowerCase();
		Predicate<Book> predicate = x -> x.getIsbn().toLowerCase().equals(lowerCaseIsbn);
		return bookList.stream().filter(predicate).findFirst().orElse(null);
	}

	@Override
	public List<Book> allBooks() throws RemoteException {
		return bookList;
	}

	@Override
	public List<Book> getAllBooks() throws RemoteException {
		return new ArrayList<>(bookList);
	}

	@Override
	public List<Book> searchBooks(String parameter) throws RemoteException {
		String lowerParameter = parameter.toLowerCase();
		return bookList.stream()
				.filter(book -> book.getTitle().toLowerCase().equals(lowerParameter) ||
						book.getIsbn().toLowerCase().equals(lowerParameter) ||
						book.getPublisher().toLowerCase().equals(lowerParameter) ||
						String.valueOf(book.getCost()).equals(lowerParameter) ||
						String.valueOf(book.getQuantity()).equals(lowerParameter))
				.collect(Collectors.toList());
	}

	@Override
	public boolean purchaseBook(String input, int quantity) throws RemoteException {
		if (quantity <= 0) {
			return false;
		}

		String lowerInput = input.toLowerCase();
		Book book = bookList.stream()
				.filter(b -> b.getTitle().toLowerCase().equals(lowerInput) ||
						b.getIsbn().toLowerCase().equals(lowerInput) ||
						b.getPublisher().toLowerCase().equals(lowerInput) ||
						String.valueOf(b.getCost()).equals(lowerInput))
				.findFirst()
				.orElse(null);

		if (book != null && book.getQuantity() >= quantity) {
			book.setQuantity(book.getQuantity() - quantity);

			if (book.getQuantity() == 0) {
				bookList.remove(book);
			}
			return true;
		} else {
			return false;
		}
	}

	private static List<Book> initializeList() {
		List<Book> list = new ArrayList<>();
		list.add(new Book("The Great Adventure", "IB-111", "HarperCollins", 30.20, 10));
		list.add(new Book("Mystery of the Lost Island", "IB-222", "Penguin Random House", 24.11, 5));
		list.add(new Book("Journey to the Unknown", "IB-333", "Simon & Schuster", 43.10, 8));
		list.add(new Book("The Secrets of the Universe", "IB-444", "Macmillan Publishers", 29.20, 3));
		list.add(new Book("The Last Kingdom", "IB-555", "Hachette Livre", 56.80, 7));
		list.add(new Book("Whispers in the Dark", "IB-666", "Bloomsbury Publishing", 60.20, 2));
		list.add(new Book("Chronicles of the Ancient World", "IB-777", "Scholastic Inc.", 14.1, 9));
		return list;
	}

	public static void main(String[] args) {
		try {
			Naming.rebind("//localhost/MyBookstore", new Bookstore(initializeList()));
			System.err.println("Server is ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.getMessage());
		}
	}
}
