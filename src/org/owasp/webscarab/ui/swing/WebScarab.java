/*
 * WebScarab.java
 *
 * Created on July 13, 2003, 7:11 PM
 */

package org.owasp.webscarab.ui.swing;

import org.owasp.webscarab.plugin.proxy.Proxy;
import org.owasp.webscarab.plugin.spider.Spider;
import org.owasp.webscarab.ui.Framework;
import org.owasp.webscarab.model.StoreException;

import org.owasp.webscarab.ui.swing.*;
import org.owasp.webscarab.ui.swing.proxy.ProxyPanel;
import org.owasp.webscarab.ui.swing.spider.SpiderPanel;

import org.owasp.webscarab.backend.FileSystemStore;

import java.util.ArrayList;
import java.io.File;

import javax.swing.JFileChooser;

/**
 *
 * @author  rdawes
 */
public class WebScarab extends javax.swing.JFrame {
    
    private Framework _framework;
    private ArrayList _plugins;
    private SwingPlugin[] _pluginArray = new SwingPlugin[0];
    private File _defaultDir = null;
    
    /** Creates new form WebScarab */
    public WebScarab(Framework framework) {
        _framework = framework;
        initComponents();
        
        // should instantiate a listener for Logger messages here, and insert it into the 
        // bottom part of the split pane . . .
        
        addPlugin(new ConversationLog(_framework));

        Proxy proxy = new Proxy(_framework);
        new Thread(proxy).start();
        _framework.addPlugin(proxy);
        addPlugin(new ProxyPanel(proxy));
        
        Spider spider = new Spider(_framework);
        _framework.addPlugin(spider);
        addPlugin(new SpiderPanel(spider));

    }

    public void addPlugin(SwingPlugin plugin) {
        if (_plugins == null) {
            _plugins = new ArrayList();
        }
        _plugins.add(plugin);
        _pluginArray = (SwingPlugin[]) _plugins.toArray(_pluginArray);
        mainTabbedPane.add(plugin.getPanel(), plugin.getPluginName());
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        mainSplitPane = new javax.swing.JSplitPane();
        mainTabbedPane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenuItem();
        saveConfigMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("WebScarab");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        mainSplitPane.setBorder(null);
        mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(1.0);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setLeftComponent(mainTabbedPane);

        logTextArea.setBackground(new java.awt.Color(204, 204, 204));
        logTextArea.setEditable(false);
        jScrollPane1.setViewportView(logTextArea);

        mainSplitPane.setRightComponent(jScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(mainSplitPane, gridBagConstraints);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");
        newMenuItem.setMnemonic('N');
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(newMenuItem);

        openMenuItem.setMnemonic('O');
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(openMenuItem);

        exitMenuItem.setMnemonic('X');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        mainMenuBar.add(fileMenu);

        toolsMenu.setMnemonic('T');
        toolsMenu.setText("Tools");
        optionsMenuItem.setText("Options");
        toolsMenu.add(optionsMenuItem);

        saveConfigMenuItem.setText("Save Configuration");
        toolsMenu.add(saveConfigMenuItem);

        mainMenuBar.add(toolsMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");
        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-640)/2, (screenSize.height-480)/2, 640, 480);
    }//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // Should check to see if the model has been saved (somewhere where the 
        // user will find it, offer to rename the base directory to something
        // useful, before exiting.
        saveSessionData();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JFileChooser jfc = new JFileChooser(_defaultDir);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Choose a directory that contains a previous session");
        int returnVal = jfc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            String dir = file.toString() + System.getProperty("file.separator");
            try {
                if (FileSystemStore.isExistingSession(dir)) {
                    FileSystemStore store = new FileSystemStore(dir);
                    _framework.setSessionStore(store);
                } else {
                    System.err.println("No session found in " + dir);
                }
            } catch (StoreException se) {
                // pop up an alert dialog box or something
                System.err.println("Error loading session : " + se);
            }
        }
        _defaultDir = jfc.getCurrentDirectory();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        JFileChooser jfc = new JFileChooser(_defaultDir);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Select a directory to write the session into");
        int returnVal = jfc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            String dir = file.toString() + System.getProperty("file.separator");
            try {
                if (FileSystemStore.isExistingSession(dir)) {
                    System.err.println(dir + " is an existing session!");
                } else {
                    FileSystemStore store = new FileSystemStore(dir);
                    store.init();
                    _framework.setSessionStore(store);
                }
            } catch (StoreException se) {
                // pop up an alert dialog box or something
                System.err.println("Error loading session : " + se);
            }
        }
        _defaultDir = jfc.getCurrentDirectory();
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // FIXME
        System.out.println("Help/About not implemented yet!");
        System.out.println("OWASP WebScarab - part of the Open Web Application Security Project");
        System.out.println("See http://www.owasp.org/");
        System.out.println("Coders : Rogan Dawes (rdawes at telkomsa.net / rdawes at deloitte.co.za)");
        System.out.println("         Ingo Struck (ingo at ingostruck.de)");
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        // Should check to see if the model has been saved (somewhere where the 
        // user will find it, offer to rename the base directory to something
        // useful, before exiting.
        saveSessionData();
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    private void saveSessionData() {
        try {
            _framework.saveSessionData();
        } catch (StoreException se) {
            // pop up an alert dialog box or something
            System.err.println("Error saving session : " + se);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Framework fw = new Framework();
        new WebScarab(fw).show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenuItem saveConfigMenuItem;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables
    
}
