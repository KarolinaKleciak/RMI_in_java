package com.rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote {
	public Book findBook(Book b) throws RemoteException;
	public List<Book> allBooks() throws RemoteException;
	public List<Book> searchBooks(String parameter) throws RemoteException;
	public boolean purchaseBook(String isbn, int quantity) throws RemoteException;
	public List<Book> getAllBooks() throws RemoteException;
}
