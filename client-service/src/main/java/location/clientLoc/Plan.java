package location.clientLoc;

import location.serveurLoc.serveurLoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Plan extends Canvas{

    static JFrame frame=new JFrame("Réservation d'un espace")	;
    static JPanel panel = new JPanel();
    static JPanel panelTop = new JPanel();
    static serveurLoc r = new serveurLoc();

    static String bat = "Batiment A";
    static String etage = "etage 1";

    static class StyledButtonUI extends BasicButtonUI {

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            AbstractButton button = (AbstractButton) c;
            button.setOpaque(false);
            button.setBorder(new EmptyBorder(5, 15, 5, 15));
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
            super.paint(g, c);
        }

        private void paintBackground(Graphics g, JComponent c, int yOffset) {
            Dimension size = c.getSize();
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(c.getBackground().darker());
            g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
            g.setColor(c.getBackground());
            g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
        }
    }

    public void paint( Graphics g ) {
        this.setBackground( Color.WHITE );
        int space = 10;
        String[] tabOcu = r.initPlan(bat, etage);
        for(int i = 0; i < tabOcu.length; i++) {
            System.out.println(tabOcu[i]);
        }
        for(int i = 0; i < 10; i++) {
            if (i < 5) {
                if (tabOcu[i].equalsIgnoreCase("booked")) {
                    g.setColor(Color.RED);
                    space = (i == 0) ? 0 : i * 10;
                    g.fillRect(40 + (150 * i) + space, 50, 150, 100);
                } else {
                    g.setColor(Color.BLUE);
                    space = (i == 0) ? 0 : i * 10;
                    g.drawRect(40 + (150 * i) + space, 50, 150, 100);
                }
            } else if (i >= 5) {
                if (tabOcu[i].equalsIgnoreCase("booked")) {
                    g.setColor(Color.RED);
                    space = (i == 5) ? 0 : (i - 5) * 10;
                    g.fillRect(40 + (150 * (i - 5)) + space, 200, 150, 100);
                } else {
                    g.setColor(Color.BLUE);
                    space = (i == 5) ? 0 : (i - 5) * 10;
                    g.drawRect(40 + (150 * (i - 5)) + space, 200, 150, 100);
                }
            }
        }
    }

    public static class ActionAccueil implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            panel.removeAll();
            frame.dispose();
            HomeLocation h = new HomeLocation();
            String[] args = {};
            h.main(args);
        }
    }

    public static class ActionPlan implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            panel.removeAll();
            frame.dispose();
            Plan p = new Plan();
            String[] args = {};
            p.main(args);
        }
    }

    public static class ActionFormulaire implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            panel.removeAll();
            Location.panelFormulaire.removeAll();
            frame.dispose();
            Location l = new Location();
            String[] args = {};
            l.main(args);
        }
    }

    public String getBat(){
        return bat;
    }

    public String getEtage(){
        return etage;
    }


    public static void main (String[] args) {

        panel.setLayout(new BorderLayout());
        panelTop.setLayout(new BorderLayout());
        JToolBar toolbar = new JToolBar();


        final JButton buttonAccueil = new JButton("Accueil");
        buttonAccueil.addActionListener(new ActionAccueil());
        buttonAccueil.setFont(new Font("Calibri", Font.PLAIN, 14));
        buttonAccueil.setBackground(new Color(0x3C4DCE));
        buttonAccueil.setForeground(Color.white);
        buttonAccueil.setUI(new HomeLocation.StyledButtonUI());
        //panelTop.add(buttonAccueil, BorderLayout.EAST);
        toolbar.add(buttonAccueil);

        final JButton buttonPlan = new JButton("Plan");
        buttonPlan.addActionListener(new ActionPlan());
        buttonPlan.setFont(new Font("Calibri", Font.PLAIN, 14));
        buttonPlan.setBackground(new Color(0x3C4DCE));
        buttonPlan.setForeground(Color.white);
        buttonPlan.setUI(new HomeLocation.StyledButtonUI());
        //panelTop.add(buttonPlan, BorderLayout.CENTER);
        toolbar.add(buttonPlan);

        final JButton buttonFormulaire = new JButton("Formulaire");
        buttonFormulaire.addActionListener(new ActionFormulaire());
        buttonFormulaire.setFont(new Font("Calibri", Font.PLAIN, 14));
        buttonFormulaire.setBackground(new Color(0x3C4DCE));
        buttonFormulaire.setForeground(Color.white);
        buttonFormulaire.setUI(new HomeLocation.StyledButtonUI());
        //panelTop.add(buttonPlan, BorderLayout.CENTER);
        toolbar.add(buttonFormulaire);


        panelTop.add(toolbar, BorderLayout.NORTH);

        String[] batListe = r.listBat();
        JComboBox<String> listeBat = new JComboBox(batListe);
        listeBat.setBounds(10, 10, 120, 23);
        listeBat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                frame.dispose();
                Plan p = new Plan();
                String[] args = {};
                p.main(args);
                e.getSource();
                String s=(String) listeBat.getSelectedItem();
                bat = s;
                System.out.println(s);
                r.listEtage(bat);
            }
        });
        panelTop.add(listeBat, BorderLayout.CENTER);

        String[] etageListe = r.listEtage(bat);
        JComboBox<String> listeEtage = new JComboBox(etageListe);
        listeEtage.setBounds(10, 10, 120, 23);
        listeEtage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                frame.dispose();
                Plan p = new Plan();
                String[] args = {};
                p.main(args);
                e.getSource();
                String s=(String) listeEtage.getSelectedItem();
                etage = s;
                System.out.println(s);
            }
        });
        panelTop.add(listeEtage, BorderLayout.SOUTH);


        Plan plan = new Plan();
        frame.add(panel);


        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(plan, BorderLayout.CENTER);

        frame.setSize(1530,600);
        frame.setVisible(true);
    }
}
