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
        
        reader_.addListener(this);
	}
	
	public void initialise() {
		previous_ = display_.getDisplay();
		display_.setDisplay((JPanel) ui_, "Borrow UI");
		setState(EBorrowState.INITIALIZED);
	}
	
	public void close() {
		display_.setDisplay(previous_, "Main Menu");
	}

	@Override
	public void cardSwiped(int memberID) {
	    if (state_ != EBorrowState.INITIALIZED) {
            throw new RuntimeException("BorrowUC_CTL: cardSwiped : illegal operation in state: " + state_);
        }
	    borrower_ = memberDAO_.getMemberByID(memberID);
	    	    
	    if (borrower_ != null) {
            boolean dueLoans  = borrower_.hasOverDueLoans();
            boolean loanLimit = borrower_.hasReachedLoanLimit();
            boolean fineLimit = borrower_.hasReachedFineLimit();
            
	        if (borrower_.hasFinesPayable()) {
	            ui_.displayOutstandingFineMessage(borrower_.getFineAmount());
	        }	        
	        if (dueLoans || loanLimit || fineLimit) {
                setState(EBorrowState.BORROWING_RESTRICTED);
	            
	            if (dueLoans)  ui_.displayOverDueMessage();
	            if (loanLimit) ui_.displayAtLoanLimitMessage();
	            if (fineLimit) ui_.displayOverFineLimitMessage(borrower_.getFineAmount());
            } else {
                setState(EBorrowState.SCANNING_BOOKS);
            }
            ui_.displayMemberDetails(borrower_.getID(), 
                    borrower_.getFirstName() + " " +
                    borrower_.getLastName(),
                    borrower_.getContactPhone());
            
	        ui_.displayExistingLoan(buildLoanListDisplay(borrower_.getLoans()));
        } else {
            ui_.displayErrorMessage(String.format("Member ID %d not found", memberID));
        }
	}
	
	
	
	@Override
	public void bookScanned(int barcode) {
		throw new RuntimeException("Not implemented yet");
	}

	
	private void setState(EBorrowState state) {
        
	    switch (state) {
            case INITIALIZED:
                ui_.setState(state);
                reader_.setEnabled(true);
                scanner_.setEnabled(false);
                break;
            case BORROWING_RESTRICTED:
                reader_.setEnabled(false);
                scanner_.setEnabled(false);
                break;
            case SCANNING_BOOKS:
                reader_.setEnabled(false);
                scanner_.setEnabled(true);
                bookList_ = new ArrayList<IBook>();
                loanList_ = new ArrayList<ILoan>();
                scanCount_ = borrower_.getLoans().size();
                break;
            case CANCELLED:
                reader_.setEnabled(false);
                scanner_.setEnabled(false);
                close();
                break;
            default:
                throw new RuntimeException("Unknown state : " + state);
        }

        ui_.setState(state);
	    state_ = state;
	    System.out.println("State set to: " + state);
	}

	@Override
	public void cancelled() {
        setState(EBorrowState.CANCELLED);
	}
	
	@Override
	public void scansCompleted() {
        setState(EBorrowState.CONFIRMING_LOANS);
	}

	@Override
	public void loansConfirmed() {
        setState(EBorrowState.COMPLETED);
	}

	@Override
	public void loansRejected() {
        setState(EBorrowState.SCANNING_BOOKS);
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
