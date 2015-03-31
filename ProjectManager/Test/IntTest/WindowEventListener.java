package IntTest;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Sam on 2015-03-31.
 */
public class WindowEventListener implements WindowListener {

    private boolean _has_been_closed;

    public WindowEventListener(){
        _has_been_closed = false;
    }

    public boolean getHasBeenClosed(){
        return _has_been_closed;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        _has_been_closed = true;
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
