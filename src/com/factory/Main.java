package com.factory;

import com.factory.persistence.ProductLineRepository;
import com.factory.persistence.TaskRepository;
import com.factory.persistence.UserRepository;
import com.factory.persistence.MaterialRepository;
import com.factory.persistence.ProductRepository;
import com.factory.service.AuthService;
import com.factory.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        String basePath = "C:/factory_data/";

        ProductLineRepository plRepo =
                new ProductLineRepository(basePath + "product_lines.csv");

        TaskRepository taskRepo =
                new TaskRepository(basePath + "tasks.csv");

        MaterialRepository materialRepo = new MaterialRepository(basePath + "materials.csv");
        ProductRepository productRepo = new ProductRepository(basePath + "products.csv");

        UserRepository userRepository = new UserRepository();
        AuthService authService = new AuthService(userRepository);

        AppContext ctx = new AppContext(plRepo, taskRepo, materialRepo, productRepo);

         SwingUtilities.invokeLater(() -> {
          new LoginFrame(authService ,materialRepo , ctx).setVisible(true);
         });


        System.out.println( materialRepo.findAllByName ("b3b3")) ;

    }
}
