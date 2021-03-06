package gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTable;

import models.Core;
import models.Core.Mode;
import models.JStatusbar;
import models.MyCellEditor;
import models.MyJPasswordPane;
import models.MyTableModel;
import models.PasswordCellRenderer;
import tools.Colors;
import tools.HTMLParser;
import tools.ExportWriter;
import tools.Log;
import tools.MyJFileChooser;
import tools.OS;

public class PWForm extends JXFrame implements WindowListener, PropertyChangeListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar;
    private JXLabel lblForText;
    private MyTableModel model;
    private String pass;
    private File file;
    private JXButton btnSpeichern, btnNeu, btnLschen;
    private JXTable table;
    private JScrollPane scrollPane;
    private JCheckBox chckbxPasswrterAnzeigen, chckbxStopEdit;
    private MouseAdapter tableMouseListener;
    private JMenu mnExport;
    private JPanel panel;
    private JStatusbar statusbar;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMM yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private JXLabel leftLabel;
    private JLabel searchLabel;
    private JTextField searchField;
    private JPanel searchPanel;
    private TableRowSorter<MyTableModel> rowSorter;

    // /**
    // * Create the application.
    // */
    // public PWForm() {
    // initialize();
    // frmPwm.setVisible(true);
    // }

    /**
     * Create the application.
     */
    public PWForm(LinkedList<String> list, String pass, File file) {
        this.file = file;
        this.pass = pass;
        // System.out.println(pass);
        // Log.write("gggdgdgdg");
        initialize();
        Iterator<String> it = list.iterator();
        try {
            while (it.hasNext()) {
                model.addRow(new Object[]{it.next(), it.next(), it.next()});
            }
        } catch (Exception ex) {
            Log.write(ex);
        }
        model.addTableModelListener(e -> btnSpeichern.setEnabled(true));
        getFrame().setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        OS.setLockAndFeel();
        JXFrame.setDefaultLookAndFeelDecorated(true);
        getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getFrame()
                .setIconImage(Toolkit.getDefaultToolkit().getImage(LoginForm.class.getResource("/images/s!logo.png")));
        getFrame().setTitle("diversityPWM");
        getFrame().addWindowListener(this);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        int progWidth = screenWidth / 2;
        int progHeight = screenHeight / 2;
        getFrame().setBounds(progWidth - (progWidth / 2), progHeight - (progHeight / 2), progWidth, progHeight);

        // MENU
        JMenuBar menuBar = new JMenuBar();
        getFrame().setJMenuBar(menuBar);

        JMenu mnDatei = new JMenu("Datei");
        menuBar.add(mnDatei);

        JMenuItem mntmbeenden = new JMenuItem("Beenden");
        mntmbeenden.addActionListener(e -> closeProgram());

        JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter");
        mntmSpeichernUnter.addActionListener(e -> {
            MyJFileChooser fc = new MyJFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("diversITy Dateien (*.dit)", "dit");
            fc.setFileFilter(filter);
            fc.setDialogTitle("Speichern unter");
            int returnVal = fc.showSaveDialog(getFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                if (!file.getName().endsWith(".dit") && !fc.getFileFilter().accept(file)) {
                    if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
                        file = new File(file.getAbsolutePath() + ".dit");
                    }
                }
                saveButtonClicked();
            }
        });

        JMenuItem mntmffnen = new JMenuItem("\u00D6ffnen");
        mntmffnen.addActionListener(e -> {
            MyJFileChooser fc = new MyJFileChooser();
            fc.setCurrentDirectory(file);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("diversITy Dateien (*.dit)", "dit");
            fc.setFileFilter(filter);
            fc.setDialogTitle("�ffnen");
            int returnVal = fc.showOpenDialog(getFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                if (!file.getName().endsWith(".dit") && !fc.getFileFilter().accept(file)) {
                    if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
                        file = new File(file.getAbsolutePath() + ".dit");
                    }
                }
                changeFile();
            }
        });
        mnDatei.add(mntmffnen);
        mnDatei.add(mntmSpeichernUnter);

        JMenu mnImport = new JMenu("Import");
        mnDatei.add(mnImport);

        JMenuItem mntmHtml = new JMenuItem("HTML");
        mntmHtml.addActionListener(e -> {
            try {
                MyJFileChooser fc = new MyJFileChooser();
                FileFilter filter = new FileNameExtensionFilter("HTML Dateien (*.html,*htm)", "htm", "html");
                fc.setFileFilter(filter);
                if (fc.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
                    HTMLParser parser = new HTMLParser(getFrame(), fc.getSelectedFile());
                    parser.execute();
                }
            } catch (Exception ex) {
                Log.write(ex);
            }
        });

        mnImport.add(mntmHtml);

        JMenuItem mntmPasswortndern = new JMenuItem("Passwort \u00E4ndern");
        mntmPasswortndern.addActionListener(e -> {
            MyJPasswordPane pane = new MyJPasswordPane();
            pane.createDialog(getFrame(), "Passwort:").setVisible(true);
            String ss = pane.getPassword();
            if ((ss).equals(pass)) {
                String s = (String) JOptionPane.showInputDialog(getFrame(), "neues Passwort", "Passwort \u00E4ndern",
                        JOptionPane.PLAIN_MESSAGE, null, null, "");
                if (s != null) {
                    if (s.length() >= 6) {
                        pass = s;
                        JOptionPane.showMessageDialog(getFrame(), "Passwort erfolgreich ge\u00E4ndert",
                                "Passwort ge\u00E4ndert", JOptionPane.INFORMATION_MESSAGE);
                        saveButtonClicked();
                    } else {
                        JOptionPane.showMessageDialog(getFrame(), "Passwort zu klein", "Passwort Fehler",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

        });

        mnExport = new JMenu("Export");
        mnDatei.add(mnExport);

        JMenuItem mntmHtml_1 = new JMenuItem("HTML");
        mntmHtml_1.addActionListener(e -> {
            File eFile = null;
            MyJFileChooser fc = new MyJFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML Dateien (*.html,*htm)", "htm", "html");
            fc.setFileFilter(filter);
            fc.setDialogTitle("Exportieren ...");
            int returnVal = fc.showSaveDialog(getFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                eFile = fc.getSelectedFile();
                if ((!eFile.getName().endsWith(".htm") || !eFile.getName().endsWith(".html"))
                        && !fc.getFileFilter().accept(eFile)) {
                    if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
                        eFile = new File(eFile.getAbsolutePath() + ".htm");
                    }
                }
                LinkedList<String> list = new LinkedList<>();
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        list.add(model.getValueAt(i, j).toString());
                        // System.out.println(tr.getLast());
                    }
                }
                ExportWriter.writeHtml(list, eFile);
                leftLabel.setText("Expotiert");
            }
        });
        mnExport.add(mntmHtml_1);

        JMenuItem mntmHtml_2 = new JMenuItem("CSV");
        mntmHtml_2.addActionListener(e -> {
            File eFile = null;
            MyJFileChooser fc = new MyJFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Dateien (*.csv)", "csv");
            fc.setFileFilter(filter);
            fc.setDialogTitle("Exportieren ...");
            int returnVal = fc.showSaveDialog(getFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                eFile = fc.getSelectedFile();
                if ((!eFile.getName().endsWith(".csv"))
                        && !fc.getFileFilter().accept(eFile)) {
                    if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
                        eFile = new File(eFile.getAbsolutePath() + ".csv");
                    }
                }
                LinkedList<LinkedList<String>> list = new LinkedList<>();
                for (int i = 0; i < model.getRowCount(); i++) {
                    LinkedList<String> row = new LinkedList<>();
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        row.add(model.getValueAt(i, j).toString());
                        list.add(row);
                    }
                }
                ExportWriter.writeCSV(list, eFile);
                leftLabel.setText("Expotiert");
            }
        });
        mnExport.add(mntmHtml_2);

        mnDatei.add(mntmPasswortndern);
        mnDatei.add(mntmbeenden);

        // RIGHTCLICK MENU
        JPopupMenu pMenu = new JPopupMenu();
        JMenuItem mntmCopy = new JMenuItem("Kopieren");
        mntmCopy.addActionListener(e -> {
            if (table.getSelectedRow() != -1 && table.getSelectedColumn() != -1) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new StringSelection(table.getStringAt(table.getSelectedRow(), table.getSelectedColumn())),
                        null);
            }
        });
        pMenu.add(mntmCopy);

        getFrame().getRootPaneExt().getContentPane().setLayout(new BorderLayout(0, 0));

        // STATUSBAR
        statusbar = new JStatusbar();
        leftLabel = new JXLabel("gestartet");
        setLblForText(leftLabel);
        statusbar.setLeftComponent(leftLabel);
        final JLabel dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        dateLabel.setText(dateFormat.format(new Date()));
        statusbar.addRightComponent(dateLabel);
        final JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setText(timeFormat.format(new Date()));
        statusbar.addRightComponent(timeLabel);
        setProgressBar(new JProgressBar());
        getStatusbar().addRightComponent(getProgressBar());
        getFrame().getRootPaneExt().getContentPane().add(statusbar, BorderLayout.SOUTH);

        panel = new JPanel();
        panel.addComponentListener(new ComponentListener() {

            // Resize the contentPanel
            @Override
            public void componentResized(ComponentEvent e) {
                scrollPane.setBounds(10, 51, getFrame().getWidth() - 36,
                        getFrame().getHeight() - 151 - getStatusbar().getHeight());
                scrollPane.validate();
                scrollPane.repaint();
                btnLschen.setBounds(160, getFrame().getHeight() - 89 - getStatusbar().getHeight(), 71, 23);
                btnLschen.repaint();
                btnNeu.setBounds(99, getFrame().getHeight() - 89 - getStatusbar().getHeight(), 51, 23);
                btnNeu.repaint();
                btnSpeichern.setBounds(10, getFrame().getHeight() - 89 - getStatusbar().getHeight(), 79, 23);
                btnSpeichern.repaint();
                chckbxPasswrterAnzeigen.setBounds(237, getFrame().getHeight() - 89 - getStatusbar().getHeight(), 131,
                        23);
                chckbxPasswrterAnzeigen.repaint();
                chckbxStopEdit.setBounds(370, getFrame().getHeight() - 89 - getStatusbar().getHeight(), 131, 23);
                chckbxStopEdit.repaint();

                searchPanel.setBounds(10, 10, getFrame().getWidth() - 36, 25);
                searchPanel.repaint();
            }

            @Override
            public void componentHidden(ComponentEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void componentMoved(ComponentEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void componentShown(ComponentEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
        panel.setLayout(null);
        getFrame().getRootPaneExt().getContentPane().add(panel, BorderLayout.CENTER);

        // BUTTONS
        btnSpeichern = new JXButton();
        btnSpeichern.addActionListener(e -> saveButtonClicked());
        btnSpeichern.setBounds(10, panel.getHeight() - 89, 79, 23);
        panel.add(btnSpeichern);
        btnSpeichern.setText("Speichern");
        btnSpeichern.setEnabled(false);

        btnNeu = new JXButton();
        btnNeu.addActionListener(e -> {
            model.addRow(new Object[]{"", "", ""});
            table.changeSelection(table.getRowSorter().convertRowIndexToView(model.getRowCount() - 1), 0, false, false);
            table.requestFocus();
        });
        btnNeu.setText("Neu");
        btnNeu.setBounds(99, panel.getHeight() - 89, 51, 23);
        panel.add(btnNeu);

        btnLschen = new JXButton();
        btnLschen.addActionListener(e -> deleteButtonClicked());
        btnLschen.setText("L\u00F6schen");
        btnLschen.setBounds(160, panel.getHeight() - 89, 71, 23);
        panel.add(btnLschen);

        // CECKBOXES
        chckbxPasswrterAnzeigen = new JCheckBox("Passw\u00F6rter anzeigen?");

        chckbxPasswrterAnzeigen.addActionListener(e -> {
            if (chckbxPasswrterAnzeigen.isSelected()) {
                MyJPasswordPane pane = new MyJPasswordPane();
                pane.createDialog(getFrame(), "Passwort:").setVisible(true);
                String ss = pane.getPassword();
                if ((ss).equals(pass)) {
                    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
                    scrollPane.repaint();
                } else {
                    chckbxPasswrterAnzeigen.setSelected(false);
                }
            } else {
                table.setDefaultRenderer(Object.class, new PasswordCellRenderer());
                scrollPane.repaint();
            }
        });
        chckbxPasswrterAnzeigen.setBounds(237, panel.getHeight() - 89, 131, 23);
        panel.add(chckbxPasswrterAnzeigen);

        chckbxStopEdit = new JCheckBox("Editieren sperren!");

        chckbxStopEdit.addActionListener(e -> {
            if (!chckbxStopEdit.isSelected()) {
                MyJPasswordPane pane = new MyJPasswordPane();
                pane.createDialog(getFrame(), "Passwort:").setVisible(true);
                String ss = pane.getPassword();
                if ((ss).equals(pass)) {
                    table.setEditable(true);
                    btnLschen.setEnabled(true);
                    btnNeu.setEnabled(true);
                    table.addMouseListener(tableMouseListener);
                    scrollPane.repaint();
                } else {
                    chckbxStopEdit.setSelected(true);
                }
            } else {
                table.setEditable(false);
                btnLschen.setEnabled(false);
                btnNeu.setEnabled(false);
                table.removeMouseListener(tableMouseListener);
                scrollPane.repaint();
            }
        });
        chckbxStopEdit.setBounds(370, panel.getHeight() - 89, 131, 23);
        panel.add(chckbxStopEdit);

        // SEARCHPANEL
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBounds(10, 10, getFrame().getWidth() - 36, 25);
        searchLabel = new JLabel("Suche: ");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);
        panel.add(searchPanel);

        // TABLE
        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 40, panel.getHeight() - 36, panel.getWidth() - 151);
        panel.add(scrollPane);

        table = new JXTable();
        table.setSelectionBackground(Colors.TABLESELECTION);
        tableMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    table.changeSelection(table.rowAtPoint(new Point(e.getX(), e.getY())),
                            table.columnAtPoint(new Point(e.getX(), e.getY())), false, false);
                    // System.out.println(Arrays.toString(pMenu.getKeyListeners()));
                    pMenu.show(table, e.getX(), e.getY());
                }
            }
        };
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (table.getCellRenderer(table.getSelectedRow(), table.getSelectedColumn()) instanceof PasswordCellRenderer) {
                    JPasswordField pwf = ((PasswordCellRenderer) table.getCellRenderer(table.getSelectedRow(), table.getSelectedColumn())).getPwField();
                    pwf.setText(Character.toString(e.getKeyChar()));
                    pwf.requestFocus();
                } else {
                    MyCellEditor c = (MyCellEditor) table.getColumnModel().getColumn(table.getSelectedColumn())
                            .getCellEditor();
                    c.getEditorField().setText(Character.toString(e.getKeyChar()));
                    c.getEditorField().requestFocus();
                }
            }
        });
        table.addMouseListener(tableMouseListener);
        table.putClientProperty("terminateEditOnFocusLost", true);
        table.setRowSelectionAllowed(true);
        model = new MyTableModel(new String[][]{}, new String[]{"Ort", "Benutzer", "Passwort"});
        table.setModel(model);
        table.getColumn(2).setCellRenderer(new PasswordCellRenderer());
        //table.setDefaultRenderer(Object.class, new PasswordCellRenderer());
        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "remove row");
        table.getActionMap().put("remove row", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                deleteButtonClicked();
            }

        });
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setCellEditor(new MyCellEditor());
        TableColumn columnt = table.getColumnModel().getColumn(1);
        columnt.setCellEditor(new MyCellEditor());

        scrollPane.setViewportView(table);

        // TableSorter for Search
        rowSorter = new TableRowSorter<>((MyTableModel) table.getModel());
        table.setRowSorter(rowSorter);
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    private void changeFile() {
        if (btnSpeichern.isEnabled()) {
            if (JOptionPane.showConfirmDialog(getFrame(), "\u00C4nderung speichern?", "Speichern",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                onlySave();
            }
        }
        LoginForm lf = new LoginForm(file);
        lf.setVisible(true);
        dispose();
        System.gc();
    }

    private void deleteButtonClicked() {
        if (table.getSelectedRows().length > 0) {
            if (JOptionPane.showConfirmDialog(getFrame(), "Passw\u00F6rter l\u00F6schen?", "L\u00F6schen",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                int[] rows = table.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    model.removeRow(table.convertRowIndexToModel(rows[i] - i));
                }
            }
        }
    }

    protected void saveButtonClicked() {
        LinkedList<String> tr = new LinkedList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                tr.add(model.getValueAt(i, j).toString());
                // System.out.println(tr.getLast());
            }
        }
        btnSpeichern.setEnabled(false);
        Core cr = new Core(file, leftLabel, Mode.SAVE, pass, this);
        cr.setToSave(tr);
        cr.execute();
    }

    protected void onlySave() {
        LinkedList<String> tr = new LinkedList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                tr.add(model.getValueAt(i, j).toString());
                // System.out.println(tr.getLast());
            }
        }
        Core cr = new Core(file, leftLabel, Mode.CLOSESAVE, pass, this);
        cr.setToSave(tr);
        cr.execute();
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        System.gc();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        closeProgram();
    }

    private void closeProgram() {
        if (btnSpeichern.isEnabled()) {
            switch (JOptionPane.showConfirmDialog(getFrame(), "\u00C4nderung speichern?", "Speichern",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                case JOptionPane.YES_NO_OPTION:
                    onlySave();
                    getFrame().dispose();
                    System.exit(0);
                    break;
                case JOptionPane.NO_OPTION:
                    getFrame().dispose();
                    System.exit(0);
                    break;
                default:
            }


        } else {
            getFrame().dispose();
            System.exit(0);
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent e) {
        System.gc();
    }

    /**
     *
     *
     * /~_ _ _|__|_ _ _ _ _ _| (~ _ _|__|_ _ _ \_/(/_ | | (/_| (_|| |(_| _)(/_ |
     * | (/_|
     *
     *
     */

    /**
     * @return the frame
     */
    public PWForm getFrame() {
        return this;
    }

    /**
     * @return the progressBar
     */
    public JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * @param progressBar the progressBar to set
     */
    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * @return the lblForText
     */
    public JXLabel getLblForText() {
        return lblForText;
    }

    /**
     * @param lblForText the lblForText to set
     */
    public void setLblForText(JXLabel lblForText) {
        this.lblForText = lblForText;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();
            getProgressBar().setValue(progress);
        }

    }

    /**
     * @return the model
     */
    public MyTableModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(MyTableModel model) {
        this.model = model;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the btnSpeichern
     */
    public JXButton getBtnSpeichern() {
        return btnSpeichern;
    }

    /**
     * @param btnSpeichern the btnSpeichern to set
     */
    public void setBtnSpeichern(JXButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
    }

    /**
     * @return the btnNeu
     */
    public JXButton getBtnNeu() {
        return btnNeu;
    }

    /**
     * @param btnNeu the btnNeu to set
     */
    public void setBtnNeu(JXButton btnNeu) {
        this.btnNeu = btnNeu;
    }

    /**
     * @return the btnLschen
     */
    public JXButton getBtnLschen() {
        return btnLschen;
    }

    /**
     * @param btnLschen the btnLschen to set
     */
    public void setBtnLschen(JXButton btnLschen) {
        this.btnLschen = btnLschen;
    }

    /**
     * @return the table
     */
    public JXTable getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(JXTable table) {
        this.table = table;
    }

    /**
     * @return the scrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * @param scrollPane the scrollPane to set
     */
    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    /**
     * @return the chckbxPasswrterAnzeigen
     */
    public JCheckBox getChckbxPasswrterAnzeigen() {
        return chckbxPasswrterAnzeigen;
    }

    /**
     * @param chckbxPasswrterAnzeigen the chckbxPasswrterAnzeigen to set
     */
    public void setChckbxPasswrterAnzeigen(JCheckBox chckbxPasswrterAnzeigen) {
        this.chckbxPasswrterAnzeigen = chckbxPasswrterAnzeigen;
    }

    /**
     * @return the chckbxStopEdit
     */
    public JCheckBox getChckbxStopEdit() {
        return chckbxStopEdit;
    }

    /**
     * @param chckbxStopEdit the chckbxStopEdit to set
     */
    public void setChckbxStopEdit(JCheckBox chckbxStopEdit) {
        this.chckbxStopEdit = chckbxStopEdit;
    }

    /**
     * @return the tableMouseListener
     */
    public MouseAdapter getTableMouseListener() {
        return tableMouseListener;
    }

    /**
     * @param tableMouseListener the tableMouseListener to set
     */
    public void setTableMouseListener(MouseAdapter tableMouseListener) {
        this.tableMouseListener = tableMouseListener;
    }

    /**
     * @return the mnExport
     */
    public JMenu getMnExport() {
        return mnExport;
    }

    /**
     * @param mnExport the mnExport to set
     */
    public void setMnExport(JMenu mnExport) {
        this.mnExport = mnExport;
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * @return the statusbar
     */
    public JStatusbar getStatusbar() {
        return statusbar;
    }

    /**
     * @param statusbar the statusbar to set
     */
    public void setStatusbar(JStatusbar statusbar) {
        this.statusbar = statusbar;
    }

    /**
     * @return the leftLabel
     */
    public JXLabel getLeftLabel() {
        return leftLabel;
    }

    /**
     * @param leftLabel the leftLabel to set
     */
    public void setLeftLabel(JXLabel leftLabel) {
        this.leftLabel = leftLabel;
    }

    /**
     * @return the dateFormat
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * @return the timeFormat
     */
    public SimpleDateFormat getTimeFormat() {
        return timeFormat;
    }
}
