package atm.hardware;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CashDispenser {
    private final DenominationDispenser headChain;
    private final Lock hardwareLock = new ReentrantLock();

    public CashDispenser() {
        headChain = new DenominationDispenser(500, 10);      // ₹5000 in 500s
        DenominationDispenser d100 = new DenominationDispenser(100, 10); // ₹1000 in 100s
        DenominationDispenser d50 = new DenominationDispenser(50, 10);   // ₹500 in 50s
        DenominationDispenser d20 = new DenominationDispenser(20, 20);   // ₹400 in 20s
        DenominationDispenser d10 = new DenominationDispenser(10, 50);   // ₹500 in 10s

        headChain.setNext(d100);
        d100.setNext(d50);
        d50.setNext(d20);
        d20.setNext(d10);
    }

    public boolean dispenseCash(int requestedAmount) {
        hardwareLock.lock();
        try {
            if (requestedAmount % 10 != 0) {
                System.out.println("Hardware Error: Amount must be a multiple of 10.");
                return false;
            }

            Map<Integer, Integer> proposal = new LinkedHashMap<>();
            if (!headChain.canDispense(requestedAmount, proposal)) {
                System.out.println("Hardware Error: Exact denominations unavailable for ₹" + requestedAmount);
                return false;
            }

            System.out.println("--- Dispensing Physical Cash ---");
            headChain.executeDispense(proposal);
            System.out.println("--------------------------------");
            return true;

        } finally {
            hardwareLock.unlock();
        }
    }
}
