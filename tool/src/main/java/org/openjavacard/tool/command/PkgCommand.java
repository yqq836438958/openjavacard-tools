package org.openjavacard.tool.command;

import org.openjavacard.gp.client.GPCard;
import org.openjavacard.gp.client.GPContext;
import org.openjavacard.packaging.manager.OJCPackageContext;
import org.openjavacard.packaging.manager.OJCPackageManager;

import javax.smartcardio.CardException;
import java.io.PrintStream;

public abstract class PkgCommand extends GPCommand {

    OJCPackageContext mPkgContext;

    public PkgCommand(GPContext context) {
        super(context);
    }

    @Override
    public void run() {
        initializePackaging();
        super.run();
    }

    private void initializePackaging() {
        PrintStream os = System.out;
        os.println("INITIALIZE PACKAGING");
        os.println();
        mPkgContext = new OJCPackageContext();
    }

    @Override
    protected void performOperation(GPContext context, GPCard card) throws CardException {
        OJCPackageManager manager = new OJCPackageManager(mPkgContext, card);
        performOperation(manager);
    }

    protected abstract void performOperation(OJCPackageManager manager) throws CardException;

}