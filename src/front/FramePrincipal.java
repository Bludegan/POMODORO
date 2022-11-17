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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
    private int m, s, cs;
    private int contParpadeante;

    private ActionListener acciones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            /*
            cs++;
            if (cs == 100) {
                cs = 0;
                ++s;
            }
            if (s == 60) {
                s = 0;
                ++m;
            }
            actualizarLabel();
            if (m == 5) {
                pararYAvisar();
            }
             */
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
        btnEmpezar.setEnabled(true);
        btnPausar.setEnabled(false);
        btnRestablecer.setEnabled(false);
        ocultarIndicador();
        if (timerLB.isRunning()) {
            timerLB.stop();
            lblTiempo.setVisible(true);
            btnPausar.setText("Pausar");
            banderaPausa = true;
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
        actualizaLblContadorDescansos();
        btnEmpezarActionPerformed(null);
    }

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
        lblTiempo = new javax.swing.JLabel();
        btnEmpezar = new javax.swing.JButton();
        btnPausar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPendientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        Btn_Agregar_Tarea = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblindicador = new javax.swing.JLabel();
        btnContinuar = new javax.swing.JButton();
        lblContadorDescansos = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProgreso = new javax.swing.JTable();
        btnPendienteAProgreso = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnPendienteATerminado = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblTerminado = new javax.swing.JTable();
        btnRestablecer = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnOmitir = new javax.swing.JButton();

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
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTiempo.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        lblTiempo.setText("00:00:00");
        lblTiempo.setToolTipText("");
        jPanel1.add(lblTiempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 18, -1, 54));

        btnEmpezar.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        btnEmpezar.setText("Empezar");
        btnEmpezar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpezarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEmpezar, new org.netbeans.lib.awtextra.AbsoluteConstraints(692, 18, -1, -1));

        btnPausar.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        btnPausar.setText("Pausar");
        btnPausar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPausarActionPerformed(evt);
            }
        });
        jPanel1.add(btnPausar, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, -1, -1));

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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 320, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Tareas Terminada");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 170, -1, -1));

        Btn_Agregar_Tarea.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Btn_Agregar_Tarea.setText("Agregar Tarea");
        Btn_Agregar_Tarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Agregar_TareaActionPerformed(evt);
            }
        });
        jPanel1.add(Btn_Agregar_Tarea, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 52, 221, 54));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Tareas Pendientes");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, -1, -1));

        lblindicador.setForeground(new java.awt.Color(255, 51, 51));
        lblindicador.setText("INDICADOR");
        lblindicador.setToolTipText("");
        jPanel1.add(lblindicador, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 52, 175, -1));

        btnContinuar.setText("Continuar");
        btnContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinuarActionPerformed(evt);
            }
        });
        jPanel1.add(btnContinuar, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 86, -1, -1));

        lblContadorDescansos.setText("Contador de Descansos");
        jPanel1.add(lblContadorDescansos, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 18, 276, -1));

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

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 330, 427));

        btnPendienteAProgreso.setText("-->");
        btnPendienteAProgreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendienteAProgresoActionPerformed(evt);
            }
        });
        jPanel1.add(btnPendienteAProgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 640, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Tareas en Progreso");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 170, -1, -1));

        btnPendienteATerminado.setText("-->");
        btnPendienteATerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendienteATerminadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnPendienteATerminado, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 640, -1, -1));

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

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 200, 330, -1));

        btnRestablecer.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        btnRestablecer.setText("Restablece");
        btnRestablecer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestablecerActionPerformed(evt);
            }
        });
        jPanel1.add(btnRestablecer, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 80, -1, -1));

        jButton1.setText("<--");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 640, -1, -1));

        btnOmitir.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        btnOmitir.setText("Omitir");
        btnOmitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOmitirActionPerformed(evt);
            }
        });
        jPanel1.add(btnOmitir, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 80, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1061, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
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
            banderaPausa = false;
        } else {
            timerLB.stop();
            lblTiempo.setVisible(true);
            t.restart();
            btnPausar.setText("Pausar");
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
                System.out.println("Fecha nueva: " + tareaPendiente.getFechaterminacion().toString());
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

    private void resetearTodoTimer() {
        t.stop();
        contPomodoros = 0;
        setTiempos(TIEMPO_PURO_CERO);
        actualizarLabelTiempo();
        actualizaLblContadorDescansos();
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
    private javax.swing.JButton btnEmpezar;
    private javax.swing.JButton btnOmitir;
    private javax.swing.JButton btnPausar;
    private javax.swing.JButton btnPendienteAProgreso;
    private javax.swing.JButton btnPendienteATerminado;
    private javax.swing.JButton btnRestablecer;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
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
