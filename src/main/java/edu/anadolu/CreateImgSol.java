package edu.anadolu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class CreateImgSol{

    final static Point[] cityPoints= new Point[]{
            new Point(1292,1094),//Adana
            new Point(1651,974),//Adıyaman
            new Point(713,808),//Afyon
            new Point(2209,640),//Ağrı
            new Point(1348,532),//Amasya
            new Point(995,638),//Ankara
            new Point(717,1098),//Antalya
            new Point(2045,420),//Artvin
            new Point(378,927),//Aydın
            new Point(405,652),//Balıkesir
            new Point(660,597),//Bilecik
            new Point(1910,788),//Bingöl
            new Point(2110,850),//Bitlis--13
            new Point(854,510),//Bolu
            new Point(671,966),//Burdur
            new Point(551,578),//Bursa
            new Point(241,564),//Çanakkale
            new Point(1090,540),//Çankırı
            new Point(1247,550),//Çorum
            new Point(527,950),//Denizli--20
            new Point(1888,938),//Diyarbakır
            new Point(278,328),//Edirne
            new Point(1761,827),//Elazığ--23
            new Point(1785,662),//Erzincan
            new Point(1997,622),//Erzurum
            new Point(718,652),//Eskişehir
            new Point(1552,1081),//Gaziantep
            new Point(1646,488),//Giresum
            new Point(1779,550),//Gümüşhane--29
            new Point(2320,961),//Hakkari
            new Point(1396,1217),//Hatay
            new Point(706,961),//Isparta
            new Point(1202,1126),//Mersin--33
            new Point(548,453),//İstanbul
            new Point(303,831),//İzmir
            new Point(2202,505),//Kars
            new Point(1110,420),//Kastamonu
            new Point(1307,828),//Kayseri
            new Point(354,328),//Kırklareli
            new Point(1150,765),//Kırşehir
            new Point(657,498),//Kocaeli
            new Point(941,955),//Konya--42
            new Point(652,707),//Kütahya
            new Point(1651,880),//Malatya
            new Point(339,805),//Manisa--45
            new Point(1486,1005),//Maraş
            new Point(1956,1028),//Mardin
            new Point(434,1029),//Muğla
            new Point(2234,803),//Muş
            new Point(1216,843),//Nevşehir
            new Point(1213,945),//Niğde
            new Point(1586,478),//Ordu--52
            new Point(1895,462),//Rize
            new Point(713,503),//Sakarya
            new Point(1407,435),//Samsun
            new Point(2100,925),//Siirt
            new Point(1271,324),//Sinop
            new Point(1492,670),//Sivas
            new Point(376,445),//Tekirdağ--59
            new Point(1436,585),//Tokat
            new Point(1803,467),//Trabzon
            new Point(1796,760),//Tunceli
            new Point(1718,1064),//Urfa
            new Point(577,815),//Uşak
            new Point(2263,823),//Van--65
            new Point(1230,660),//Yozgat
            new Point(880,404),//Zonguldak
            new Point(1134,883),//Aksaray--68
            new Point(1868,578),//Bayburt
            new Point(1032,1064),//Karaman
            new Point(1076,654),//Kırıkkale--71
            new Point(2000,937),//Batman
            new Point(2165,984),//Şırnak
            new Point(945,377),//Bartın
            new Point(2150,429),//Ardahan--75
            new Point(2324,606),//Iğdır
            new Point(582,510),//Yalova
            new Point(975,443),//Karabük
            new Point(1511,1136),//Kilis
            new Point(1402,1080),//Osmaniye
            new Point(802,494)//Düzce
    };
    final static File rootDir = new File(System.getProperty("user.dir"));
    final static File imgDir = new File(rootDir.getPath() + "\\src" + "\\main" + "\\resources" + "\\img"+ "\\tr-map2.jpg");
    final static Image img = new ImageIcon(imgDir.getAbsolutePath()).getImage();

    private CreateImgSol(){};
    public static void create(File jsonDir)  throws IOException,ParseException {
        //Image to graphic
        BufferedImage buffImg = ImageIO.read(imgDir);
        BufferedImage buffSBI = new BufferedImage(buffImg.getWidth(),buffImg.getHeight(),buffImg.TYPE_INT_RGB);
        Graphics2D g2D = buffSBI.createGraphics();
        g2D.setComposite(AlphaComposite.Src);
        g2D.drawImage(img,0,0, buffImg.getWidth(), buffImg.getHeight(), null);

        //Some Colour
        Color clr1; // Paths
        Color clr2 = new Color(255, 57, 0); // Depots
        Color clr3 = new Color(0, 251, 255); // Cities

        int rOfCityMark = 30;
        g2D.setStroke(new BasicStroke(4));

        //Read JSON
        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(jsonDir));
        JSONArray listObj = (JSONArray) json.get("solution");

        for(Object obj: listObj){
            String theDpot = (String) ((JSONObject)obj).get("depot");
            int theDpotInt = Integer.parseInt(theDpot);
            Point theDpotPoint = cityPoints[theDpotInt];

            JSONArray routesOfTheDepot = (JSONArray) ((JSONObject)obj).get("routes");
            for(Object rotesStr: routesOfTheDepot){

                Random rNum = new Random();
                clr1 = new Color(rNum.nextInt(256),
                        rNum.nextInt(256),
                        rNum.nextInt(256));


                String[] cities = ((String)rotesStr).split(" ");
                System.out.println();

                //First City
                int firstCity = Integer.parseInt(cities[0]);
                Point firstCityP = cityPoints[firstCity];
                //Draw firt path
                g2D.setColor(clr1);
                g2D.drawLine(
                        theDpotPoint.x,
                        theDpotPoint.y,
                        firstCityP.x,
                        firstCityP.y
                );
                for(int i = 0; i < cities.length-1; i++){
                    int curCity = Integer.parseInt(cities[i]);
                    int nextCity = Integer.parseInt(cities[i+1]);
                    Point curCityP = cityPoints[curCity];
                    Point nextCityP = cityPoints[nextCity];

                    //Draw line between curCity and nextCity
                    g2D.setColor(clr1);
                    g2D.drawLine(
                            curCityP.x,
                            curCityP.y,
                            nextCityP.x,
                            nextCityP.y
                    );
                    //Mark curCity
                    g2D.setColor(clr3);
                    g2D.fillOval(
                            curCityP.x-(rOfCityMark/2),
                            curCityP.y-(rOfCityMark/2),
                            rOfCityMark,
                            rOfCityMark
                    );
                }
                //Last City
                int lastCity = Integer.parseInt(cities[cities.length-1]);
                Point lastCityP = cityPoints[lastCity];
                //Draw last path
                g2D.setColor(clr1);
                g2D.drawLine(
                        lastCityP.x,
                        lastCityP.y,
                        theDpotPoint.x,
                        theDpotPoint.y
                );
                //Mark lastCity
                g2D.setColor(clr3);
                g2D.fillOval(
                        lastCityP.x-(rOfCityMark/2),
                        lastCityP.y-(rOfCityMark/2),
                        rOfCityMark,
                        rOfCityMark
                );
            }
            //mark theDepot
            g2D.setColor(clr2);
            g2D.fillOval(
                    theDpotPoint.x-(rOfCityMark/2),
                    theDpotPoint.y-(rOfCityMark/2),
                    rOfCityMark,
                    rOfCityMark
            );
        }

        g2D.dispose();
        File solImdDir = new File(rootDir.getAbsolutePath() + "\\" + jsonDir.getName() + ".jpg");
        ImageIO.write(buffSBI,"jpg", solImdDir);
        /*
        g2D.setColor(clr1);
        g2D.drawLine(cityPoints[0].x,cityPoints[0].y,cityPoints[1].x,cityPoints[1].y);
        //g2D.drawLine(0,0,1415,768);
        g2D.setColor(clr2);
        int r = 30;
        //g2D.fillOval(700,500,30,30);
        for(Point p : cityPoints){
            g2D.fillOval(p.x-(r/2),p.y-(r/2),r,r);
        }


        g2D.dispose();
        ImageIO.write(buffSBI,"gif", solution);*/
    }

    public static void main(String[] args)  throws IOException,ParseException {
        create(new File(System.getProperty("user.dir")+"\\solution.json"));
    }
}
