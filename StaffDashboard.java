/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import java.awt.CardLayout;

/**
 *
 * @author kent panim
 */
public class StaffDashboard extends javax.swing.JFrame {
    private String staffLoginId;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StaffDashboard.class.getName());

    /**
     * Creates new form StaffDashboard
     */
    public StaffDashboard(String staffLoginId) {
        initComponents();
        this.staffLoginId = staffLoginId;
        
        setLocationRelativeTo(null);
        setTitle("Staff Dashboard");
        
        loadDashboardData();
        loadRecentDutyRecords();
        loadMyScholars();
    }
    
    private void loadDashboardData() {
    loadSummaryCounts();
    loadRecentDutyRecords();
    loadDutyRecords();
    loadVerifyDuty();
}

private void loadSummaryCounts() {
    String sql = """
        SELECT
            COUNT(DISTINCT a.scholar_id) AS total_scholars,
            SUM(CASE
                WHEN dr.verification_status = 'Pending' THEN 1
                ELSE 0
            END) AS pending_count,
            SUM(CASE
                WHEN dr.verification_status = 'Verified' THEN 1
                ELSE 0
            END) AS verified_count
        FROM staff s
        LEFT JOIN assignments a ON s.staff_id = a.staff_id
        LEFT JOIN duty_records dr ON a.assignment_id = dr.assignment_id
        WHERE s.staff_code = ?
        """;

    try (java.sql.Connection con =
             scholarduty.DatabaseConnection.getConnection();
         java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, staffLoginId);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                lblMyScholarsCount.setText(
                    "Total Scholars: " + rs.getInt("total_scholars"));

                lblPendingCount.setText(
                    "Pending Verification: " + rs.getInt("pending_count"));

                lblVerifiedCount.setText(
                    "Verified Duty Records: " + rs.getInt("verified_count"));
            }
        }
    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Unable to load dashboard counts: " + e.getMessage());
    }
}
 private void loadRecentDutyRecords() {
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel)
            tblRecentDutyRecords.getModel();

    model.setRowCount(0);

    String sql =
        "SELECT s.student_id, " +
        "CONCAT(s.first_name, ' ', s.last_name) AS scholar_name, " +
        "dr.duty_date, dr.time_in, dr.time_out, " +
        "dr.verification_status " +
        "FROM duty_records dr " +
        "JOIN assignments a ON dr.assignment_id = a.assignment_id " +
        "JOIN scholars s ON a.scholar_id = s.scholar_id " +
        "JOIN staff st ON a.staff_id = st.staff_id " +
        "WHERE st.staff_code = ? " +
        "ORDER BY dr.duty_date DESC";

    try (java.sql.Connection con =
             scholarduty.DatabaseConnection.getConnection();
         java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, staffLoginId);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("student_id"),
                    rs.getString("scholar_name"),
                    rs.getDate("duty_date"),
                    rs.getTime("time_in"),
                    rs.getTime("time_out"),
                    rs.getString("verification_status")
                });
            }
        }
    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Error loading recent duty records: " + e.getMessage()
        );
    }
}
private void loadMyScholars() {
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel)
        tblMyScholars.getModel();

    model.setRowCount(0);

    String sql =
        "SELECT s.student_id, " +
        "CONCAT(s.first_name, ' ', s.last_name) AS scholar_name, " +
        "s.program, o.office_name, a.assigned_hours, " +
        "COALESCE(SUM(dr.daily_hours), 0) AS completed_hours " +
        "FROM assignments a " +
        "JOIN scholars s ON a.scholar_id = s.scholar_id " +
        "JOIN staff st ON a.staff_id = st.staff_id " +
        "LEFT JOIN offices o ON a.office_id = o.office_id " +
        "LEFT JOIN duty_records dr ON a.assignment_id = dr.assignment_id " +
        "WHERE st.staff_code = ? " +
        "AND a.status = 'Active' " +
        "GROUP BY s.student_id, s.first_name, s.last_name, " +
        "s.program, o.office_name, a.assignment_id, a.assigned_hours";

    try (java.sql.Connection con =
             scholarduty.DatabaseConnection.getConnection();
         java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, staffLoginId);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                double assigned = rs.getDouble("assigned_hours");
                double completed = rs.getDouble("completed_hours");

                model.addRow(new Object[]{
                    rs.getString("student_id"),
                    rs.getString("scholar_name"),
                    rs.getString("program"),
                    rs.getString("office_name"),
                    assigned,
                    completed,
                    Math.max(0, assigned - completed),
                    completed >= assigned ? "Completed" : "In Progress"
                });
            }
        }
    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Error loading scholars: " + e.getMessage());
    }
}
private void loadDutyRecords() {
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel)
        tblDutyRecords.getModel();

    model.setRowCount(0);

    String sql =
        "SELECT s.student_id, " +
        "CONCAT(s.first_name, ' ', s.last_name) AS scholar_name, " +
        "dr.duty_date, dr.time_in, dr.time_out, " +
        "dr.verification_status " +
        "FROM duty_records dr " +
        "JOIN assignments a ON dr.assignment_id = a.assignment_id " +
        "JOIN scholars s ON a.scholar_id = s.scholar_id " +
        "JOIN staff st ON a.staff_id = st.staff_id " +
        "WHERE st.staff_code = ? " +
        "ORDER BY dr.duty_date DESC";

    try (java.sql.Connection con =
             scholarduty.DatabaseConnection.getConnection();
         java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, staffLoginId);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("student_id"),
                    rs.getString("scholar_name"),
                    rs.getDate("duty_date"),
                    rs.getTime("time_in"),
                    rs.getTime("time_out"),
                    rs.getString("verification_status")
                });
            }
        }
    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Error loading duty records: " + e.getMessage());
    }
}

private void loadVerifyDuty() {
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel)
        tblVerifyDuty.getModel();

    model.setRowCount(0);

    String sql =
        "SELECT dr.record_id, s.student_id, " +
        "CONCAT(s.first_name, ' ', s.last_name) AS scholar_name, " +
        "dr.duty_date, dr.time_in, dr.time_out, " +
        "dr.daily_hours, dr.verification_status " +
        "FROM duty_records dr " +
        "JOIN assignments a ON dr.assignment_id = a.assignment_id " +
        "JOIN scholars s ON a.scholar_id = s.scholar_id " +
        "JOIN staff st ON a.staff_id = st.staff_id " +
        "WHERE st.staff_code = ? " +
        "AND dr.verification_status = 'Pending' " +
        "ORDER BY dr.duty_date DESC";

    try (java.sql.Connection con =
             scholarduty.DatabaseConnection.getConnection();
         java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, staffLoginId);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("record_id"),
                    rs.getString("student_id"),
                    rs.getString("scholar_name"),
                    rs.getDate("duty_date"),
                    rs.getTime("time_in"),
                    rs.getTime("time_out"),
                    rs.getDouble("daily_hours"),
                    rs.getString("verification_status")
                });
            }
        }
    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Error loading pending duty: " + e.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JButton();
        btnMyScholars = new javax.swing.JButton();
        btnDutyRecords = new javax.swing.JButton();
        btnVerifyDuty = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        pnlMainContent = new javax.swing.JPanel();
        pnlStaffDashboard = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblMyScholarsCount = new javax.swing.JLabel();
        lblPendingCount = new javax.swing.JLabel();
        lblVerifiedCount = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecentDutyRecords = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        pnlMyScholars = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMyScholars = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        pnlDutyRecords = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDutyRecords = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        pnlVerifyDuty = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblVerifyDuty = new javax.swing.JTable();
        btnVerifySelected = new javax.swing.JButton();
        btnRejectSelected = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 0));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Screenshot 2026-10-07 234923.png"))); // NOI18N
        jLabel6.setText("jLabel6");

        jLabel7.setFont(new java.awt.Font("Bookman Old Style", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("PHINMA-COC");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Scholarship Duty Time Tracker");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 154, 0));

        btnDashboard.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        btnDashboard.setText("DASHBOARD");
        btnDashboard.addActionListener(this::btnDashboardActionPerformed);

        btnMyScholars.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        btnMyScholars.setText("MY SCHOLARS");
        btnMyScholars.addActionListener(this::btnMyScholarsActionPerformed);

        btnDutyRecords.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        btnDutyRecords.setText(" DUTY RECORDS");
        btnDutyRecords.addActionListener(this::btnDutyRecordsActionPerformed);

        btnVerifyDuty.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        btnVerifyDuty.setText("VERIFY DUTY");
        btnVerifyDuty.addActionListener(this::btnVerifyDutyActionPerformed);

        btnLogout.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMyScholars, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDutyRecords, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnVerifyDuty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(btnLogout)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(btnDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(btnMyScholars, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(btnDutyRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnVerifyDuty, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116)
                .addComponent(btnLogout)
                .addContainerGap(640, Short.MAX_VALUE))
        );

        pnlMainContent.setLayout(new java.awt.CardLayout());

        pnlStaffDashboard.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel1.setText("STAFF DASBOARD");

        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        jLabel2.setText("WELCOME T0 THE PHINMA-COC SCHOLARS DUTY TIME TRACKER");

        lblMyScholarsCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblMyScholarsCount.setText("My Scholars");

        lblPendingCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPendingCount.setText("Pending");

        lblVerifiedCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblVerifiedCount.setText("Verified");

        tblRecentDutyRecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Student Name", "Duty Date", "Time In", "Time out", "Verification Status"
            }
        ));
        jScrollPane1.setViewportView(tblRecentDutyRecords);

        jLabel3.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        jLabel3.setText("Recent Duty Records");

        javax.swing.GroupLayout pnlStaffDashboardLayout = new javax.swing.GroupLayout(pnlStaffDashboard);
        pnlStaffDashboard.setLayout(pnlStaffDashboardLayout);
        pnlStaffDashboardLayout.setHorizontalGroup(
            pnlStaffDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStaffDashboardLayout.createSequentialGroup()
                .addGroup(pnlStaffDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlStaffDashboardLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel2))
                    .addGroup(pnlStaffDashboardLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(pnlStaffDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlStaffDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblPendingCount, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addComponent(lblVerifiedCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblMyScholarsCount, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlStaffDashboardLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlStaffDashboardLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(pnlStaffDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 951, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        pnlStaffDashboardLayout.setVerticalGroup(
            pnlStaffDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStaffDashboardLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(lblMyScholarsCount)
                .addGap(18, 18, 18)
                .addComponent(lblPendingCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblVerifiedCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(215, 215, 215))
        );

        pnlMainContent.add(pnlStaffDashboard, "Dashboard");

        pnlMyScholars.setBackground(new java.awt.Color(153, 153, 153));

        tblMyScholars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Student Name", "Program", "Office", "Assigned Hours", "Completed Hour", "Remaining Hours", "Status"
            }
        ));
        jScrollPane2.setViewportView(tblMyScholars);

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel4.setText("My Scholars");

        javax.swing.GroupLayout pnlMyScholarsLayout = new javax.swing.GroupLayout(pnlMyScholars);
        pnlMyScholars.setLayout(pnlMyScholarsLayout);
        pnlMyScholarsLayout.setHorizontalGroup(
            pnlMyScholarsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMyScholarsLayout.createSequentialGroup()
                .addGroup(pnlMyScholarsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMyScholarsLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 999, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlMyScholarsLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel4)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        pnlMyScholarsLayout.setVerticalGroup(
            pnlMyScholarsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMyScholarsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        pnlMainContent.add(pnlMyScholars, "My Scholars");

        pnlDutyRecords.setBackground(new java.awt.Color(153, 153, 153));

        tblDutyRecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Student Name", "Duty Date", "Time In", "Time Out", "Verification Status"
            }
        ));
        jScrollPane3.setViewportView(tblDutyRecords);

        jLabel5.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel5.setText("Student Duty Records");

        javax.swing.GroupLayout pnlDutyRecordsLayout = new javax.swing.GroupLayout(pnlDutyRecords);
        pnlDutyRecords.setLayout(pnlDutyRecordsLayout);
        pnlDutyRecordsLayout.setHorizontalGroup(
            pnlDutyRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDutyRecordsLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlDutyRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1007, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnlDutyRecordsLayout.setVerticalGroup(
            pnlDutyRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDutyRecordsLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel5)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );

        pnlMainContent.add(pnlDutyRecords, "Duty Records");

        pnlVerifyDuty.setBackground(new java.awt.Color(153, 153, 153));

        tblVerifyDuty.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Student Name", "Duty Date", "Time In", "Time Out", "Daily Hours", "Verification Status"
            }
        ));
        jScrollPane4.setViewportView(tblVerifyDuty);

        btnVerifySelected.setText("Verify Selected");
        btnVerifySelected.addActionListener(this::btnVerifySelectedActionPerformed);

        btnRejectSelected.setText("Reject Selected");
        btnRejectSelected.addActionListener(this::btnRejectSelectedActionPerformed);

        javax.swing.GroupLayout pnlVerifyDutyLayout = new javax.swing.GroupLayout(pnlVerifyDuty);
        pnlVerifyDuty.setLayout(pnlVerifyDutyLayout);
        pnlVerifyDutyLayout.setHorizontalGroup(
            pnlVerifyDutyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVerifyDutyLayout.createSequentialGroup()
                .addGroup(pnlVerifyDutyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVerifyDutyLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 996, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlVerifyDutyLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(btnVerifySelected)
                        .addGap(52, 52, 52)
                        .addComponent(btnRejectSelected)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlVerifyDutyLayout.setVerticalGroup(
            pnlVerifyDutyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVerifyDutyLayout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addGroup(pnlVerifyDutyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnVerifySelected, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(btnRejectSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(227, 227, 227))
        );

        pnlMainContent.add(pnlVerifyDuty, "Verify Duty");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMainContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlMainContent, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
   CardLayout cl = (CardLayout) pnlMainContent.getLayout();
cl.show(pnlMainContent, "Dashboard");
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnMyScholarsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMyScholarsActionPerformed
    CardLayout cl = (CardLayout) pnlMainContent.getLayout();
cl.show(pnlMainContent, "My Scholars");
    }//GEN-LAST:event_btnMyScholarsActionPerformed

    private void btnDutyRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDutyRecordsActionPerformed
   CardLayout cl = (CardLayout) pnlMainContent.getLayout();
cl.show(pnlMainContent, "Duty Records");
    }//GEN-LAST:event_btnDutyRecordsActionPerformed

    private void btnVerifyDutyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerifyDutyActionPerformed
    CardLayout cl = (CardLayout) pnlMainContent.getLayout();
cl.show(pnlMainContent, "Verify Duty");
    }//GEN-LAST:event_btnVerifyDutyActionPerformed

    private void btnVerifySelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerifySelectedActionPerformed
 verifySelectedDuty();
    }//GEN-LAST:event_btnVerifySelectedActionPerformed

    private void btnRejectSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectSelectedActionPerformed
     rejectSelectedDuty();
    }//GEN-LAST:event_btnRejectSelectedActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
  int choice = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to log out?",
        "Confirm Logout",
        javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (choice == javax.swing.JOptionPane.YES_OPTION) {
        this.dispose();
        new LoginGUI().setVisible(true);
    }
    }//GEN-LAST:event_btnLogoutActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new StaffDashboard("").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDutyRecords;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMyScholars;
    private javax.swing.JButton btnRejectSelected;
    private javax.swing.JButton btnVerifyDuty;
    private javax.swing.JButton btnVerifySelected;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblMyScholarsCount;
    private javax.swing.JLabel lblPendingCount;
    private javax.swing.JLabel lblVerifiedCount;
    private javax.swing.JPanel pnlDutyRecords;
    private javax.swing.JPanel pnlMainContent;
    private javax.swing.JPanel pnlMyScholars;
    private javax.swing.JPanel pnlStaffDashboard;
    private javax.swing.JPanel pnlVerifyDuty;
    private javax.swing.JTable tblDutyRecords;
    private javax.swing.JTable tblMyScholars;
    private javax.swing.JTable tblRecentDutyRecords;
    private javax.swing.JTable tblVerifyDuty;
    // End of variables declaration//GEN-END:variables
private void verifySelectedDuty() {
    int row = tblVerifyDuty.getSelectedRow();

    if (row == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Please select a duty record first.");
        return;
    }

    int recordId = Integer.parseInt(
        tblVerifyDuty.getValueAt(row, 0).toString());

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to verify this duty record?",
        "Confirm Verification",
        javax.swing.JOptionPane.YES_NO_OPTION);

    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

    String sql =
        "UPDATE duty_records dr " +
        "JOIN assignments a ON dr.assignment_id = a.assignment_id " +
        "JOIN staff st ON a.staff_id = st.staff_id " +
        "SET dr.verification_status = 'Verified', " +
        "dr.verified_at = NOW() " +
        "WHERE dr.record_id = ? " +
        "AND st.staff_code = ? " +
        "AND dr.verification_status = 'Pending'";

    try (java.sql.Connection con =
             scholarduty.DatabaseConnection.getConnection();
         java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, recordId);
        pst.setString(2, staffLoginId);

        int updated = pst.executeUpdate();

        if (updated > 0) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Duty record verified successfully!");

            loadVerifyDuty();
            loadDutyRecords();
            loadRecentDutyRecords();
            loadDashboardData();
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Record was not updated. It may already be processed.");
        }

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Error verifying duty: " + e.getMessage());
    }
}

private void rejectSelectedDuty() {
    int row = tblVerifyDuty.getSelectedRow();

    if (row == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Please select a duty record first.");
        return;
    }

    int recordId = Integer.parseInt(
        tblVerifyDuty.getValueAt(row, 0).toString());

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to reject this duty record?",
        "Confirm Rejection",
        javax.swing.JOptionPane.YES_NO_OPTION);

    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

    String sql =
        "UPDATE duty_records dr " +
        "JOIN assignments a ON dr.assignment_id = a.assignment_id " +
        "JOIN staff st ON a.staff_id = st.staff_id " +
        "SET dr.verification_status = 'Rejected' " +
        "WHERE dr.record_id = ? " +
        "AND st.staff_code = ? " +
        "AND dr.verification_status = 'Pending'";

    try (java.sql.Connection con =
             scholarduty.DatabaseConnection.getConnection();
         java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, recordId);
        pst.setString(2, staffLoginId);

        int updated = pst.executeUpdate();

        if (updated > 0) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Duty record rejected.");

            loadVerifyDuty();
            loadDutyRecords();
            loadRecentDutyRecords();
            loadDashboardData();
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Record was not updated. It may already be processed.");
        }

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Error rejecting duty: " + e.getMessage());
    }
}

}
