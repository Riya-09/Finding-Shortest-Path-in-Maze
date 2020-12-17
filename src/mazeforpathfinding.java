//all imports

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JComboBox;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
public class mazeforpathfinding {



//    public class mazeforpathfinding{
        //UTIL ARRAYS
        private String[] algorithmschoice = {"Dijkstra","A*(A-STAR)"};
        private String[] options = {"Start","Finish","Wall", "Eraser"};
        //ALL VARIABLES
        private int sourcex = -1;
        private int sourcey = -1;
        private int destinationx = -1;
        private int destinationy = -1;
        private double densityfactor = .4;
    private int dimension=10;
        private double totaldensity = (dimension*dimension)*.4;
        private int option = 0;
        private int noofchecks = 0;
        private int pathlength = 0;
        private int curentrAlg = 0;
        private int WIDTH = 850;

        private int slowness=40;
        private final int HEIGHT = 650;
        private final int Mazesize = 600;
//        private int WIDTH = 850;
//        private final int HEIGHT = 650;
//        private final int Mazesize = 600;
        private int csize = Mazesize/dimension;
        //UTIL
        Element[][] ele;
        Algorithm Alg = new Algorithm();
        Random rand = new Random();
        //BOOLEANS
        private boolean issolved = false;
        //FRAME
        JFrame frame;
        Element map[][];
        //PANELS
        JPanel Panelbox = new JPanel();
        //CANVAS
        Map canvas;
    //SLIDERS
    JSlider dim = new JSlider(1,5,1);
    JSlider slow = new JSlider(0,500,slowness);
    JSlider blackbox = new JSlider(1,100,50);
        //BORDER
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        //LABELS
        JLabel algsel = new JLabel("Algo-choice");
        JLabel optionlist = new JLabel("Options");
        JLabel dimensionset = new JLabel("Dim:");
        JLabel dimensionsquare = new JLabel(dimension+"x"+dimension);
        JLabel slowfact = new JLabel("Slow:");
        JLabel metrepersecond = new JLabel(slowness+"ms");
        JLabel bblabel = new JLabel("Black:");
        JLabel percentage = new JLabel(blackbox.getValue()+"%");
        JLabel noofcheckslabel = new JLabel("Total Checks: "+noofchecks);
        JLabel pathlengthlabel = new JLabel("Path Length: "+pathlength);

        //BUTTONS
        JButton searchpath = new JButton("Search Path");
        JButton resetSD = new JButton("Reset s&d");
        JButton newmaze = new JButton("New Maze");
        JButton eraseall = new JButton("Erase All");
        JButton by = new JButton("By");
        //DROP DOWN
        JComboBox algorithmschoicebox = new JComboBox(algorithmschoice);
        JComboBox optionsbox = new JComboBox(options);

        private void initialize() {	//INITIALIZE THE GUI ELEMENTS
            frame = new JFrame();
            frame.setVisible(true);
            frame.setResizable(true);
            frame.setSize(WIDTH,HEIGHT);
            frame.setTitle("Path Finding in Maze");
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setLayout(null);

            Panelbox.setBorder(BorderFactory.createTitledBorder(loweredetched,"Select"));
            int small = 25;
            int large = 45;

            Panelbox.setLayout(null);
            Panelbox.setBounds(10,10,210,600);

            searchpath.setBounds(40,small, 120, 25);
            Panelbox.add(searchpath);
            small+=large;

            resetSD.setBounds(40,small,120,25);
            Panelbox.add(resetSD);
            small+=large;

            newmaze.setBounds(40,small, 120, 25);
            Panelbox.add(newmaze);
            small+=large;

            eraseall.setBounds(40,small, 120, 25);
            Panelbox.add(eraseall);
            small+=40;

            algsel.setBounds(40,small,120,25);
            Panelbox.add(algsel);
            small+=25;

            algorithmschoicebox.setBounds(40,small, 120, 25);
            Panelbox.add(algorithmschoicebox);
            small+=40;

            optionlist.setBounds(40,small,120,25);
            Panelbox.add(optionlist);
            small+=25;

            optionsbox.setBounds(40,small,120,25);
            Panelbox.add(optionsbox);
            small+=large;

            dimensionset.setBounds(15,small,40,25);
            Panelbox.add(dimensionset);
            dim.setMajorTickSpacing(10);
            dim.setBounds(50,small,100,25);
            Panelbox.add(dim);
            dimensionsquare.setBounds(160,small,40,25);
            Panelbox.add(dimensionsquare);
            small+=large;

            slowfact.setBounds(15,small,50,25);
            Panelbox.add(slowfact);
            slow.setMajorTickSpacing(5);
            slow.setBounds(50,small,100,25);
            Panelbox.add(slow);
            metrepersecond.setBounds(160,small,40,25);
            Panelbox.add(metrepersecond);
            small+=large;

            bblabel.setBounds(15,small,100,25);
            Panelbox.add(bblabel);
            blackbox.setMajorTickSpacing(5);
            blackbox.setBounds(50,small,100,25);
            Panelbox.add(blackbox);
            percentage.setBounds(160,small,100,25);
            Panelbox.add(percentage);
            small+=large;

            noofcheckslabel.setBounds(15,small,100,25);
            Panelbox.add(noofcheckslabel);
            small+=large;

            pathlengthlabel.setBounds(15,small,100,25);
            Panelbox.add(pathlengthlabel);
            small+=large;

            by.setBounds(40, small, 120, 25);
            Panelbox.add(by);

            frame.getContentPane().add(Panelbox);

            canvas = new Map();
            canvas.setBounds(230, 10, Mazesize+1, Mazesize+1);
            frame.getContentPane().add(canvas);

            searchpath.addActionListener(new ActionListener() {		//ACTION LISTENERS
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetvar();
                    if((sourcex > -1 && sourcey > -1) && (destinationx > -1 && destinationy > -1))
                        issolved = true;
                }
            });
            resetSD.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetmaze();
                    Update();
                }
            });
            newmaze.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createnewmaze();
                    Update();
                }
            });
            eraseall.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    clearallpaths();
                    Update();
                }
            });

            algorithmschoicebox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    curentrAlg = algorithmschoicebox.getSelectedIndex();
                    Update();
                }
            });
            optionsbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    option = optionsbox.getSelectedIndex();
                }
            });
            dim.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    dimension = dim.getValue()*10;
                    clearallpaths();
                    resetvar();
                    Update();
                }
            });
            slow.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    slowness = slow.getValue();
                    Update();
                }
            });
            blackbox.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    densityfactor = (double)blackbox.getValue()/100;
                    Update();
                }
            });
            by.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(frame, "	                         Pathfinding\n"
                            + "             Copyright (c) 2020-2021\n"
                            + "                            Riya\n"
                            + "          Build Date:  Dec 10, 2020   ", "Credit", JOptionPane.PLAIN_MESSAGE, new ImageIcon(""));
                }
            });

            startSearch();	//START STATE
        }


        public static void main(String[] args) {	//MAIN METHOD
            new mazeforpathfinding();
        }
        public mazeforpathfinding() {	//CONSTRUCTOR
            clearallpaths();
            initialize();
        }
        public void clearallpaths() {	//CLEAR MAP
            sourcex = -1;	//RESET THE START AND FINISH
            sourcey = -1;
            destinationx = -1;
            destinationy = -1;
            map = new Element[dimension][dimension];	//CREATE NEW MAP OF NODES
            for(int i = 0; i < dimension; i++) {
                for(int j = 0; j < dimension; j++) {
                    map[i][j] = new Element(3,i,j);	//SET ALL NODES TO EMPTY
                }
            }
            resetvar();	//RESET SOME VARIABLES
        }
        public void resetvar() {	//RESET METHOD
            issolved = false;
            pathlength = 0;
            noofchecks = 0;
        }
        public void createnewmaze() {	//CREATE A NEW MAZE
            clearallpaths();	//CLEAR ALL PATHS FOR CREATING A NEW MAZE
            for(int i = 0; i < totaldensity; i++) {
                Element curr;
                do {
                    int x = rand.nextInt(dimension);
                    int y = rand.nextInt(dimension);
                    curr = map[x][y];	//FIND A RANDOM ELEMENT IN THE MAZE
                } while(curr.getType()==2);	//IF IT IS ALREADY A WALL, FIND A NEW ONE
                curr.setType(2);	//SET ELEMENT TO BE A WALL
            }
        }
        public void resetmaze() {	//RESET MAZE
            for(int x = 0; x < dimension; x++) {
                for(int y = 0; y < dimension; y++) {
                    Element curr = map[x][y];
                    if(curr.getType() == 4 || curr.getType() == 5)	//CHECK TO SEE IF CURRENT ELEMENT IS NEITHER CHECKED NOR FINAL PATH
                        map[x][y] = new Element(3,x,y);	//RESET IT TO AN EMPTY ELEMENT
                }
            }
            if(sourcex > -1 && sourcey > -1) {	//RESET THE SOURCE AND DESTINATION
                map[sourcex][sourcey] = new Element(0,sourcex,sourcey);
                map[sourcex][sourcey].setHop(0);
            }
            if(destinationx > -1 && destinationy > -1)
               map[destinationx][destinationy] = new Element(1,destinationx,destinationy);
            resetvar();	//RESET SOME VARIABLES
        }

        public void Update() {	//UPDATE ELEMENTS OF THE GUI
            totaldensity = (dimension*dimension)*densityfactor;
            csize = Mazesize/dimension;
            canvas.repaint();
            dimensionsquare.setText(dimension+"x"+dimension);
            metrepersecond.setText(slowness+"ms");
            pathlengthlabel.setText("Path Length: "+pathlength);
            percentage.setText(blackbox.getValue()+"%");
            noofcheckslabel.setText("Checks: "+noofchecks);
        }
        public void startSearch() {	//START STATE
            if(issolved) {
                switch(curentrAlg) {
                    case 0:
                        Alg.Dijkstra();
                        break;
                    case 1:
                        Alg.AStar();
                        break;
                }
            }
            pause();	//PAUSE STATE
        }
        public void pause() {	//PAUSE STATE
            int i = 0;
            while(!issolved) {
                i++;
                if(i > 500)
                    i = 0;
                try {
                    Thread.sleep(1);
                } catch(Exception e) {}
            }
            startSearch();	//START STATE
        }
        public void delay() {	//DELAY METHOD
            try {
                Thread.sleep(slowness);
            } catch(Exception e) {}
        }

        class Map extends JPanel implements MouseListener, MouseMotionListener{	//MAP CLASS

            public Map() {
                addMouseListener(this);
                addMouseMotionListener(this);
            }

            public void paintComponent(Graphics gr) {	//REPAINT
                super.paintComponent(gr);
                for(int x = 0; x < dimension; x++) {	//PAINT EACH NODE IN THE GRID
                    for(int y = 0; y < dimension; y++) {
                        switch(map[x][y].getType()) {
                            case 0:
                                gr.setColor(Color.MAGENTA);
                                break;
                            case 1:
                                gr.setColor(Color.YELLOW);
                                break;
                            case 2:
                                gr.setColor(Color.BLACK);
                                break;
                            case 3:
                                gr.setColor(Color.WHITE);
                                break;
                            case 4:
                                gr.setColor(Color.PINK);
                                break;
                            case 5:
                                gr.setColor(Color.BLUE);
                                break;
                        }
                        gr.fillRect(x*csize,y*csize,csize,csize);
                        gr.setColor(Color.BLACK);
                        gr.drawRect(x*csize,y*csize,csize,csize);
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent evt) {
                try {
                    int x = evt.getX()/csize;
                    int y = evt.getY()/csize;
                    Element curr = map[x][y];
                    if((option == 2 || option == 3) && (curr.getType() != 0 && curr.getType() != 1))
                        curr.setType(option);
                    Update();
                } catch(Exception z) {}
            }

            @Override
            public void mouseMoved(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent evt) {
                resetmaze();	//RESET THE MAP WHENEVER CLICKED
                try {
                    int x = evt.getX()/csize;	//GET THE X AND Y OF THE MOUSE CLICK IN RELATION TO THE SIZE OF THE GRID
                    int y = evt.getY()/csize;
                    Element curr = map[x][y];
                    switch(option ) {
                        case 0: {	//START NODE
                            if(curr.getType()!=2) {	//IF NOT WALL
                                if(sourcex > -1 && sourcey > -1) {	//IF START EXISTS SET IT TO EMPTY
                                    map[sourcex][sourcey].setType(3);
                                    map[sourcex][sourcey].setHop(-1);
                                }
                                curr.setHop(0);
                                sourcex = x;	//SET THE START X AND Y
                                sourcey = y;
                                curr.setType(0);	//SET THE NODE CLICKED TO BE START
                            }
                            break;
                        }
                        case 1: {
//FINISH NODE
                            if(curr.getType()!=2) {	//IF NOT WALL
                                if(destinationx > -1 && destinationy > -1)	//IF FINISH EXISTS SET IT TO EMPTY
                                    map[destinationx][destinationy].setType(3);
                                destinationx = x;	//SET THE FINISH X AND Y
                                destinationy = y;
                                curr.setType(1);	//SET THE NODE CLICKED TO BE FINISH
                            }
                            break;
                        }
                        default:
                            if(curr.getType() != 0 && curr.getType() != 1)
                                curr.setType(option);
                            break;
                    }
                    Update();
                } catch(Exception z) {}	//EXCEPTION HANDLER
            }

            @Override
            public void mouseReleased(MouseEvent e) {}
        }
    class Algorithm {	//ALGORITHM CLASS


        public void Dijkstra() {
            ArrayList<Element> pq = new ArrayList<Element>();    //CREATE A PRIORITY QUE
            pq.add(map[sourcex][sourcey]);    //ADD THE START TO THE QUE
            while (issolved) {
                if (pq.size() <= 0) {    //IF THE QUE IS 0 THEN NO PATH CAN BE FOUND
                    issolved = false;
                    break;
                }
                int hop = pq.get(0).getHop() + 1;    //INCREMENT THE HOPS VARIABLE
                ArrayList<Element> explored = exploreNbr(pq.get(0), hop);    //CREATE AN ARRAYLIST OF NODES THAT WERE EXPLORED
                if (explored.size() > 0) {
                    pq.remove(0);    //REMOVE THE NODE FROM THE QUE
                    pq.addAll(explored);    //ADD ALL THE NEW NODES TO THE QUE
                    Update();
                    delay();
                } else {    //IF NO NODES WERE EXPLORED THEN JUST REMOVE THE NODE FROM THE QUE
                    pq.remove(0);
                }
            }
        }
        public void AStar() {
            ArrayList<Element> pq = new ArrayList<Element>();
            pq.add(map[sourcex][sourcey]);
            while(issolved) {
                if(pq.size() <= 0) {
                    issolved = false;
                    break;
                }
                int hop = pq.get(0).getHop()+1;
                ArrayList<Element> exploredlist= exploreNbr(pq.get(0),hop);
                if(exploredlist.size() > 0) {
                    pq.remove(0);
                    pq.addAll(exploredlist);
                    Update();
                    delay();
                } else {
                    pq.remove(0);
                }
                queuesort(pq);	//SORT THE PRIORITY QUEUE
            }
        }

        public ArrayList<Element> queuesort(ArrayList<Element> q) {	//SORT PRIORITY QUE
            int count = 0;
            while(count < q.size()) {
                int t = count;
                for(int i = count+1; i < q.size(); i++) {
                    if(q.get(i).getEuclidDist()+q.get(i).getHop() < q.get(t).getEuclidDist()+q.get(t).getHop())
                        t = i;
                }
                if(count != t) {
                    Element temp = q.get(count);
                    q.set(count, q.get(t));
                    q.set(t, temp);
                }
                count++;
            }
            return q;
        }

        public ArrayList<Element> exploreNbr(Element e, int h) {	//EXPLORE NEIGHBORS
            ArrayList<Element> elementexp = new ArrayList<Element>();	//LIST OF NODES THAT HAVE BEEN EXPLORED
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++) {
                    int x = e.getX()+i;
                    int y = e.getY()+j;
                    if((x> -1 && x< dimension) && (y> -1 && y< dimension)) {	//MAKES SURE THE NODE IS NOT OUTSIDE THE GRID
                        Element nbr = map[x][y];
                        if((nbr.getHop()==-1 || nbr.getHop() > h) && nbr.getType()!=2) {	//CHECKS IF THE NODE IS NOT A WALL AND THAT IT HAS NOT BEEN EXPLORED
                            lookup(nbr, e.getX(), e.getY(), h);	//EXPLORE THE NODE
                            elementexp.add(nbr);	//ADD THE NODE TO THE LIST
                        }
                    }
                }
            }
            return elementexp;
        }

        public void lookup(Element e, int lx, int ly, int hop) {	//EXPLORE A ELEMENT
            if(e.getType()!=0 && e.getType() != 1)	//CHECK THAT THE NODE IS NOT THE START OR FINISH
                e.setType(4);	//SET IT TO EXPLORED
            e.setEndelem(lx, ly);	//KEEP TRACK OF THE NODE THAT THIS NODE IS EXPLORED FROM
            e.setHop(hop);	//SET THE HOPS FROM THE START
            noofchecks++;
            if(e.getType() == 1) {	//IF THE NODE IS THE FINISH THEN BACKTRACK TO GET THE PATH
                backtrack(e.getEndx(), e.getEndy(),hop);
            }
        }

        public void backtrack(int lx, int ly, int hop) {	//BACKTRACK
            pathlength = hop;
            while(hop > 1) {	//BACKTRACK FROM THE END OF THE PATH TO THE START
                Element e = map[lx][ly];
                e.setType(5);
                lx = e.getEndx();
                ly = e.getEndy();
                hop--;
            }
            issolved = false;
        }
    }



    class Element{

            // 0 = start, 1 = finish, 2 = wall, 3 = empty, 4 = checked, 5 = finalpath
            private int dimensiontype = 0;
            private int y;
            private int endx;
            private int endy;
            private int hop;
            private int x;
            private double End = 0;

            public Element(int dimtype, int x, int y) {	//CONSTRUCTOR
                dimensiontype = dimtype;
                this.x = x;
                this.y = y;
                hop = -1;
            }

            public double getEuclidDist() {		//CALCULATES THE EUCLIDIAN DISTANCE TO THE FINISH NODE
                int xdifference = Math.abs(x-endx);
                int ydifference = Math.abs(y-endy);
                End = Math.sqrt((xdifference*xdifference)+(ydifference*ydifference));
                return End;
            }

            public int getX() {return x;}		//GET METHODS
            public int getY() {return y;}
            public int getEndx() {return endx;}
            public int getEndy() {return endy;}
            public int getType() {return dimensiontype;}
            public int getHop() {return hop;}

            public void setType(int dimtype) {dimensiontype = dimtype;}		//SET METHODS
            public void setEndelem(int x, int y) {endx = x; endy = y;}
            public void setHop(int hop) {this.hop = hop;}
        }
    }



//}
