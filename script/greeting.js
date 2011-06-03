importPackage(javax.swing);
importPackage(java.lang);
importPackage(java.awt.event);

jFrame1 = new JFrame("Greeting");
jLabel1 = new JLabel("Name:");
jTextField1 = JTextField();
jLabel2 = new JLabel();
jButton1 = new JButton("Click");
jFrame1.setSize(400, 400);
listener1 = {
	actionPerformed : function(e) {
		jLabel2.setText("Hello " + jTextField1.getText() + " !");
	}
}
alistener = new ActionListener(listener1);
jButton1.addActionListener(alistener);
jFrame1.getContentPane().setLayout(null);
jFrame1.getContentPane().add(jLabel1);
jFrame1.getContentPane().add(jTextField1);
jFrame1.getContentPane().add(jLabel2);
jFrame1.getContentPane().add(jButton1);
jLabel1.setBounds(10, 50, 40, 20);
jTextField1.setBounds(50, 50, 220, 20);
jLabel2.setBounds(10, 100, 220, 20);
jButton1.setBounds(280, 50, 120, 23);
jFrame1.setDefaultCloseOperation(jFrame1.EXIT_ON_CLOSE);
jFrame1.setVisible(true);
for (;;) {
}