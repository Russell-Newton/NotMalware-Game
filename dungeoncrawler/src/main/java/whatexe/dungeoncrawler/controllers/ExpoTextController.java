package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import whatexe.dungeoncrawler.ManagedController;

import java.util.Map;

public class ExpoTextController extends ManagedController {

    private static final String[] EXPOSITIONS = {
        "You are a piece of malware, recently installed on this computer under the "
                    + "guise of "
                    + "NotMalware.exe, a \"totally legitimate\" file passed around by a "
                    + "phishing "
                    + "scam.\nIt is now your task to infect and take over this computer."
                    + "\nThis will not be an easy task...\n\nYour first target is the "
                    + "computer's Storage. Infecting its files is the first step in "
                    + "embedding "
                    + "your presence in the whole of the machine."
                    + "\n\n\n\nGo forth and infect...",
        "The Storage is laid bare, infected by you.\nBy now, the user of this "
                    + "computer has "
                    + "probably noticed something is amiss. Perhaps their antivirus has "
                    + "issued a "
                    + "warning about NotMalware.exe.\n\nYour next target is the Registry. "
                    + "Infecting this will allow you to influence the behaviors of other "
                    + "programs"
                    + " on the computer, including the Operating System."
                    + "\n\n\n\nGo forth and infect...",
        "With the Registry infected, this computer is almost entirely under your "
                    + "control.\n"
                    + "You have crippled other programs' access to important files."
                    + "\n\nOnly one obstacle remains: the Operating System. Your final "
                    + "target, "
                    + "control over this will offer you free reign over even the hardware"
                    + " drivers"
                    + ". You could hijack the GPU for crypto mining if you wanted. Maybe "
                    + "even "
                    + "send NotMalware.exe to more computers. Whatever the end,"
                    + "\n\n\n\nGo forth and infect..." };

    @FXML
    protected Label expoLabel;
    @FXML
    protected Label pressAnyKeyLabel;

    @Override
    public void init(Map<String, Object> loadParameters) {
        expoLabel.setText(EXPOSITIONS[(int) loadParameters.get("depth")]);

        init();
    }

    @Override
    public void init() {
        pressAnyKeyLabel.setVisible(false);
    }

    @Override
    public void setState(Map<String, Object> stateParameters) {
        if ((boolean) stateParameters.get("ready")) {
            pressAnyKeyLabel.setVisible(true);
        }
    }
}
