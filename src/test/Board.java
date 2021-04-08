package test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static test.ClassManager.consoleIn;
import static test.ClassManager.consoleOut;


public class Board extends Application {
    int timeLimit = 2000;
    int testNumber = 100;
    TextField timeLimitSet;
    TextField testCaseSet;
    Label runLabel;


    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox pane = new HBox();
        VBox left = new VBox();
        VBox right = new VBox();

        left.setPadding(new Insets(20));
        left.setAlignment(Pos.TOP_CENTER);
        left.setSpacing(20);
        left.setBackground(new Background(new BackgroundFill(Color.rgb(250,250,250),null,null)));

        GridPane settings = new GridPane();
        settings.add(new Label("设置："),0,0);
        timeLimitSet = new TextField(""+2000);
        settings.add(new Label("时间限制设置："),0,1);
        settings.add(timeLimitSet,0,2);

        testCaseSet = new TextField(""+100);
        settings.add(new Label("样例数量设置："),0,3);
        settings.add(testCaseSet,0,4);

        HBox runBox = new HBox();
        runBox.setSpacing(10);
        runLabel = new Label("0%");
        runLabel.setAlignment(Pos.CENTER);
        runLabel.setFont(Font.font("Arial", FontWeight.BOLD,16));
        runLabel.setPadding(new Insets(5));
        runLabel.setPrefWidth(100);
        runLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,new CornerRadii(5),null)));
        Button runBtn = new Button("开始测试");
        runBox.getChildren().addAll(runBtn,runLabel);
        left.getChildren().add(runBox);
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    runTest(right);
                } catch (IOException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        left.getChildren().add(settings);

        String tipStr = "使用说明：\n替换Tcg.java文件来生成样例（建议使用快写）\n替换Answer.java文件为标准答案\n替换Test.java文件为你的答案\n" +
                "\n时间限制受电脑性能影响，仅供参考，建议设置为1000ms内；样例数量建议设置为1000以内。\n"+
                "\n本软件仅供分享调试使用，请勿用于其他用途。如因使用本软件导致不良后果，作者不承担任何责任。";
        Label tips = new Label(tipStr);
        tips.setBackground(new Background(new BackgroundFill(Color.rgb(235,235,235),new CornerRadii(5),null)));
        tips.setPadding(new Insets(10));
        tips.setPrefWidth(180);
        tips.setWrapText(true);
        tips.setFont(Font.font("Arial", 14));
        left.getChildren().add(tips);


        ScrollPane rightS = new ScrollPane(right);
        rightS.setPrefHeight(600);
        rightS.setPrefWidth(550);
        rightS.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        pane.getChildren().addAll(left,rightS);
        Scene mainScene = new Scene(pane);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("控制台");
        primaryStage.show();

    }

    public void runTest(VBox right) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        timeLimit = Integer.parseInt(timeLimitSet.getText());
        testNumber = Integer.parseInt(testCaseSet.getText());

        right.getChildren().clear();
        ClassManager cases = new ClassManager("empty.txt","testcase.txt","Tcg");
        ClassManager test = new ClassManager("testcase.txt","resulttest.txt","Test");
        ClassManager answer = new ClassManager("testcase.txt","resultanswer.txt","Answer");
        TextOperator tc = new TextOperator("resulttest.txt","resultanswer.txt","testcase.txt");
        int total = 0;
        int size = testNumber;
        boolean haveWrong = false;
        for (int i = 1; i <= size; i++) {

            cases.run();
            answer.run();
            long timeTmp = System.currentTimeMillis();
            try {
                test.run();
            } catch (Exception e) {
                System.setIn(consoleIn);
                System.setOut(consoleOut);
                int time = (int)(System.currentTimeMillis() - timeTmp);
                right.getChildren().add(new Info(i,3,time));
                if (!haveWrong) {
                    haveWrong = true;
                    tc.compare();
                    logMistake(tc,i,3);
                }
                continue;
            }
            int time = (int)(System.currentTimeMillis() - timeTmp);


            if (tc.compare()) {
                if (time >= timeLimit) {
                    right.getChildren().add(new Info(i,2,time));
                    if (!haveWrong) {
                        haveWrong = true;
                        logMistake(tc,i,2);
                    }
                } else {
                    right.getChildren().add(new Info(i,0,time));
                    total++;
                }

            } else {
                right.getChildren().add(new Info(i,1,time));
                if (!haveWrong) {
                    haveWrong = true;
                    logMistake(tc,i,1);
                }
            }
        }

        if (total == size) {
            runLabel.setText("100%");
            runLabel.setBackground( new Background(new BackgroundFill(Color.LIGHTGREEN,new CornerRadii(5),null)));
        } else {
            runLabel.setText((total*100)/size + "%");
            runLabel.setBackground( new Background(new BackgroundFill(Color.rgb(255,120,120),new CornerRadii(5),null)));
        }

        tc.close();
    }

    public void logMistake(TextOperator tc, int caseIndex, int op) {
        System.out.println("----------------------------------------------------------------------------");
        System.out.print("样例：");
        tc.print(3);
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("第一次错误发生在样例 "+caseIndex+" , 样例详见上方。");
        System.out.print("标准答案：");
        tc.print(2);
        System.out.println();
        if (op != 3) System.out.print("你的答案：");
        if (op != 3) tc.print(1);
        System.out.println();
        System.out.println("出现Wrong Answer时，如果出现以下问题，则你的答案正确，只是被测试系统误判：");
        System.out.println("1.注意是否存在空格回车差异，文本最后一行有无回车也会影响结果。");
        System.out.println("2.全局变量必须在main方法内再次初始化，否则会影响结果。");
        System.out.println("[重要] 再次测试时，如果你修改了你的答案或样例生成代码，则需要重新运行程序才能生效。");
        System.out.println("----------------------------------------------------------------------------");
    }

    static class Info extends HBox{

        public Info(int index, int state, int time) {
            if (index %2 == 0) {
                setBackground(new Background(new BackgroundFill(Color.rgb(230,230,230),null,null)));
            } else {
                setBackground(new Background(new BackgroundFill(Color.rgb(240,240,240),null,null)));
            }

            setPadding(new Insets(10));
            getChildren().add(new Cell("Test Case " + index,Color.rgb(220,210,250)));
            if (state == 0) {
                getChildren().add(new Cell("Accepted",Color.LIGHTGREEN));
            } else if (state == 1) {
                getChildren().add(new Cell("Wrong Answer",Color.rgb(255,120,120)));
            } else if (state == 2) {
                getChildren().add(new Cell("Time Limit Exceeded",Color.ORANGE));
            } else if (state == 3) {
                getChildren().add(new Cell("Run Time Error",Color.ORANGE));
            }
            getChildren().add(new Cell(time + "ms", Color.rgb(220,210,250)));
        }

    }

    static class Cell extends Pane {

        public Cell(String info, Color color) {
            setPrefWidth(180);
            Label l = new Label(info);
            l.setPadding(new Insets(5));
            l.setBackground(new Background(new BackgroundFill(color,new CornerRadii(5),null)));
            getChildren().add(l);
        }
    }
}
