package TankWare;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class War extends Application { 
	public static void main(String[] args) {
		Application.launch(args);
	}
	public void start(Stage StartStage) {
		Pane pane = new Pane();
		//pane.setPadding(new Insets(5,5,5,5));
		Image BackGround = new Image("file:image/start.png");
		ImageView startBG = new ImageView(BackGround);
		startBG.setX(0);
		startBG.setY(0);
		Image startV = new Image("file:image/开始游戏.png");
		Image endV = new Image("file:image/退出游戏.png");
		
		Button start = new Button();
		start.setGraphic(new ImageView(startV));
		Button end = new Button();
		end.setGraphic(new ImageView(endV));
		pane.getChildren().addAll(startBG,start,end);
		start.setLayoutX(200);
		start.setLayoutY(200);
		end.setLayoutX(200);
		end.setLayoutY(350);
		
		end.setOnAction(e->System.exit(0));
		start.setOnAction(e->{
			new Game().running();
			StartStage.close();
		});
		
		Scene scene = new Scene(pane,Status.width,Status.heigth);
		StartStage.setTitle("The Tank War");
		StartStage.setResizable(false);
		StartStage.setScene(scene);
		StartStage.show();
	}
	
}

 class Game {
	 Text Score = new Text();
	 int status = Status.Runing;
	 private int level = 1;
	 private int Derection = Status.up;
	 private int map[][] = new int[30][30];//实际为21*12
	 private int life = Status.numOflife;
	 private  int enemy = 0;
	 private int numOfenemy = 0;
	 private int EDerection = 2;
	 int Vderection = 1;
	 private Timeline bT;
	 private Timeline ETM;
	 private int score = 0;
	 ImageView myTankV;
	 otank[] otankV = new otank[20];
	 Game() {
		 Pane pane = new Pane();
		 Text t = new Text();
		 t.setText("\n\n“W”向上移动，“S”向下移动，“A”向左移动，“D”向右移动。\n"
		 		+ "“J”发送炮弹"+"英雄机有3命，都用完后游戏结束，\n自家基地被攻击后游戏直接结束");
		 pane.getChildren().add(t);
		 Scene scene = new Scene(pane,300,100);
		 Stage s = new Stage();
		 s.setTitle("提示");
		 s.setScene(scene);
		 s.setAlwaysOnTop(true);
		 s.show();
	 }
	 void running(){
		 
		map = setMap();
		//游戏中背景	 
		Pane pane = new Pane();
		Image myTank = new Image("FIle:image/自己.png");
		myTankV = new ImageView(myTank);
		myTankV.setX(8*60);
		myTankV.setY(11*60);
		map[8][11] = Status.myTank;
		Image GameBV = new Image("File:image/游戏中背景.png");
		ImageView IGameBV = new ImageView(GameBV);
		IGameBV.setX(0);
		IGameBV.setY(0);
		
		Score.setFill(Color.WHITE);
		Score.setLayoutX(20);
		Score.setLayoutY(30);
		Score.setText("分数:"+score);
		pane.getChildren().add(IGameBV);
		pane.getChildren().add(myTankV);
		pane.getChildren().add(Score);	
		
		
		EventHandler<ActionEvent> enemyevent = e->{EnemyTank(pane);};
		Timeline ET = new Timeline(new KeyFrame(Duration.millis(10),enemyevent));
		ET.setCycleCount(Timeline.INDEFINITE);
		ET.play();
		
		CreateMap(pane);
		
		Scene scene = new Scene(pane,Status.width-10,Status.heigth-10);
		Stage RunningStage = new Stage();
		myTankV.setOnKeyPressed(e->MyMove(e.getCode(),myTankV,pane,RunningStage));
		RunningStage.setScene(scene);
		RunningStage.setTitle("The Tank War");
		RunningStage.setResizable(false);
		RunningStage.show();
		myTankV.requestFocus();
	}
	 
	 
	class otank{
		ImageView tank;
		boolean ot = true;
		void setX(double x) {
			tank.setX(x);
		}
		void setY(double y) {
			tank.setY(y);
		}
		double getX() {
			return tank.getX();
		}
		double getY() {
			return tank.getY();
		}
		void setRotate(double d) {
			tank.setRotate(d);
		}
		void setNull() {
			ot = false;
		}
	}
	
	
	 void EnemyTank(Pane pane) {
		 Image Iotank = new Image("File:image/敌人.png");
		 int i = 0;
			 while(enemy<Status.numOfOnStage&&numOfenemy<Status.numOfTank) {
				 Random rd = new Random();
				 int random = rd.nextInt(4);
				 otankV[i] = new otank();
				otankV[i].tank = new ImageView(Iotank);
				if(random == 1) {
					otankV[i].tank.setX(0);
					otankV[i].tank.setY(0);
					map[0][0] = Status.oTank;
				}
				else if(random == 2) {
					otankV[i].tank.setX(21*60);
					otankV[i].tank.setY(0);
					map[21][0] = Status.oTank;
				}
				else {
					otankV[i].tank.setX(10*60);
					otankV[i].tank.setY(0);
					map[10][0] = Status.oTank;
				}
				pane.getChildren().add(otankV[i].tank);
				final int k = i;
				EventHandler<ActionEvent> enemyevent = e->{EMove(otankV[k]);};
				ETM = new Timeline(new KeyFrame(Duration.millis(400),enemyevent));
				ETM.setCycleCount(Timeline.INDEFINITE);
				ETM.play();
				enemy++;
				i++;
			 }
			}
		 
		 
		ImageView[] Wall(double X,double Y,int n,int status){
			 double x = X;
			 double y = Y;
			 Image wall = new Image("File:image/墙.png");
			 ImageView[] wallV = new ImageView[n];
			 for(int i=0;i<n;i++) {
				 wallV[i] = new ImageView(wall);
				 wallV[i].setX(x*60);
				 wallV[i].setY(y*60);
				 map[(int)x][(int)y] = Status.wall;
				 if(status == Status.up)
					 y -= 1;
				 else if(status == Status.down) 
					 y += 1;
				 else if(status == Status.left)
					 x -= 1;
				 else
					 x += 1;
			 }
			 return wallV;
		 }
		 ImageView[] HardWall(double X,double Y,int n,int status) {
			 double x = X;
			 double y = Y;
			 Image hardwall = new Image("File:image/铁.png");
			 ImageView[] hardwallV = new ImageView[n];
			 for(int i=0;i<n;i++) {
				 hardwallV[i] = new ImageView(hardwall);
				 hardwallV[i].setX(x*60);
				 hardwallV[i].setY(y*60);
				 map[(int)x][(int)y] = Status.hardwall;
				 if(status == Status.up)
					 y -= 1;
				 else if(status == Status.down) 
					 y += 1;
				 else if(status == Status.left)
					 x -= 1;
				 else
					 x += 1;
			 }
			 return hardwallV;
		 }
		 ImageView[] Water(double X,double Y,int n,int status) {
			 double x = X;
			 double y = Y;
			 Image water = new Image("File:image/水.png");
			 ImageView[] waterV = new ImageView[n];
			 for(int i=0;i<n;i++) {
				 waterV[i] = new ImageView(water);
				 waterV[i].setX(x*60);
				 waterV[i].setY(y*60);
				 map[(int)x][(int)y] = Status.water;
				 if(status == Status.up)
					 y -= 1;
				 else if(status == Status.down) 
					 y += 1;
				 else if(status == Status.left)
					 x -= 1;
				 else
					 x += 1;
			 }
			 return waterV;
		 }
		 ImageView[] Grass(double X,double Y,int n,int status) {
			 double x = X;
			 double y = Y;
			 Image grass = new Image("File:image/草.png");
			 ImageView[] grassV = new ImageView[n];
			 for(int i=0;i<n;i++) {
				 grassV[i] = new ImageView(grass);
				 grassV[i].setX(x*60);
				 grassV[i].setY(y*60);
				 map[(int)x][(int)y] = Status.grass;
				 if(status == Status.up)
					 y -= 1;
				 else if(status == Status.down) 
					 y += 1;
				 else if(status == Status.left)
					 x -= 1;
				 else
					 x += 1;
			 }
			 return grassV;
		 }
		 ImageView[] GoFastWall(double X,double Y,int n,int status) {
			 double x = X;
			 double y = Y;
			 Image GoFastWall = new Image("File:image/滑块.png");
			 ImageView[] GoFastWallV = new ImageView[n];
			 for(int i=0;i<n;i++) {
				 GoFastWallV[i] = new ImageView(GoFastWall);
				 GoFastWallV[i].setX(x*60);
				 GoFastWallV[i].setY(y*60);
				 map[(int)x][(int)y] = Status.Goto;
				 if(status == Status.up)
					 y -= 1;
				 else if(status == Status.down) 
					 y += 2;
				 else if(status == Status.left)
					 x -= 3;
				 else
					 x += 4;
			 }
			 return GoFastWallV;
		 }
		 ImageView boss(double X,double Y) {
			 Image boss = new Image("File:image/boss.png");
			 ImageView bossV = new ImageView(boss);
			 bossV.setX(X*60);
			 bossV.setY(Y*60);
			 map[(int)X][(int)Y] = Status.boss;
			 return bossV;
		 }
		 
		 
		 
		 private int[][] setMap()
		 {
			 int[][] map1 = new int[30][30];
			 for(int i=0;i<30;i++)
			 {
				 for(int j=0;j<30;j++) {
					 map1[i][j] = Status.nothing;
				 }
			 }
			 return map1;
		 }
		 
		 private void MyMove(KeyCode code,ImageView myTankV,Pane pane,Stage RunningStage) {
			 {
					switch(code) {
					case W:
						if(Derection != Status.up) {
							myTankV.setRotate(0);
							Derection = Status.up;
						}
						if(myTankV.getY()>=1) {
							if(myTankV.getY()%60!=0) {
								map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
								myTankV.setY(myTankV.getY()-5);
								map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
								break;
							}
							else {
								if(myTankV.getX()%60 == 0) {
									if(map[((int)myTankV.getX()/60)][(((int)myTankV.getY())/60)-1] == Status.nothing)
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
										myTankV.setY(myTankV.getY()-5);
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
										break;
								}
								else if(myTankV.getX()%60 != 0&&map[((int)myTankV.getX()/60)][(((int)myTankV.getY())/60)-1] == Status.nothing)
									if(map[(((int)myTankV.getX())/60)-1][(((int)myTankV.getY())/60)-1] == Status.nothing) {
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
										myTankV.setY(myTankV.getY()-5);
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
										break;
									}
							}
						}
					case S:
						if(Derection != Status.down) {
							myTankV.setRotate(180);
							Derection = Status.down;
						}
						if(myTankV.getY()<=Status.heigth-61) {
							if(myTankV.getY()%60!=0) {
								map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
								myTankV.setY(myTankV.getY()+5);
								map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
								break;
							}
							else {
								if(myTankV.getX()%60 == 0) {
									if(map[(((int)myTankV.getX())/60)][(((int)myTankV.getY())/60)+1] == Status.nothing) {
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
										myTankV.setY(myTankV.getY()+5);
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
										break;
									}
								}
								else if(myTankV.getX()%60 != 0&&map[(((int)myTankV.getX())/60)][(((int)myTankV.getY())/60)+1] == Status.nothing) {
									if(map[(((int)myTankV.getX())/60)+1][(((int)myTankV.getY())/60)-1] == Status.nothing) {
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
										myTankV.setY(myTankV.getY()+5);
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
										break;
									}
									else
										break;
								}
								else
									break;
							}
						}
					case A:
						if(Derection != Status.left) {
							myTankV.setRotate(270);
							Derection = Status.left;
						}
						if(myTankV.getX()>=1) {
								if(myTankV.getX()%60!=0) {
									map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
									myTankV.setX(myTankV.getX()-5);
									map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
									break;
								}
								else {
									if(myTankV.getY()%60 == 0) {
										if(map[(((int)myTankV.getX())/60)-1][(((int)myTankV.getY())/60)] == Status.nothing) {
											map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
											myTankV.setX(myTankV.getX()-5);
											map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
											break;
										}
									}
									else if(myTankV.getY()%60 != 0&&map[(((int)myTankV.getX())/60)-1][(((int)myTankV.getY())/60)] == Status.nothing){
										if(map[(((int)myTankV.getX())/60)-1][(((int)myTankV.getY())/60)-1] == Status.nothing) {
											map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
											myTankV.setX(myTankV.getX()-5);
											map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
											break;
										}
									}
										
								}
							}
					case D:
						if(Derection != Status.right) {
							myTankV.setRotate(90);
							Derection = Status.right;
						}
						if(myTankV.getX()<=Status.width-60) {
							if(myTankV.getX()%60!=0) {
								map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
								myTankV.setX(myTankV.getX()+5);
								map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
								break;
							}
							else {
								if(myTankV.getY()%60 == 0) {
									if(map[(((int)myTankV.getX())/60)+1][(((int)myTankV.getY())/60)] == Status.nothing) {
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
										myTankV.setX(myTankV.getX()+5);
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
										break;
									}
								}
								else if(myTankV.getY()%60 != 0&&map[(((int)myTankV.getX())/60)+1][(((int)myTankV.getY())/60)] == Status.nothing){
									if(map[(((int)myTankV.getX())/60)+1][(((int)myTankV.getY())/60)+1] == Status.nothing) {
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.nothing;
										myTankV.setX(myTankV.getX()+5);
										map[(int)(myTankV.getX()/60)][(int)(myTankV.getY()/60)] = Status.myTank;
										break;
									}
								}
							}
						}
					case J:
						Boom(pane,myTankV,Derection,RunningStage);
					default:
						break;
					}
				}
		 }
		 
		 
		 void Boom(Pane pane,ImageView myTankV,int Derection,Stage RunningStage) {
			 Image boom = new Image("File:image/boom.png");
			 ImageView boomV = new ImageView(boom);
			 if(Derection == Status.up) {
				 boomV.setX((myTankV.getX()+myTankV.getX()+60)/2-8);
				 boomV.setY(myTankV.getY()-64);
			 }
			 else if(Derection == Status.down) {
				 boomV.setX((myTankV.getX()+myTankV.getX()+60)/2-8);
				 boomV.setY(myTankV.getY()+64);
			 }
			 else if(Derection == Status.left) {
				 boomV.setX(myTankV.getX()-64);
				 boomV.setY((myTankV.getY()+myTankV.getY()+60)/2-8);
			 }
			 else if(Derection == Status.right) {
				 boomV.setX(myTankV.getX()+64);
				 boomV.setY((myTankV.getY()+myTankV.getY()+60)/2-8);
			 }
			 
			 pane.getChildren().add(boomV);
			 EventHandler<ActionEvent> BE = e->{
				 if(status == Status.Runing) {
					 Bmove(boomV,Derection,pane,RunningStage);
				 }};
			 bT = new Timeline(new KeyFrame(Duration.millis(100),BE));
			 bT.setCycleCount(Timeline.INDEFINITE);
			 if(status == Status.Runing) {
				 bT.play();
			 }
			 
		 }
		 
		 
		 
		 private  void Bmove(ImageView boomV , int Derection,Pane pane,Stage RunningStage) {
			 switch(Derection) {
			 case Status.up:
				 if(boomV.getY()>10) {
					 if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.nothing) {
						 boomV.setX(boomV.getX());
						 boomV.setY(boomV.getY()-20);break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.wall) {
						 pane.getChildren().remove(boomV);
						 Image replace = new Image("File:image/墙替换.png");
						 ImageView replaceV = new ImageView(replace);
						 replaceV.setX((int)(boomV.getX()/60)*60);
						 replaceV.setY((int)(boomV.getY()/60)*60);
						 pane.getChildren().add(replaceV);
						 pane.getChildren().remove(myTankV);
						 pane.getChildren().add(myTankV);
						 map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]=Status.nothing;
						 bT.stop();break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.hardwall) {
						 pane.getChildren().remove(boomV);break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.oTank) {
						 pane.getChildren().remove(boomV);
						 for(int i=0; i<20;i++) {
							 if(otankV[i].ot==true) {
								 if(otankV[i].ot==true&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)) {
									 pane.getChildren().remove(otankV[i].tank);
									 map[(int)(otankV[i].getX()/60)][(int)(otankV[i].getX()/60)]=Status.nothing;
									 enemy -- ;
									 otankV[i].ot = false;
									 break;
								 }
							 }
							 else
								 continue;
						 }
						 score+=10;
						 Score.setText("分数:"+score);
						 bT.stop();
						 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.myTank) {
						 pane.getChildren().remove(boomV);
						 life --;
						 bT.stop();
						 if(life<=0) {
							new gameOver(Score,score);
							status = Status.Stop;
							RunningStage.close();break;
						 }
						 else
							 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.boss) {
						 pane.getChildren().remove(boomV);
						 status = Status.Stop;
						 bT.stop();
						 new gameOver(Score,score);
						 RunningStage.close();break;				
						 }
				 }
				 else {
					 pane.getChildren().remove(boomV);break;
				 }
			 case Status.down:
				 if(boomV.getY()<=Status.heigth-10) {
					 if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.nothing) {
						 boomV.setX(boomV.getX());
						 boomV.setY(boomV.getY()+20);break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.wall) {
						 pane.getChildren().remove(boomV);
						 Image replace = new Image("File:image/墙替换.png");
						 ImageView replaceV = new ImageView(replace);
						 replaceV.setX((int)(boomV.getX()/60)*60);
						 replaceV.setY((int)(boomV.getY()/60)*60);
						 pane.getChildren().add(replaceV);
						 pane.getChildren().remove(myTankV);
						 pane.getChildren().add(myTankV);
						 map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]=Status.nothing;
						 bT.stop();break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.hardwall) {
						 pane.getChildren().remove(boomV);break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.oTank) {
						 pane.getChildren().remove(boomV);
						 for(int i=0; i<20;i++) {
							 if(otankV[i].ot==true) {
								 if(otankV[i].ot==true&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)) {
									 pane.getChildren().remove(otankV[i].tank);
									 map[(int)(otankV[i].getX()/60)][(int)(otankV[i].getX()/60)]=Status.nothing;
									 enemy -- ;
									 otankV[i].ot = false;
									 break;
								 }
							 }
							 else
								 continue;
						 }
						 score+=10;
						 Score.setText("分数:"+score);
						 bT.stop();
						 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.myTank) {
						 pane.getChildren().remove(boomV);
						 life --;
						 bT.stop();
						 if(life<=0) {
							new gameOver(Score,score);
							status = Status.Stop;
							RunningStage.close();break;
						 }
						 else
							 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.boss) {
						 pane.getChildren().remove(boomV);
						 status = Status.Stop;
						 bT.stop();
						 new gameOver(Score,score);
						 RunningStage.close();break;
						 }
				 }
				 else {
					 pane.getChildren().remove(boomV);break;
				 }
			 case Status.left:
				 if(boomV.getX()>=10) {
					 if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.nothing) {
						 boomV.setX(boomV.getX()-20);
						 boomV.setY(boomV.getY());break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.wall) {
						 pane.getChildren().remove(boomV);
						 Image replace = new Image("File:image/墙替换.png");
						 ImageView replaceV = new ImageView(replace);
						 replaceV.setX((int)(boomV.getX()/60)*60);
						 replaceV.setY((int)(boomV.getY()/60)*60);
						 pane.getChildren().add(replaceV);
						 pane.getChildren().remove(myTankV);
						 pane.getChildren().add(myTankV);
						 map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]=Status.nothing;
						 bT.stop();break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.hardwall) {
						 pane.getChildren().remove(boomV);break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.oTank) {
						 pane.getChildren().remove(boomV);
						 for(int i=0; i<20;i++) {
							 if(otankV[i].ot==true) {
								 if(otankV[i].ot==true&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)) {
									 pane.getChildren().remove(otankV[i].tank);
									 map[(int)(otankV[i].getX()/60)][(int)(otankV[i].getX()/60)]=Status.nothing;
									 enemy -- ;
									 otankV[i].ot = false;
									 break;
								 }
							 }
							 else
								 continue;
						 }
						 score+=10;
						 Score.setText("分数:"+score);
						 bT.stop();
						 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.myTank) {
						 pane.getChildren().remove(boomV);
						 life --;
						 bT.stop();
						 if(life<=0) {
							new gameOver(Score,score);
							status = Status.Stop;
							RunningStage.close();break;
						 }
						 else
							 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.boss) {
						 pane.getChildren().remove(boomV);
						 new gameOver(Score,score);
						 status = Status.Stop;
						 bT.stop();
						 RunningStage.close();break;
						 }
				 }
				 else {
					 pane.getChildren().remove(boomV);break;
				 }
			 case Status.right:
				 if(boomV.getX()<Status.width-60) {
					 if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.nothing) {
						 boomV.setX(boomV.getX()+20);
						 boomV.setY(boomV.getY());break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.wall) {
						 pane.getChildren().remove(boomV);
						 Image replace = new Image("File:image/墙替换.png");
						 ImageView replaceV = new ImageView(replace);
						 replaceV.setX((int)(boomV.getX()/60)*60);
						 replaceV.setY((int)(boomV.getY()/60)*60);
						 pane.getChildren().add(replaceV);
						 pane.getChildren().remove(myTankV);
						 pane.getChildren().add(myTankV);
						 map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]=Status.nothing;
						 bT.stop();break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.hardwall) {
						 pane.getChildren().remove(boomV);break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.oTank) {
						 pane.getChildren().remove(boomV);
						 for(int i=0; i<20;i++) {
							 if(otankV[i].ot==true) {
								 if(otankV[i].ot==true&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)&&(int)(otankV[i].getX()/60)==(int)(boomV.getX()/60)) {
									 pane.getChildren().remove(otankV[i].tank);
									 map[(int)(otankV[i].getX()/60)][(int)(otankV[i].getX()/60)]=Status.nothing;
									 enemy -- ;
									 otankV[i].ot = false;
									 break;
								 }
							 }
							 else
								 continue;
						 }
						 score+=10;
						 Score.setText("分数:"+score);
						 bT.stop();
						 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.myTank) {
						 pane.getChildren().remove(boomV);
						 life --;
						 bT.stop();
						 if(life<=0) {
							new gameOver(Score,score);
							status = Status.Stop;
							RunningStage.close();break;
						 }
						 else
							 break;
					 }
					 else if(map[(int)(boomV.getX()/60)][(int)(boomV.getY()/60)]==Status.boss) {
						 pane.getChildren().remove(boomV);
						 new gameOver(Score,score);
						 status = Status.Stop;
						 bT.stop();
						 RunningStage.close();break;
						 } 
				 }
				 else {
					 pane.getChildren().remove(boomV);break;
				 }
			 }
				 
		 }
		 
		 private void EMove(otank otankV) {
			 Random rd = new Random();
			 switch(EDerection) {
			 case Status.up:
				 if(Vderection != Status.up) {
					 otankV.setRotate(0);
					 Vderection = Status.up;
				 }
				 if(otankV.getY()>=1&&otankV.ot==true) {
					 if(map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)-1]==Status.nothing) {
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.nothing;
						 otankV.setX(otankV.getX());
						 otankV.setY(otankV.getY()-60);
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.oTank;
					 }
					 else {
						 EDerection = rd.nextInt(4)+1;break;
					 }
				 }
				 else {
					 EDerection = rd.nextInt(4)+1;break;
				 }
			 case Status.down:
				 if(Vderection != Status.down) {
					 otankV.setRotate(180);
					 Vderection = Status.down;
				 }
				 if(otankV.getY()<Status.heigth-60&&otankV.ot == true) {
					 if(map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)+1]==Status.nothing) {
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.nothing;
						 otankV.setX(otankV.getX());
						 otankV.setY(otankV.getY()+60);
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.oTank;
					 }
					 else {
						 EDerection = rd.nextInt(4)+1;break;
					 }
				 }
				 else {
					 EDerection = rd.nextInt(4)+1;break;
				 }
			 case Status.left:
				 if(Vderection != Status.left) {
					 otankV.setRotate(270);
					 Vderection = Status.left;
				 }
				 if(otankV.getX()>=1) {
					 if(map[(int)(otankV.getX()/60)-1][(int)(otankV.getY()/60)]==Status.nothing) {
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.nothing;
						 otankV.setX(otankV.getX()-60);
						 otankV.setY(otankV.getY());
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.oTank;
					 }
					 else {
						 EDerection = rd.nextInt(4)+1;break;
					 }
				 }
				 else {
					 EDerection = rd.nextInt(4)+1;break;
				 }
			 case Status.right:
				 if(Vderection != Status.right) {
					 otankV.setRotate(90);
					 Vderection = Status.right;
				 }
				 if(otankV.getX()<=Status.width-120&&otankV.ot == true) {
					 if(map[(int)(otankV.getX()/60)+1][(int)(otankV.getY()/60)]==Status.nothing) {
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.nothing;
						 otankV.setX(otankV.getX()+60);
						 otankV.setY(otankV.getY());
						 map[(int)(otankV.getX()/60)][(int)(otankV.getY()/60)] = Status.oTank;
					 }
					 else {
						 EDerection = rd.nextInt(4)+1;break;
					 }
				 }
				 else {
					 EDerection = rd.nextInt(4)+1;break;
				 }
				 default:
					 EDerection = rd.nextInt(4)+1;break;
			 }
			 
			}
		 
		 private void CreateMap(Pane pane) {
			 if(level==1) {
					//boss
					pane.getChildren().addAll(Wall(9, 11, 2, Status.up));
					pane.getChildren().addAll(Wall(10, 10, 1, Status.right));
					pane.getChildren().add(boss(10, 11));
					pane.getChildren().addAll(Wall(11, 10, 2, Status.down));
					//map。红砖
					pane.getChildren().addAll(Wall(1, 10, 3, Status.up));			
					pane.getChildren().addAll(Wall(3, 10, 3, Status.up));
					pane.getChildren().addAll(Wall(5, 10, 3, Status.up));			
					pane.getChildren().addAll(Wall(7, 10, 3, Status.up));
					pane.getChildren().addAll(Wall(13, 10, 3, Status.up));
					pane.getChildren().addAll(Wall(15, 10, 3, Status.up));
					pane.getChildren().addAll(Wall(17, 10, 3, Status.up));			
					pane.getChildren().addAll(Wall(19, 10, 3, Status.up));
					
					//map。红砖
					pane.getChildren().addAll(Wall(1, 4, 4, Status.up));			
					pane.getChildren().addAll(Wall(3, 4, 4, Status.up));
					pane.getChildren().addAll(Wall(5, 4, 4, Status.up));			
					pane.getChildren().addAll(Wall(7, 4, 4, Status.up));
					pane.getChildren().addAll(Wall(13, 4, 4, Status.up));
					pane.getChildren().addAll(Wall(15, 4, 4, Status.up));
					pane.getChildren().addAll(Wall(17, 4, 4, Status.up));			
					pane.getChildren().addAll(Wall(19, 4, 4, Status.up));
					
					pane.getChildren().addAll(Wall(9, 2, 2, Status.up));	
					pane.getChildren().addAll(Wall(11, 2, 2, Status.up));
					
					
					pane.getChildren().addAll(Wall(9, 6, 3, Status.down));	
					pane.getChildren().addAll(Wall(11, 6, 3, Status.down));

					pane.getChildren().addAll(Wall(5, 6, 3, Status.right));			
					pane.getChildren().addAll(Wall(13, 6, 3, Status.right));
					pane.getChildren().addAll(Wall(10, 7, 1, Status.up));
					pane.getChildren().addAll(Wall(9, 4, 1, Status.up));
					pane.getChildren().addAll(Wall(11, 4, 1, Status.up));		
					
					//map.铁
					pane.getChildren().addAll(HardWall(0, 6, 4, Status.right));
					pane.getChildren().addAll(HardWall(20, 6, 4, Status.left));
					pane.getChildren().addAll(HardWall(10, 2, 1, Status.right));
				}
				else {
					System.out.print("You have won!");
					System.exit(1);
				} 
		 }
	 
}

 class gameOver {
	 gameOver(Text Score,int score){
		 Pane pane = new Pane();
		 Stage EndStage = new Stage();
		 Image im = new Image("File:image/游戏结束.png");
		 ImageView imV = new ImageView(im);
		 imV.setX(0);
		 imV.setY(0);
		 pane.getChildren().add(imV);
		 Button bt1 = new Button("退出游戏");
		 Button bt2 = new Button("重新开始");
		 bt1.setLayoutX(200);
		 bt1.setLayoutY(200);
		 bt2.setLayoutX(200);
		 bt2.setLayoutY(350);
		 
		 
		 Score.setLayoutX(600);
		 Score.setLayoutY(100);
		 Score.setStyle("-fx-border-color: white");
		 Score.setText("分数："+score);
		 
		 pane.getChildren().addAll(bt1,bt2,Score);
		 
		 bt1.setOnAction(e->System.exit(0));
		 bt2 .setOnAction(e->{
			new Game().running();
			EndStage.close();
		});
		 
		 Scene scene = new Scene(pane,Status.width,Status.heigth);
		 EndStage.setTitle("The Tank War");
		 EndStage.setResizable(false);
		 EndStage.setScene(scene);
		 EndStage.show();
	 }
 }
class Status{
	//地图构建标志
	//坦克朝向标识
	static final int up = 1;
	static final int down = 2;
	static final int left = 3;
	static final int right = 4;
	
	//地图标识
	
	static final int nothing = -1;
	static final int wall = 0;
	static final int hardwall = 1;
	static final int boss = 2;
	static final int grass = 3;
	static final int water = 4;
	static final int Goto = 5;
	static final int myTank = 6;
	static final int oTank = 7;
	
	//窗口大小
	static final double width = 1260;
	static final double heigth = 720;
	
	//游戏状态标识
	static final int Runing = 1;
	static final int Stop = -1;
	static final int Pause = 0;
	
	//数量限定
	static final int numOfTank = 20;
	static final int numOfOnStage = 5;
	static final int numOflife = 3;
}