package houseplan;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SpringBootApplication
public class HouseplanMain extends Application {

//    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
//        String[] args = getParameters().getRaw().toArray(new String[]{});
//        applicationContext = SpringApplication.run(getClass(), args);
//        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//        String[] beanNames = applicationContext.getBeanDefinitionNames();
//        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }

        VBox page = (VBox) FXMLLoader.load(HouseplanMain.class.getResource("MainFrame.fxml"));
        Scene scene = new Scene(page);
        primaryStage.setScene(scene);
        primaryStage.setTitle("House Plan");
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
//        applicationContext.close();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}