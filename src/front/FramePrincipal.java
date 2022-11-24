/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package front;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import ObjectosNegocio.Actividades;
import javax.swing.table.DefaultTableModel;
import DAO.PendientesDAO;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

/**
 *
 * @author danie
 */
public class FramePrincipal extends javax.swing.JFrame {

    private final int TIEMPO_DESCANSO = 1;
    private final int TIEMPO_TRABAJO = 2;
    private final int TIEMPO_DESCANSO_LARGO = 3;
    private final int TIEMPO_PURO_CERO = 4;
    PendientesDAO PendienteControl = new PendientesDAO();

    /**
     * Creates new form FramePrincipal
     */
    public FramePrincipal() {
        initComponents();
        setLocationRelativeTo(null);
        lblindicador.setVisible(false);
        btnContinuar.setVisible(false);
        btnPausar.setEnabled(false);
        t = new Timer(1, acciones);
        timerLB = new Timer(500, accionesLblParpadeante);
        timerBeep = new Timer(500, accionesBeepTerminacion);
        this.cargarTabla();
        btnEmpezar.setEnabled(checaProgreso());
        btnRestablecer.setEnabled(false);
        btnOmitir.setVisible(false);
        actualizaValoresTiempos();
        actualizarLabelTiempo();
        actualizaLblContadorDescansos();
    }

    private boolean esDescanso = false;
    private boolean esDescansoLargo = false;
    private byte contPomodoros = 0;
    private boolean banderaPausa = true;
    private Timer t;
    private Timer timerLB;
    private Timer timerBeep;
    private int m, s, cs;
    private int contParpadeante;
    private int contNotificacion;

    private ActionListener acciones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (cs > 0) {
                cs--;
            } else {
                if (s > 0) {
                    s--;
                    cs = 99;
                } else if (m > 0) {
                    m--;
                    s = 59;
                    cs = 99;
                }
            }
            if (s <= 5) {
                mostrarIndicador();
            }
            actualizarLabelTiempo();
        }
    };

    private boolean checaProgreso() {
        return tblProgreso.getModel().getRowCount() > 0;
    }

    private void setTiempos(int tipo) {
        switch (tipo) {
            case TIEMPO_DESCANSO -> {
                m = 0;
                s = 10;
                cs = 0;
            }
            case TIEMPO_TRABAJO -> {
                m = 0;
                s = 20;
                cs = 0;
            }
            case TIEMPO_DESCANSO_LARGO -> {
                m = 0;
                s = 15;
                cs = 0;
            }
            case TIEMPO_PURO_CERO -> {
                m = 0;
                s = 0;
                cs = 0;
            }
        }
    }

    private void mostrarIndicador() {
        if (s == 0 && m == 0 && cs == 0) {
            lblindicador.setText("Se ha acabado el tiempo");
            btnContinuar.setVisible(true);
            timerBeep.start();
            btnPausar.setEnabled(false);
            if (!esDescanso) {
                btnOmitir.setVisible(true);
            }
        } else {
            lblindicador.setVisible(true);
            lblindicador.setText("Faltan " + s + " segundos para acabar");
        }
    }

    private void ocultarIndicador() {
        lblindicador.setVisible(false);
        btnContinuar.setVisible(false);
    }

    private void actualizarLabelTiempo() {
        String tiempo = (m <= 9 ? "0" : "") + m + ":" + (s <= 9 ? "0" : "") + s + ":" + (cs <= 9 ? "0" : "") + cs;
        lblTiempo.setText(tiempo);
    }

    private void actualizaValoresTiempos() {
        if (esDescanso) {
            if (esDescansoLargo) {
                setTiempos(TIEMPO_DESCANSO_LARGO);
                esDescansoLargo = false;
                contPomodoros = 0;
            } else {
                setTiempos(TIEMPO_DESCANSO);
            }
        } else {
            setTiempos(TIEMPO_TRABAJO);
        }
    }

    private void restableceTiempo() {
        esDescanso = false;
        banderaPausa = true;
        contPomodoros = 0;
        actualizaValoresTiempos();
        actualizarLabelTiempo();
        actualizaLblContadorDescansos();
        btnEmpezar.setEnabled(checaProgreso());
        btnPausar.setEnabled(false);
        btnRestablecer.setEnabled(false);
        btnOmitir.setVisible(false);
        ocultarIndicador();
        if (timerLB.isRunning()) {
            timerLB.stop();
            lblTiempo.setVisible(true);
            btnPausar.setText("Pausar");
            banderaPausa = true;
        }
        if (timerBeep.isRunning()) {
            timerBeep.stop();
        }
    }

    private void omitirDescanso() {
        if (!esDescanso) {
            if (contPomodoros < 5) {
                contPomodoros += 1;
            }
        }
        if (contPomodoros == 5) {
            contPomodoros = 0;
        }
        timerBeep.stop();
        actualizaLblContadorDescansos();
        btnEmpezarActionPerformed(null);
    }

    private ActionListener accionesBeepTerminacion = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (contNotificacion % 2 == 0) {
                Toolkit.getDefaultToolkit().beep();
            }
            contNotificacion++;
        }
    };

    private ActionListener accionesLblParpadeante = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (contParpadeante % 2 == 0) {
                lblTiempo.setVisible(false);
            } else {
                lblTiempo.setVisible(true);
            }
            contParpadeante++;
        }
    };

    public void EliminarPendientes() {
        int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere Eliminar esta actividad", "info", JOptionPane.YES_NO_OPTION);
        try {
            if (opcion == JOptionPane.YES_OPTION) {
                DefaultTableModel dtmPendiente = (DefaultTableModel) tblPendientes.getModel();
                Actividades tareaPendiente = (Actividades) dtmPendiente.getValueAt(tblPendientes.getSelectedRow(), 0);
                if (tareaPendiente != null) {
                    this.PendienteControl.Eliminar(tareaPendiente);
                    JOptionPane.showMessageDialog(this, "La tarea se eliminó con exito.,",
                            "Notificación.", JOptionPane.INFORMATION_MESSAGE);
                    this.cargarTabla();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "No sea seleccionado una tarea", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void EliminarProgreso() {
        int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere Eliminar esta actividad", "info", JOptionPane.YES_NO_OPTION);
        try {
            if (opcion == JOptionPane.YES_OPTION) {
                DefaultTableModel dtmPendiente = (DefaultTableModel) tblProgreso.getModel();
                Actividades tareaProgreso = (Actividades) dtmPendiente.getValueAt(tblProgreso.getSelectedRow(), 0);
                if (tareaProgreso != null) {
                    this.PendienteControl.Eliminar(tareaProgreso);
                    JOptionPane.showMessageDialog(this, "La tarea se eliminó con exito.,",
                            "Notificación.", JOptionPane.INFORMATION_MESSAGE);
                    this.cargarTabla();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "No sea seleccionado una tarea", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        tblPendientes1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPendientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProgreso = new javax.swing.JTable();
        btnPendienteAProgreso = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnPendienteATerminado = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblTerminado = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btn_eliminar1 = new javax.swing.JButton();
        btn_eliminar2 = new javax.swing.JButton();
        btnEditarPendiente = new javax.swing.JButton();
        btnEditarProgreso = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        Btn_Agregar_Tarea = new javax.swing.JButton();
        lblindicador = new javax.swing.JLabel();
        btnContinuar = new javax.swing.JButton();
        lblContadorDescansos = new javax.swing.JLabel();
        btnEmpezar = new javax.swing.JButton();
        btnRestablecer = new javax.swing.JButton();
        btnPausar = new javax.swing.JButton();
        btnOmitir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblTiempo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        tblPendientes1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Actividad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblPendientes1);
        if (tblPendientes1.getColumnModel().getColumnCount() > 0) {
            tblPendientes1.getColumnModel().getColumn(0).setResizable(false);
        }

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pomodoro");
        setResizable(false);

        jPanel1.setLayout(null);

        tblPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Actividad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPendientes);
        if (tblPendientes.getColumnModel().getColumnCount() > 0) {
            tblPendientes.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(480, 180, 320, 402);

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 94, 166));
        jLabel1.setText("Tareas Terminada");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(1310, 150, 162, 21);

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(12, 94, 166));
        jLabel2.setText("Tareas Pendientes");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(560, 150, 170, 21);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(452, 402));

        tblProgreso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Actividad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblProgreso);
        if (tblProgreso.getColumnModel().getColumnCount() > 0) {
            tblProgreso.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel1.add(jScrollPane4);
        jScrollPane4.setBounds(820, 180, 370, 400);

        btnPendienteAProgreso.setText("-->");
        btnPendienteAProgreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendienteAProgresoActionPerformed(evt);
            }
        });
        jPanel1.add(btnPendienteAProgreso);
        btnPendienteAProgreso.setBounds(480, 620, 72, 23);

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(12, 94, 166));
        jLabel3.setText("Tareas en Progreso");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(930, 150, 180, 21);

        btnPendienteATerminado.setText("-->");
        btnPendienteATerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendienteATerminadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnPendienteATerminado);
        btnPendienteATerminado.setBounds(1120, 620, 72, 23);

        tblTerminado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Actividad", "Fecha - Hora"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblTerminado);

        jPanel1.add(jScrollPane5);
        jScrollPane5.setBounds(1210, 180, 330, 400);

        jButton1.setText("<--");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(820, 620, 72, 23);

        btn_eliminar1.setText("Eliminar  Pendiente");
        btn_eliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar1ActionPerformed(evt);
            }
        });
        jPanel1.add(btn_eliminar1);
        btn_eliminar1.setBounds(570, 620, 132, 23);

        btn_eliminar2.setText("Eliminar  Progreso");
        btn_eliminar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar2ActionPerformed(evt);
            }
        });
        jPanel1.add(btn_eliminar2);
        btn_eliminar2.setBounds(900, 620, 126, 23);

        btnEditarPendiente.setText("Modificar");
        btnEditarPendiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarPendienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditarPendiente);
        btnEditarPendiente.setBounds(720, 620, 81, 23);

        btnEditarProgreso.setText("Modificar");
        btnEditarProgreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProgresoActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditarProgreso);
        btnEditarProgreso.setBounds(1030, 620, 81, 23);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        Btn_Agregar_Tarea.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Btn_Agregar_Tarea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-add-40.png"))); // NOI18N
        Btn_Agregar_Tarea.setText("Agregar Tarea");
        Btn_Agregar_Tarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Agregar_TareaActionPerformed(evt);
            }
        });

        lblindicador.setForeground(new java.awt.Color(255, 51, 51));
        lblindicador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-sirena-19.png"))); // NOI18N
        lblindicador.setText("INDICADOR");
        lblindicador.setToolTipText("");

        btnContinuar.setText("Continuar");
        btnContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinuarActionPerformed(evt);
            }
        });

        lblContadorDescansos.setText("Contador de Descansos");

        btnEmpezar.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        btnEmpezar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-play-25.png"))); // NOI18N
        btnEmpezar.setText("Empezar");
        btnEmpezar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpezarActionPerformed(evt);
            }
        });

        btnRestablecer.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        btnRestablecer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-grabar-25.png"))); // NOI18N
        btnRestablecer.setText("Restablecer");
        btnRestablecer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestablecerActionPerformed(evt);
            }
        });

        btnPausar.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        btnPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-pausa-25.png"))); // NOI18N
        btnPausar.setText("Pausar");
        btnPausar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPausarActionPerformed(evt);
            }
        });

        btnOmitir.setText("Omitir");
        btnOmitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOmitirActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lblTiempo.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        lblTiempo.setText("00:00:00");
        lblTiempo.setToolTipText("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 217, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(lblTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(lblTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(8, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnEmpezar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRestablecer))
                            .addComponent(lblindicador, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOmitir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnContinuar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPausar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(32, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Btn_Agregar_Tarea)
                            .addComponent(lblContadorDescansos, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblContadorDescansos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Btn_Agregar_Tarea, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEmpezar)
                    .addComponent(btnRestablecer)
                    .addComponent(btnPausar))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnContinuar)
                    .addComponent(lblindicador))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOmitir)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(20, 240, 450, 310);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Please .png"))); // NOI18N
        jLabel4.setText("jLabel4");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(0, 0, 1560, 750);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1558, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 748, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEmpezarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpezarActionPerformed
        actualizaValoresTiempos();
        t.start();
        btnEmpezar.setEnabled(false);
        btnPausar.setEnabled(true);
        btnRestablecer.setEnabled(true);
        btnContinuar.setVisible(false);
        btnOmitir.setVisible(false);
        lblindicador.setVisible(false);
    }//GEN-LAST:event_btnEmpezarActionPerformed

    private void btnPausarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPausarActionPerformed
        if (banderaPausa) {
            t.stop();
            timerLB.start();
            btnPausar.setText("Reanudar");
            btnPausar.setIcon(new ImageIcon("src/img/icons8-play-25.png"));
            banderaPausa = false;
        } else {
            timerLB.stop();
            lblTiempo.setVisible(true);
            t.restart();
            btnPausar.setText("Pausar");
            btnPausar.setIcon(new ImageIcon("src/img/icons8-pausa-25.png"));
            banderaPausa = true;
        }
    }//GEN-LAST:event_btnPausarActionPerformed

    public void cargarTabla() {
        this.PendienteControl.crearConexion();
        List<Actividades> list = this.PendienteControl.consultar();
        System.out.println(list);

        //--------------------------------
        // CARGA TABLA EN PEDIENTES
        //--------------------------------
        DefaultTableModel model = (DefaultTableModel) tblPendientes.getModel();
        model.setRowCount(0);
        list.forEach(pendiente -> {
            if (pendiente.getEstado().equals("pendiente")) {
                model.addRow(new Object[]{pendiente});
            }
        });

        //--------------------------------
        // CARGA TABLA EN PROGRESO
        //--------------------------------
        DefaultTableModel modelProgreso = (DefaultTableModel) tblProgreso.getModel();
        modelProgreso.setRowCount(0);
        list.forEach(pendiente -> {
            if (pendiente.getEstado().equals("progreso")) {
                modelProgreso.addRow(new Object[]{pendiente});
            }
        });

        //--------------------------------
        // CARGA TABLA EN TERMINADO
        //--------------------------------
        DefaultTableModel modelTerminada = (DefaultTableModel) tblTerminado.getModel();
        modelTerminada.setRowCount(0);
        //Ordenamiento de la lista
        List<Actividades> terminados = list.stream()
                .filter(act -> act.getEstado().equals("terminado"))
                .sorted((c1, c2) -> {
                    return c2.getFechaterminacion().compareTo(c1.getFechaterminacion());
                })
                .toList();
        terminados.forEach(pendiente -> {
            //Se modifico para imprimir la fecha
            modelTerminada.addRow(new Object[]{pendiente, convertirFecha(pendiente.getFechaterminacion())});
        });
    }

    //Comvertir la fecha en string
    public String convertirFecha(Date fecha) {
        String pattern = "yyyy-MM-dd hh:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String fechaString = sdf.format(fecha);
        return fechaString;
    }

    private void Btn_Agregar_TareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Agregar_TareaActionPerformed

        Date fecha = new Date(Calendar.getInstance().getTimeInMillis());
        String actividad = JOptionPane.showInputDialog(rootPane, "Tarea", "Agregar", JOptionPane.QUESTION_MESSAGE);
        if (actividad != null) {
            List<Actividades> list = this.PendienteControl.consultar();
            if (actividad.isEmpty() || actividad.isBlank()) {
                JOptionPane.showMessageDialog(rootPane, "La actividad esta vacia", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (actividad.length() > 100) {
                JOptionPane.showMessageDialog(rootPane, "Logitud mayor a 100", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Actividades New_actividad = new Actividades(actividad, "pendiente", fecha);
            if (list.contains(New_actividad)) {
                JOptionPane.showMessageDialog(rootPane, "La actividad ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PendienteControl.agregar(New_actividad);
            JOptionPane.showMessageDialog(rootPane, "La actividad se agrego con exito!! ", "Listo", JOptionPane.INFORMATION_MESSAGE);
            this.cargarTabla();
        }
    }//GEN-LAST:event_Btn_Agregar_TareaActionPerformed

    private void btnContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinuarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Seguro que desea apagar la alarma y continuar con el siguiente temporizador?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.NO_OPTION) {
            return;
        }
        timerBeep.stop();
        if (!esDescanso) {
            if (contPomodoros < 5) {
                contPomodoros += 1;
            }
        }
        actualizaLblContadorDescansos();
        if (contPomodoros == 5) {
            esDescansoLargo = true;
        }
        esDescanso = !esDescanso;
        btnEmpezarActionPerformed(null);
    }//GEN-LAST:event_btnContinuarActionPerformed

    private void btnPendienteAProgresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendienteAProgresoActionPerformed
        int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere realizar esta actividad", "info", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                DefaultTableModel dtmPendiente = (DefaultTableModel) tblPendientes.getModel();
                Actividades tareaPendiente = (Actividades) dtmPendiente.getValueAt(tblPendientes.getSelectedRow(), 0);
                tareaPendiente.setEstado("progreso");
                this.PendienteControl.modificar(tareaPendiente);
                this.cargarTabla();
                if (!timerLB.isRunning() && !t.isRunning()) {
                    btnEmpezar.setEnabled(checaProgreso());
                } else {
                    btnEmpezar.setEnabled(false);
                }
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_btnPendienteAProgresoActionPerformed

    private void btnPendienteATerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendienteATerminadoActionPerformed
        int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere terminar esta actividad", "info", JOptionPane.YES_NO_OPTION);
        try {
            if (opcion == JOptionPane.YES_OPTION) {
                DefaultTableModel dtmPendiente = (DefaultTableModel) tblProgreso.getModel();
                Actividades tareaPendiente = (Actividades) dtmPendiente.getValueAt(tblProgreso.getSelectedRow(), 0);
                tareaPendiente.setEstado("terminado");
                tareaPendiente.setFechaterminacion(new Date(Calendar.getInstance().getTimeInMillis()));
                this.PendienteControl.modificar(tareaPendiente);
                this.cargarTabla();
                btnEmpezar.setEnabled(checaProgreso());
                if (!checaProgreso()) {
                    resetearTodoTimer();
                }
                JOptionPane.showMessageDialog(rootPane, "La actividad se termino con exito!! ", "Listo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "No sea seleccionado una tarea", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnPendienteATerminadoActionPerformed

    private void btnRestablecerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestablecerActionPerformed
        int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere restablecer el tiempo?", "info", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            t.stop();
            timerBeep.stop();
            timerLB.stop();
            restableceTiempo();
        }
    }//GEN-LAST:event_btnRestablecerActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere pausar esta actividad?", "info", JOptionPane.YES_NO_OPTION);
        try {
            if (opcion == JOptionPane.YES_OPTION) {
                DefaultTableModel dtmPendiente = (DefaultTableModel) tblProgreso.getModel();
                Actividades tareaPendiente = (Actividades) dtmPendiente.getValueAt(tblProgreso.getSelectedRow(), 0);
                tareaPendiente.setEstado("pendiente");
                this.PendienteControl.modificar(tareaPendiente);
                this.cargarTabla();
                btnEmpezar.setEnabled(checaProgreso());
                if (!checaProgreso()) {
                    resetearTodoTimer();
                }
                JOptionPane.showMessageDialog(rootPane, "La actividad regreso a pendiente con exito!! ", "Listo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "No sea seleccionado una tarea", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnOmitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOmitirActionPerformed
        int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere omitir el descanso que sigue y pasar directamente al siguiente pomodoro?", "info", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            omitirDescanso();
        }
    }//GEN-LAST:event_btnOmitirActionPerformed

    private void btn_eliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar1ActionPerformed
        this.EliminarPendientes();
        cargarTabla();
        
    }//GEN-LAST:event_btn_eliminar1ActionPerformed

    private void btn_eliminar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar2ActionPerformed
        this.EliminarProgreso();
        cargarTabla();
        if (!checaProgreso()) {
            restableceTiempo();
            resetearTodoTimer();
        }

    }//GEN-LAST:event_btn_eliminar2ActionPerformed

    private void btnEditarPendienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPendienteActionPerformed
        Actividades tareaPendiente;
        try {
            DefaultTableModel dtmPendiente = (DefaultTableModel) tblPendientes.getModel();
            tareaPendiente = (Actividades) dtmPendiente.getValueAt(tblPendientes.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "No sea seleccionado una tarea", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String nuevoNombre = JOptionPane.showInputDialog(rootPane, "Tarea", "Modificar", JOptionPane.QUESTION_MESSAGE);
        if (nuevoNombre != null) {
            int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere modificar el registro?", "info", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                List<Actividades> list = this.PendienteControl.consultar();
                if (nuevoNombre.isEmpty() || nuevoNombre.isBlank()) {
                    JOptionPane.showMessageDialog(rootPane, "La actividad esta vacia", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nuevoNombre.length() > 100) {
                    JOptionPane.showMessageDialog(rootPane, "Logitud mayor a 100", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (list.contains(new Actividades(nuevoNombre))) {
                    JOptionPane.showMessageDialog(rootPane, "La actividad ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                tareaPendiente.setNombre_Tarea(nuevoNombre);
                PendienteControl.modificar(tareaPendiente);
                JOptionPane.showMessageDialog(rootPane, "La actividad se modifico con exito!! ", "Listo", JOptionPane.INFORMATION_MESSAGE);
                this.cargarTabla();
            }
        }
    }//GEN-LAST:event_btnEditarPendienteActionPerformed

    private void btnEditarProgresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProgresoActionPerformed

        Actividades tareaPendiente;
        try {
            DefaultTableModel dtmPendiente = (DefaultTableModel) tblProgreso.getModel();
            tareaPendiente = (Actividades) dtmPendiente.getValueAt(tblProgreso.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "No sea seleccionado una tarea", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog(rootPane, "Tarea", "Modificar", JOptionPane.QUESTION_MESSAGE);
        if (nuevoNombre != null) {
            int opcion = JOptionPane.showConfirmDialog(rootPane, "Seguro que quiere modificar el registro?", "info", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                List<Actividades> list = this.PendienteControl.consultar();
                if (nuevoNombre.isEmpty() || nuevoNombre.isBlank()) {
                    JOptionPane.showMessageDialog(rootPane, "La actividad esta vacia", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nuevoNombre.length() > 100) {
                    JOptionPane.showMessageDialog(rootPane, "Logitud mayor a 100", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (list.contains(new Actividades(nuevoNombre))) {
                    JOptionPane.showMessageDialog(rootPane, "La actividad ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DefaultTableModel dtmPendiente = (DefaultTableModel) tblProgreso.getModel();
                tareaPendiente = (Actividades) dtmPendiente.getValueAt(tblProgreso.getSelectedRow(), 0);
                tareaPendiente.setNombre_Tarea(nuevoNombre);
                PendienteControl.modificar(tareaPendiente);
                JOptionPane.showMessageDialog(rootPane, "La actividad se modifico con exito!! ", "Listo", JOptionPane.INFORMATION_MESSAGE);
                this.cargarTabla();
            }
        }
    }//GEN-LAST:event_btnEditarProgresoActionPerformed

    private void resetearTodoTimer() {
        t.stop();
        contPomodoros = 0;
        setTiempos(TIEMPO_PURO_CERO);
        actualizarLabelTiempo();
        actualizaLblContadorDescansos();
        btnPausar.setEnabled(false);
        
        if (timerBeep.isRunning()) {
            timerBeep.stop();
        }
    }

    private void actualizaLblContadorDescansos() {
        switch (contPomodoros) {
            case 4 ->
                lblContadorDescansos.setText("El siguiente descanso sera largo");
            case 5 ->
                lblContadorDescansos.setText("Este es el descanso largo");
            default ->
                lblContadorDescansos.setText("Faltan " + (4 - contPomodoros) + " pomodoros para el siguiente descanso largo");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_Agregar_Tarea;
    private javax.swing.JButton btnContinuar;
    private javax.swing.JButton btnEditarPendiente;
    private javax.swing.JButton btnEditarProgreso;
    private javax.swing.JButton btnEmpezar;
    private javax.swing.JButton btnOmitir;
    private javax.swing.JButton btnPausar;
    private javax.swing.JButton btnPendienteAProgreso;
    private javax.swing.JButton btnPendienteATerminado;
    private javax.swing.JButton btnRestablecer;
    private javax.swing.JButton btn_eliminar1;
    private javax.swing.JButton btn_eliminar2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblContadorDescansos;
    private javax.swing.JLabel lblTiempo;
    private javax.swing.JLabel lblindicador;
    private javax.swing.JTable tblPendientes;
    private javax.swing.JTable tblPendientes1;
    private javax.swing.JTable tblProgreso;
    private javax.swing.JTable tblTerminado;
    // End of variables declaration//GEN-END:variables
}
