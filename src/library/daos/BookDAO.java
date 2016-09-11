package library.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookDAO implements IBookDAO {

    private IBookHelper helper_;
    private int nextId_;
    private Map<Integer, IBook> bookMap_;
    
    public BookDAO(IBookHelper helper) {
        if (helper == null) {
            throw new IllegalArgumentException("BookDAO : constructor : helper cannot be NULL");
        }
        nextId_  = 1;
        helper_  = helper;
        bookMap_ = new HashMap<Integer, IBook>();
    }
    
    @Override
    public IBook addBook(String author, String title, String callNo) {
        int id = getNextId();
        IBook book = helper_.makeBook(author, title, callNo, id);
        bookMap_.put(Integer.valueOf(id), book);
        return book;
    }

    @Override
    public IBook getBookByID(int id) {
        if (bookMap_.containsKey(Integer.valueOf(id))) {
            return bookMap_.get(Integer.valueOf(id));
        }
        return null;
    }

    @Override
    public List<IBook> listBooks() {
        return new ArrayList<IBook>(bookMap_.values());
    }

    @Override
    public List<IBook> findBooksByAuthor(String author) {
        List<IBook> list = new ArrayList<IBook>();
        if (!author.isEmpty()) {
            for (IBook book : bookMap_.values()) {
                if (author.equals(book.getAuthor())) {
                    list.add(book);
                }
            }
        }
        return list;
    }

    @Override
    public List<IBook> findBooksByTitle(String title) {
        List<IBook> list = new ArrayList<IBook>();
        if (!title.isEmpty()) {
            for (IBook book : bookMap_.values()) {
                if (title.equals(book.getTitle())) {
                    list.add(book);
                }
            }
        }
        return list;
    }

    @Override
    public List<IBook> findBooksByAuthorTitle(String author, String title) {
        List<IBook> list = new ArrayList<IBook>();
        if (!title.isEmpty() || !author.isEmpty()) {
            for (IBook book : bookMap_.values()) {
                if (author.equals(book.getAuthor()) && title.equals(book.getTitle())) {
                    list.add(book);
                }
            }
        }
        return list;
    }
    
    private int getNextId() {
        return nextId_++;
    }
}
