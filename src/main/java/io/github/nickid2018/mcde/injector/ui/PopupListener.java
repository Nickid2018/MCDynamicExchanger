package io.github.nickid2018.mcde.injector.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class PopupListener extends MouseAdapter {

    private final JPopupMenu popupMenu;

    public PopupListener(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    }
}
