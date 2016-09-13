package library;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;

public class BorrowUC_CTL implements ICardReaderListener, 
									 IScannerListener, 
									 IBorrowUIListener {
    private IBorrowUI ui_;
    private EBorrowState state_;
    
    private ICardReader reader_;
    private IScanner scanner_;
    private IPrinter printer_;
    private IDisplay display_;
    
    private IBookDAO bookDAO_;
    private IMemberDAO memberDAO_;
    private ILoanDAO loanDAO_;
    
    private int scanCount_ = 0;
    
	private List<IBook> bookList_;
	private List<ILoan> loanList_;
	private IMember borrower_;
	
	private JPanel previous_;


	public BorrowUC_CTL(ICardReader reader, IScanner scanner, 
			IPrinter printer, IDisplay display,
			IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO ) {

		ui_    = new BorrowUC_UI(this);
		state_ = EBorrowState.CREATED;
		
		reader_  = reader;
		scanner_ = scanner;
		printer_ = printer;
        display_ = display;
        
        bookDAO_   = bookDAO;
        loanDAO_   = loanDAO;
        memberDAO_ = memberDAO;
	}
	
	public void initialise() {
		previous = display.getDisplay();
		display.setDisplay((JPanel) ui, "Borrow UI");		
	}
	
	public void close() {
		display.setDisplay(previous, "Main Menu");
	}

	@Override
	public void cardSwiped(int memberID) {
		throw new RuntimeException("Not implemented yet");
	}
	
	
	
	@Override
	public void bookScanned(int barcode) {
		throw new RuntimeException("Not implemented yet");
	}

	
	private void setState(EBorrowState state) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void cancelled() {
		close();
	}
	
	@Override
	public void scansCompleted() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansConfirmed() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansRejected() {
		throw new RuntimeException("Not implemented yet");
	}

	private String buildLoanListDisplay(List<ILoan> loans) {
		StringBuilder bld = new StringBuilder();
		for (ILoan ln : loans) {
			if (bld.length() > 0) bld.append("\n\n");
			bld.append(ln.toString());
		}
		return bld.toString();		
	}

}
